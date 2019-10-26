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
import lombok.Data;
import ro.albertlr.graph.algorithm.BaseEdge;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

@Data
public class Road extends BaseEdge<Node, Double> {
    private Node a;
    private Node b;

    private String featureId;
    private long id;

    private long length;

    private int SP_F;
    private int SP_B;
    private int SP_F_M;
    private double SP_B_M;
    private double CAP_F_M;
    private int CAP_B_M;
    private int LD_F_M;
    private int LD_B_M;
    private int SPF_F_M;
    private int SPF_B_M;
    private int LDF_F_M;
    private int LDF_B_M;
    private int SP_F_E;
    private int SP_B_E;
    private int CAP_F_E;
    private int CAP_B_E;
    private int LD_F_E;
    private int LD_B_E;
    private int SPF_F_E;
    private int SPF_B_E;
    private int LDF_F_E;
    private int LDF_B_E;
    private int LEVEL;

    @Builder
    private Road(Node a, Node b, String featureId, long id, long length, int SP_F, int SP_B, int SP_F_M, double SP_B_M, double CAP_F_M, int CAP_B_M, int LD_F_M, int LD_B_M, int SPF_F_M, int SPF_B_M, int LDF_F_M, int LDF_B_M, int SP_F_E, int SP_B_E, int CAP_F_E, int CAP_B_E, int LD_F_E, int LD_B_E, int SPF_F_E, int SPF_B_E, int LDF_F_E, int LDF_B_E, int LEVEL) {
        setFromSupplier(this::getA);
        setFromConsumer(this::setA);
        setToSupplier(this::getB);
        setToConsumer(this::setB);

        this.a = a;
        this.b = b;
        this.featureId = featureId;
        this.id = id;
        this.length = length;
        this.SP_F = SP_F;
        this.SP_B = SP_B;
        this.SP_F_M = SP_F_M;
        this.SP_B_M = SP_B_M;
        this.CAP_F_M = CAP_F_M;
        this.CAP_B_M = CAP_B_M;
        this.LD_F_M = LD_F_M;
        this.LD_B_M = LD_B_M;
        this.SPF_F_M = SPF_F_M;
        this.SPF_B_M = SPF_B_M;
        this.LDF_F_M = LDF_F_M;
        this.LDF_B_M = LDF_B_M;
        this.SP_F_E = SP_F_E;
        this.SP_B_E = SP_B_E;
        this.CAP_F_E = CAP_F_E;
        this.CAP_B_E = CAP_B_E;
        this.LD_F_E = LD_F_E;
        this.LD_B_E = LD_B_E;
        this.SPF_F_E = SPF_F_E;
        this.SPF_B_E = SPF_B_E;
        this.LDF_F_E = LDF_F_E;
        this.LDF_B_E = LDF_B_E;
        this.LEVEL = LEVEL;
    }

    @Override
    protected Class<?> type(String property) {
        switch (property) {
            case "length":
                return long.class;
            case "SP_B_M": // fall through;
            case "CAP_F_M":
                return double.class;
            case "SP_F": // fall through
            case "SP_B": // fall through
            case "SP_F_M": // fall through
            case "CAP_B_M": // fall through
            case "LD_F_M": // fall through
            case "LD_B_M": // fall through
            case "SPF_F_M": // fall through
            case "SPF_B_M": // fall through
            case "LDF_F_M": // fall through
            case "LDF_B_M": // fall through
            case "SP_F_E": // fall through
            case "SP_B_E": // fall through
            case "CAP_F_E": // fall through
            case "CAP_B_E": // fall through
            case "LD_F_E": // fall through
            case "LD_B_E": // fall through
            case "SPF_F_E": // fall through
            case "SPF_B_E": // fall through
            case "LDF_F_E": // fall through
            case "LDF_B_E": // fall through
            case "LEVEL":
                return int.class;
            default:
                throw new IllegalArgumentException("Unknown property " + property);
        }

    }

    @Override
    protected IntConsumer intSetter(final String property) {
        switch (property) {
            case "SP_F":
                return this::setSP_F;
            case "SP_B":
                return this::setSP_B;
            case "SP_F_M":
                return this::setSP_F_M;
            case "CAP_B_M":
                return this::setCAP_B_M;
            case "LD_F_M":
                return this::setLD_F_M;
            case "LD_B_M":
                return this::setLD_B_M;
            case "SPF_F_M":
                return this::setSPF_F_M;
            case "SPF_B_M":
                return this::setSPF_B_M;
            case "LDF_F_M":
                return this::setLDF_F_M;
            case "LDF_B_M":
                return this::setLDF_B_M;
            case "SP_F_E":
                return this::setSP_F_E;
            case "SP_B_E":
                return this::setSP_B_E;
            case "CAP_F_E":
                return this::setCAP_F_E;
            case "CAP_B_E":
                return this::setCAP_B_E;
            case "LD_F_E":
                return this::setLD_F_E;
            case "LD_B_E":
                return this::setLD_B_E;
            case "SPF_F_E":
                return this::setSPF_F_E;
            case "SPF_B_E":
                return this::setSPF_B_E;
            case "LDF_F_E":
                return this::setLDF_F_E;
            case "LDF_B_E":
                return this::setLDF_B_E;
            case "LEVEL":
                return this::setLEVEL;
            default:
                throw new IllegalArgumentException("Unknown property " + property);
        }
    }

    @Override
    protected LongConsumer longSetter(final String property) {
        switch (property) {
            case "length":
                return this::setLength;
            default:
                throw new IllegalArgumentException("Unknown property " + property);
        }
    }

    @Override
    protected DoubleConsumer doubleSetter(final String property) {
        switch (property) {
            case "SP_B_M":
                return this::setSP_B_M;
            case "CAP_F_M":
                return this::setCAP_F_M;
            default:
                throw new IllegalArgumentException("Unknown property " + property);
        }
    }

    @Override
    protected Supplier<Double> getter(final String property) {
        switch (property) {
            case "length":
                return cast(this::getLength);
            case "SP_F":
                return cast(this::getSP_F);
            case "SP_B":
                return cast(this::getSP_B);
            case "SP_F_M":
                return cast(this::getSP_F_M);
            case "SP_B_M":
                return this::getSP_B_M;
            case "CAP_F_M":
                return this::getCAP_F_M;
            case "CAP_B_M":
                return cast(this::getCAP_B_M);
            case "LD_F_M":
                return cast(this::getLD_F_M);
            case "LD_B_M":
                return cast(this::getLD_B_M);
            case "SPF_F_M":
                return cast(this::getSPF_F_M);
            case "SPF_B_M":
                return cast(this::getSPF_B_M);
            case "LDF_F_M":
                return cast(this::getLDF_F_M);
            case "LDF_B_M":
                return cast(this::getLDF_B_M);
            case "SP_F_E":
                return cast(this::getSP_F_E);
            case "SP_B_E":
                return cast(this::getSP_B_E);
            case "CAP_F_E":
                return cast(this::getCAP_F_E);
            case "CAP_B_E":
                return cast(this::getCAP_B_E);
            case "LD_F_E":
                return cast(this::getLD_F_E);
            case "LD_B_E":
                return cast(this::getLD_B_E);
            case "SPF_F_E":
                return cast(this::getSPF_F_E);
            case "SPF_B_E":
                return cast(this::getSPF_B_E);
            case "LDF_F_E":
                return cast(this::getLDF_F_E);
            case "LDF_B_E":
                return cast(this::getLDF_B_E);
            case "LEVEL":
                return cast(this::getLEVEL);
            default:
                throw new IllegalArgumentException("Unknown property " + property);
        }
    }

    private <N extends Number> Supplier<Double> cast(Supplier<N> numberSupplier) {
        return () -> numberSupplier.get().doubleValue();
    }
}
