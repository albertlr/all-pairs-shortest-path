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

import com.google.common.collect.Sets;
import lombok.Builder;
import org.jgrapht.Graph;
import ro.albertlr.graph.algorithm.Vertex.Color;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class BreadthFirstSearch<V extends Vertex<V>, E> extends Search<V, E> {

    @Builder
    protected BreadthFirstSearch(Graph<V, E> graph) {
        super(graph);
    }

    public void search(V source) {
        Set<V> vertices = Sets.newHashSet(graph.vertexSet());
        vertices.remove(source);

        for (V u : vertices) {
            u.setColor(Color.WHITE);
            u.setDistance(INFINITE);
            u.setPredecessor(null);
        }

        source.setColor(Color.GRAY);
        source.setDistance(0);
        source.setPredecessor(null);

        // keep the gray vertices
        Queue<V> queue = new LinkedList<>();
        queue.offer(source);

        while (!queue.isEmpty()) {
            V u = queue.poll();
            Set<V> adjacentOfU = getAdjacentOf(u);
            for (V v : adjacentOfU) {
                if (Color.WHITE.equals(v.getColor())) {
                    v.setColor(Color.GRAY);
                    v.setDistance(u.getDistance() + 1);
                    v.setPredecessor(u);

                    queue.offer(v);
                }
            }
            u.setColor(Color.BLACK);
        }
    }

}
