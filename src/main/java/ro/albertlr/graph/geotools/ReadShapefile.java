/*-
 * #%L
 * All-Pair Shortest Path
 *  
 * Copyright (C) 2019 László-Róbert, Albert (robert@albertlr.ro)
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ro.albertlr.graph.geotools;

import com.google.common.base.Throwables;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import ro.albertlr.graph.Node;
import ro.albertlr.graph.Road;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@UtilityClass
@Slf4j
public class ReadShapefile {

    public static Graph<Node, Road> importFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            log.error("The file {} does not exists", filename);
            return null;
        }

        Supplier<Node> nodeSupplier = null;
        Supplier<Road> roadSupplier = null;

        Graph<Node, Road> roadNetwork = new DefaultDirectedWeightedGraph<>(nodeSupplier, roadSupplier);

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        System.out.printf("typeNames: %s%n", Arrays.toString(store.getTypeNames()));
        String typeName = store.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();

                Road road = Road.builder().featureId(feature.getID()).build();

                for (Property property : feature.getProperties()) {
                    setProperty(property, road);
                }

                log.debug(road.toString());

                if (!roadNetwork.containsVertex(road.getA())) {
                    roadNetwork.addVertex(road.getA());
                }
                if (!roadNetwork.containsVertex(road.getB())) {
                    roadNetwork.addVertex(road.getB());
                }

                roadNetwork.addEdge(road.getA(), road.getB(), road);
            }
        } catch (IllegalArgumentException exception) {
            final String exceptionMessage = exception.getMessage();
            if (exceptionMessage.startsWith("Expected requestor org.geotools.data.shapefile.dbf.DbaseFileReader")
                    && exceptionMessage.endsWith("to have locked the url but it does not hold the lock for the URL")) {
                log.warn("Known issue while reading DBaseFile", exception);
            } else {
                Throwables.throwIfUnchecked(exception);
            }
        }

        log.info("===");
        log.info("network: {}", roadNetwork);
        log.info("===");

        return roadNetwork;
    }

    private static Map<String, Node> nodeCache = new HashMap<>();

    private static void setProperty(Property property, Road road) {
        log.trace("\tproperty: [{}<{}>={}]; {}", property.getName(), property.getDescriptor().getName(), property.getValue(), property);

        String key = property.getName().toString();
        if (property.getValue() == null) {
            log.debug("Skipping null property {}", key);
            return;
        }
        String value = String.valueOf(property.getValue());
        switch (key) {
            case "NODEA": {
                Node nodeA = getOrCreate(value);

                road.setA(nodeA);
                nodeA.getRoadsFrom().add(road);
            }
            break;
            case "NODEB": {
                Node nodeB = getOrCreate(value);

                road.setB(nodeB);
                nodeB.getRoadsTo().add(road);
            }
            break;
            case "ID":
                set(road::setId, Parsers.LONG.getParser(), value);
                break;
            case "SP_F":
                road.setSP_F((int) Double.parseDouble(value));
                break;
            case "SP_B":
                road.setSP_B((int) Double.parseDouble(value));
                break;
            case "SP_F_M":
                road.setSP_F_M((int) Double.parseDouble(value));
                break;
            case "SP_B_M":
                set(road::setSP_B_M, Parsers.DOUBLE.getParser(), value);
                break;
            case "CAP_F_M":
                set(road::setCAP_F_M, Parsers.DOUBLE.getParser(), value);
                break;
            case "CAP_B_M":
                road.setCAP_B_M((int) Double.parseDouble(value));
                break;
            case "LD_F_M":
                road.setLD_F_M((int) Double.parseDouble(value));
                break;
            case "LD_B_M":
                road.setLD_B_M((int) Double.parseDouble(value));
                break;
            case "SPF_F_M":
                road.setSPF_F_M((int) Double.parseDouble(value));
                break;
            case "SPF_B_M":
                road.setSPF_B_M((int) Double.parseDouble(value));
                break;
            case "LDF_F_M":
                road.setLDF_F_M((int) Double.parseDouble(value));
                break;
            case "LDF_B_M":
                road.setLDF_B_M((int) Double.parseDouble(value));
                break;
            case "SP_F_E":
                road.setSP_F_E((int) Double.parseDouble(value));
                break;
            case "SP_B_E":
                road.setSP_B_E((int) Double.parseDouble(value));
                break;
            case "CAP_F_E":
                road.setCAP_F_E((int) Double.parseDouble(value));
                break;
            case "CAP_B_E":
                road.setCAP_B_E((int) Double.parseDouble(value));
                break;
            case "LD_F_E":
                road.setLD_F_E((int) Double.parseDouble(value));
                break;
            case "LD_B_E":
                road.setLD_B_E((int) Double.parseDouble(value));
                break;
            case "SPF_F_E":
                road.setSPF_F_E((int) Double.parseDouble(value));
                break;
            case "SPF_B_E":
                road.setSPF_B_E((int) Double.parseDouble(value));
                break;
            case "LDF_F_E":
                road.setLDF_F_E((int) Double.parseDouble(value));
                break;
            case "LDF_B_E":
                set(road::setLDF_B_E, Parsers.INT.getParser(), value);
                break;
            case "LEVEL":
                set(road::setLEVEL, Parsers.INT.getParser(), value);
                break;
            case "LENGTH":
                set(road::setLength, (ToLongFunction<String>) Long::parseLong, value);
                break;
            case "the_geom":
                // do nothing
                break;
            default:
                log.warn("PROPERTY[{},{}]: {} with value {} skipped", road.getFeatureId(), road.getId(), key, value);
                break;
        }
    }

    public void set(IntConsumer setter, ToIntFunction<String> converter, String value) {
        setter.accept(
                converter.applyAsInt(value)
        );
    }

    public void set(LongConsumer setter, ToLongFunction<String> converter, String value) {
        setter.accept(
                converter.applyAsLong(value)
        );
    }

    public void set(DoubleConsumer setter, ToDoubleFunction<String> converter, String value) {
        setter.accept(
                converter.applyAsDouble(value)
        );
    }


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class Parsers<P> {
        private static final Parsers<ToLongFunction<String>> LONG = new Parsers<>(Long::parseLong);
        private static final Parsers<ToIntFunction<String>> INT = new Parsers<>(value -> (int) Double.parseDouble(value));
        private static final Parsers<ToDoubleFunction<String>> DOUBLE = new Parsers<>(Double::parseDouble);

        private final P parser;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class Setters<C> {
        private static final Setters<LongConsumer> longSetter(LongConsumer consumer) {
            return new Setters<>(consumer);
        }

        private static final Setters<IntConsumer> intSetter(IntConsumer consumer) {
            return new Setters<>(consumer);
        }

        private static final Setters<DoubleConsumer> doubleSetter(DoubleConsumer consumer) {
            return new Setters<>(consumer);
        }

        private final C setter;
    }

    private Map<String, LongConsumer> longConsumers;

    private Node getOrCreate(String tazId) {
        Node node;
        if (nodeCache.containsKey(tazId)) {
            node = nodeCache.get(tazId);
        } else {
            node = Node.builder().tazId(tazId).build();
            nodeCache.put(tazId, node);
        }
        return node;
    }
}
