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

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class Paths<V extends Vertex<V>, E> extends Algorithm<V, E> {

    @Builder
    protected Paths(Graph<V, E> graph) {
        super(graph);
    }

    public void print(V source, V destination) {
        if (source == destination) {
            log.info("{}", source);
        } else if (destination.getPredecessor() == null) {
            log.info("no path from {} to {} exists", source, destination);
        } else {
            print(source, destination.getPredecessor());
            log.info("{}", destination);
        }
    }

    /**
     * Returns the path from source to destination
     */
    public Collection<V> path(V source, V destination) {
        Collection<V> path = new ArrayList<>();
        computePath(source, destination, path);
        return path;
    }

    private void computePath(V source, V destination, Collection<V> path) {
        if (source == destination) {
            path.add(source);
        } else if (destination.getPredecessor() == null) {
            log.trace("no path from {} to {} exists", source, destination);
        } else {
            computePath(source, destination.getPredecessor(), path);
            path.add(destination);
        }
    }
}
