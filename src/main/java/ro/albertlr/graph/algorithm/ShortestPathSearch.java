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
package ro.albertlr.graph.algorithm;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.function.BiFunction;

public class ShortestPathSearch<V extends Vertex<V>, E extends Edge<V, Double>> extends Search<V, E> {

    public ShortestPathSearch(Graph<V, E> graph) {
        super(graph);
    }

    protected void initializeSingleSource(V source) {
        Set<V> vertices = graph.vertexSet();
        for (V v : vertices) {
            v.setDistance(INFINITE);
            v.setPredecessor(null);
        }

        source.setDistance(0L);
    }

    protected void relax(E edgeUtoV) {
        V u = edgeUtoV.getFrom();
        V v = edgeUtoV.getTo();
        long w = edgeUtoV.getWeight().longValue();
        if (v.getDistance() > u.getDistance() + w) {
            v.setDistance(u.getDistance() + w);
            v.setPredecessor(u);
        }
    }
}
