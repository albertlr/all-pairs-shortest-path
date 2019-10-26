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
package ro.albertlr.graph.algorithm;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ro.albertlr.graph.Import;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

@Setter(AccessLevel.PROTECTED)
public abstract class BaseEdge<V extends Vertex<V>, W extends Number> implements Edge<V, W> {

    private Supplier<V> fromSupplier;
    private Consumer<V> fromConsumer;
    private Supplier<V> toSupplier;
    private Consumer<V> toConsumer;

    @Override
    public V getFrom() {
        return fromSupplier.get();
    }

    @Override
    public void setFrom(V from) {
        fromConsumer.accept(from);
    }

    @Override
    public V getTo() {
        return toSupplier.get();
    }

    @Override
    public void setTo(V to) {
        toConsumer.accept(to);
    }

    @Override
    public W getWeight() {
        String weightProperty = Import.properties.getProperty("algorithm.weight.property", "SP_B_M");
        return getter(weightProperty)
                .get();
    }

    @Override
    public void setWeight(W weight) {
        String weightProperty = Import.properties.getProperty("algorithm.weight.property", "SP_B_M");

        Class<?> type = type(weightProperty);

        if (int.class.equals(type)) {
            intSetter(weightProperty)
                    .accept(weight.intValue());
        } else if (long.class.equals(type)) {
            longSetter(weightProperty)
                    .accept(weight.longValue());
        } else if (double.class.equals(type)) {
            doubleSetter(weightProperty)
                    .accept(weight.doubleValue());
        } else {
            throw new IllegalArgumentException("Could not set weight for " + weightProperty);
        }
    }

    protected abstract Class<?> type(String property);

    protected abstract IntConsumer intSetter(final String property);

    protected abstract LongConsumer longSetter(final String property);

    protected abstract DoubleConsumer doubleSetter(final String property);

    protected abstract Supplier<W> getter(final String property);

}
