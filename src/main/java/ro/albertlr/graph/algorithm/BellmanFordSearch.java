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

/**
 * the Bellman-Ford algorithm returns a boolean value indicating whether or not there is a negative-weight cycle that
 * is reachable from the source.
 *
 * @param <V> Vertices type. Must be a superclass of {@link Vertex}
 * @param <E> Edge type. Must be a superclass of {@link Edge}
 */
public class BellmanFordSearch<V extends Vertex<V>, E extends Edge<V, Double>> extends ShortestPathSearch<V, E> {
    public BellmanFordSearch(Graph<V, E> graph) {
        super(graph);
    }

    public boolean serach(V source) {
        initializeSingleSource(source);
        Set<E> edges = graph.edgeSet();
        for (int i = 1; i <= graph.vertexSet().size(); i++) {
            for (E edgeUtoV : edges) {
                relax(edgeUtoV);
            }
        }

        for (E edgeUtoV : edges) {
            V u = edgeUtoV.getFrom();
            V v = edgeUtoV.getTo();
            long w = edgeUtoV.getWeight().longValue();

            if (v.getDistance() > u.getDistance() + w) {
                return false;
            }
        }

        return true;
    }
}
