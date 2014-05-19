/**
 * Copyright (C) 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.dataset.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dashbuilder.dataset.group.IntervalList;
import org.dashbuilder.dataset.index.stats.SizeEstimator;
import org.dashbuilder.dataset.index.visitor.DataSetIndexVisitor;
import org.dashbuilder.model.dataset.group.GroupColumn;
import org.dashbuilder.model.dataset.group.ScalarFunctionType;

/**
 * A DataSet index element
 */
public abstract class DataSetIndexElement {

    /**
     * Time (in nanoseconds) required to "build" (load, create, filter, ...) the indexed element.
     */
    long buildTime = 0;

    /**
     * Number of times the data set has been reused from an existing build.
     */
    int reuseHits = 0;

    DataSetIndexElement(long buildTime) {
        this.buildTime = buildTime;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public int getReuseHits() {
        return reuseHits;
    }

    public void reuseHit() {
        this.reuseHits++;
    }

    public long getReuseTime() {
        return buildTime*reuseHits;
    }

    public void acceptVisitor(DataSetIndexVisitor visitor) {
        visitor.visit(this);
    }

    public long getEstimatedSize() {
        return SizeEstimator.sizeOfLong + SizeEstimator.sizeOfInteger;
    }
}

