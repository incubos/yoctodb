/*
 * (C) YANDEX LLC, 2014-2016
 *
 * The Source Code called "YoctoDB" available at
 * https://github.com/yandex/yoctodb is subject to the terms of the
 * Mozilla Public License, v. 2.0 (hereinafter referred to as the "License").
 *
 * A copy of the License is also available at http://mozilla.org/MPL/2.0/.
 */

package com.yandex.yoctodb.v1.immutable;

import com.yandex.yoctodb.util.mutable.BitSet;
import org.jetbrains.annotations.NotNull;

/**
 * Pair of database and its document bitset
 *
 * @author incubos
 */
public final class DatabaseDocs {
    @NotNull
    public final V1QueryContext ctx;
    @NotNull
    public final BitSet docs;

    public DatabaseDocs(
            @NotNull
            final V1QueryContext ctx,
            @NotNull
            final BitSet docs) {
        assert ctx.getDatabase().getDocumentCount() == docs.getSize();
        assert !docs.isEmpty();

        this.ctx = ctx;
        this.docs = docs;
    }
}
