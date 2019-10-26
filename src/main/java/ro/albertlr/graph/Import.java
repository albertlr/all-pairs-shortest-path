package ro.albertlr.graph;

/*-
 * #%L
 * All-Pairs Shortest Path
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

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;
import ro.albertlr.graph.geotools.ReadShapefile;
import ro.albertlr.graph.algorithm.BreadthFirstSearch;
import ro.albertlr.graph.algorithm.DepthFirstSearch;
import ro.albertlr.graph.algorithm.Paths;
import ro.albertlr.graph.utils.RProperties;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class Import {

    public static RProperties properties;

    public static void main(String[] args) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Import.properties = RProperties.load();

        properties.keySet()
                .forEach((key) -> log.info("{} -> {}", key, properties.getProperty(key)));

        log.info("properties loaded in {}", stopwatch);

        stopwatch.reset().start();
        Graph<Node, Road> roadNetwork;
        try {
            roadNetwork = ReadShapefile.importFile(properties.getProperty("input.shapes"));
        } finally {
            log.info("road network loaded in {}", stopwatch);
        }

        stopwatch.reset().start();
        BreadthFirstSearch<Node, Road> bfsSearch = BreadthFirstSearch.<Node, Road>builder()
                .graph(roadNetwork)
                .build();

        Node source = getVertex(roadNetwork, "1");

        bfsSearch.search(source);

        log.info("breadth first search computed from node {} in {}", source, stopwatch);

        stopwatch.reset().start();

        Collection<Node> path = Paths.<Node, Road>builder()
                .graph(roadNetwork)
                .build()
                .path(source, getVertex(roadNetwork, "261"));

        log.info(":: BFS :: Path from {} to {} is: [{}]", 1, 261, path.stream().map(Node::getTazId).collect(Collectors.joining(" -> ")));

        stopwatch.reset().start();
        DepthFirstSearch<Node, Road> dfsSearch = DepthFirstSearch.<Node, Road>builder()
                .graph(roadNetwork)
                .build();

        source = getVertex(roadNetwork, "1");

        dfsSearch.search();

        log.info("depth first search computed from node {} in {}", source, stopwatch);

        stopwatch.reset().start();

        Node vertex261 = getVertex(roadNetwork, "261");
        path = Paths.<Node, Road>builder()
                .graph(roadNetwork)
                .build()
                .path(source, vertex261);

        log.info(":: DFS :: Path from {} to {} is: [{}]", 1, 261, path.stream().map(Node::getTazId).collect(Collectors.joining(" -> ")));

        stopwatch.reset().start();

        DijkstraShortestPath<Node, Road> dijkstraAlg = new DijkstraShortestPath<>(roadNetwork);
        SingleSourcePaths<Node, Road> iPaths = dijkstraAlg.getPaths(source);
        log.info(":: Dijkstra :: Path from {} to {} is : [{}]", 1, 261,
                iPaths.getPath(vertex261).getVertexList().stream()
                .map(Node::getTazId)
                .collect(Collectors.joining(" -> "))
        );
        log.info("Builtin Dijkstra export done in {}", stopwatch);

//        graphVizExport(roadNetwork);
        log.info("GraphViz export done in {}", stopwatch);
    }

    private static Node getVertex(Graph<Node, Road> graph, String vertexId) {
        for (Node v : graph.vertexSet()) {
            if (vertexId.equals(v.getTazId())) {
                return v;
            }
        }
        return null;
    }

    public static void graphVizExport(Graph<Node, Road> graph) {
        // use helper classes to define how vertices should be rendered,
        // adhering to the DOT language restrictions
        ComponentNameProvider<Node> vertexIdProvider = node -> String.valueOf(node.getTazId());
        ComponentNameProvider<Node> vertexLabelProvider = node ->
                String.valueOf(node.getTazId()) + ":" +
                        (Long.MAX_VALUE == node.getDistance() ? "∞" : String.valueOf(node.getDistance()));
        ComponentNameProvider<Road> roadLabelProvider = road -> "" + road.getId() + ':' + road.getLEVEL();
        GraphExporter<Node, Road> exporter =
                new DOTExporter<>(
                        vertexIdProvider,
                        vertexLabelProvider,
                        null //roadLabelProvider
                );
        try {
            Writer writer = new FileWriter("roads-distance.dot");
            exporter.exportGraph(graph, writer);
        } catch (ExportException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
