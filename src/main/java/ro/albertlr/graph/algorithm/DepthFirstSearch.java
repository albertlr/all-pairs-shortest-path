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

import com.google.common.collect.Sets;
import lombok.Builder;
import org.jgrapht.Graph;
import ro.albertlr.graph.algorithm.Vertex.Color;

import java.util.Set;

public class DepthFirstSearch<V extends Vertex<V>, E> extends Search<V, E> {

    long time;

    @Builder
    protected DepthFirstSearch(Graph<V, E> graph) {
        super(graph);
    }

    public void search() {
        Set<V> vertices = Sets.newHashSet(graph.vertexSet());

        for (V u : vertices) {
            u.setColor(Color.WHITE);
            u.setPredecessor(null);
        }

        time = 0;

        for (V u : vertices) {
            if (Color.WHITE.equals(u.getColor())) {
                visit(u);
            }
        }
    }

    private void visit(V u) {
        u.setDiscoveryTime(++time); // white vertex u has just been discovered
        u.setColor(Color.GRAY);

        Set<V> adjacentOfU = getAdjacentOf(u);
        for (V v : adjacentOfU) { // explore edge (u,v)
            if (Color.WHITE.equals(v.getColor())) {
                v.setPredecessor(u);
                visit(v);
            }
        }

        u.setColor(Color.BLACK);
        u.setFinishingTime(++time);
    }
}
