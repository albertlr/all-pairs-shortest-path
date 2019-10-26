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

public interface Vertex<V extends Vertex<V>> {
    /**
     * Coloring used by Breadth-first search
     */
    enum Color {
        /**
         * All vertices start out white and may later become gray and then black
         */
        WHITE,
        /**
         * Gray vertices may have some adjacent white vertices; they represent the frontier between discovered and undiscovered vertices.
         */
        GRAY,
        BLACK;
    }

    Color getColor();
    void setColor(Color color);

    V getPredecessor();
    void setPredecessor(V v);

    long getDistance();
    void setDistance(long distance);

    long getDiscoveryTime();
    void setDiscoveryTime(long discoveryTime);

    long getFinishingTime();
    void setFinishingTime(long finishingTime);

}
