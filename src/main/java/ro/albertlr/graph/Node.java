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
package ro.albertlr.graph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ro.albertlr.graph.algorithm.BaseVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"roadsFrom", "roadsTo"})
@ToString(exclude = {"roadsFrom", "roadsTo"})
@Builder
public class Node extends BaseVertex<Node> {
    /**
     * TAZ (Traffic Assignment Zone ID)
     */
    private final String tazId;

    private final Collection<Road> roadsFrom = new ArrayList<>();
    private final Collection<Road> roadsTo = new ArrayList<>();

    public String toFullString() {
        final String from = roadsFrom.stream()
                .map(Road::getA)
                .filter(Objects::nonNull)
                .map(Node::getTazId)
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        final String to = roadsFrom.stream()
                .map(Road::getB)
                .filter(Objects::nonNull)
                .map(Node::getTazId)
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        return "Node(taz=" + tazId + ", roadsFrom=[" + from + "], roadsTo=[" + to + "])";
    }
}
