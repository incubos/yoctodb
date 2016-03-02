/*
 * (C) YANDEX LLC, 2014-2016
 *
 * The Source Code called "YoctoDB" available at
 * https://github.com/yandex/yoctodb is subject to the terms of the
 * Mozilla Public License, v. 2.0 (hereinafter referred to as the "License").
 *
 * A copy of the License is also available at http://mozilla.org/MPL/2.0/.
 */

package com.yandex.yoctodb.query.simple;

import com.yandex.yoctodb.immutable.FilterableIndexProvider;
import com.yandex.yoctodb.query.Condition;
import com.yandex.yoctodb.util.mutable.ArrayBitSet;
import com.yandex.yoctodb.util.mutable.ArrayBitSetPool;
import com.yandex.yoctodb.util.mutable.BitSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * {@code AND} condition
 *
 * @author incubos
 */
@Immutable
public final class SimpleAndCondition implements Condition {
    @NotNull
    private final Collection<Condition> clauses;

    public SimpleAndCondition(
            @NotNull
            final Collection<Condition> conditions) {
        if (conditions.isEmpty())
            throw new IllegalArgumentException("No conditions");

        this.clauses = new ArrayList<Condition>(conditions);
    }

    @Override
    public boolean set(
            @NotNull
            final FilterableIndexProvider indexProvider,
            @NotNull
            final BitSet to,
            @NotNull
            final ArrayBitSetPool bitSetPool) {
        if (clauses.size() == 1) {
            final Condition c = clauses.iterator().next();
            return c.set(indexProvider, to, bitSetPool);
        } else {
            assert !clauses.isEmpty();

            final ArrayBitSet result = bitSetPool.borrowSet(to.getSize());
            result.set();
            final ArrayBitSet clauseResult = bitSetPool.borrowSet(to.getSize());
            try {
                final Iterator<Condition> iter = clauses.iterator();
                while (iter.hasNext()) {
                    if (!iter.next().set(
                            indexProvider,
                            clauseResult,
                            bitSetPool)) {
                        return false;
                    } else if (!result.and(clauseResult)) {
                        return false;
                    }
                    if (iter.hasNext())
                        clauseResult.clear();
                }

                return to.or(result);
            } finally {
                bitSetPool.returnSet(clauseResult);
                bitSetPool.returnSet(result);
            }
        }
    }
}
