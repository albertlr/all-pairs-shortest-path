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
import java.util.stream.Collectors;

public abstract class Search<V extends Vertex<V>, E> extends Algorithm<V, E> {
    protected Search(Graph<V, E> graph) {
        super(graph);
    }

    protected Set<V> getAdjacentOf(V u) {
        return graph.outgoingEdgesOf(u)
                .stream()
                .map(e -> graph.getEdgeTarget(e))
                .collect(Collectors.toSet());
    }

}
