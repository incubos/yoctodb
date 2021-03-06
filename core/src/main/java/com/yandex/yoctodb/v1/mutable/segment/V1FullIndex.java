/*
 * (C) YANDEX LLC, 2014-2016
 *
 * The Source Code called "YoctoDB" available at
 * https://github.com/yandex/yoctodb is subject to the terms of the
 * Mozilla Public License, v. 2.0 (hereinafter referred to as the "License").
 *
 * A copy of the License is also available at http://mozilla.org/MPL/2.0/.
 */

package com.yandex.yoctodb.v1.mutable.segment;

import com.yandex.yoctodb.v1.V1DatabaseFormat;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

/**
 * Index supporting filtering and sorting by specific field
 *
 * @author incubos
 */
@NotThreadSafe
public final class V1FullIndex extends AbstractV1FullIndex {
    public V1FullIndex(
            @NotNull
            final String fieldName,
            final boolean fixedLength) {
        super(
                fieldName,
                fixedLength,
                fixedLength ?
                        V1DatabaseFormat.SegmentType.FIXED_LENGTH_FULL_INDEX :
                        V1DatabaseFormat.SegmentType.VARIABLE_LENGTH_FULL_INDEX);

    }
}
