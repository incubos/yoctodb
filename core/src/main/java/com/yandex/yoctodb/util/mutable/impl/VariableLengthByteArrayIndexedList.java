/*
 * (C) YANDEX LLC, 2014-2015
 *
 * The Source Code called "YoctoDB" available at
 * https://github.com/yandex/yoctodb is subject to the terms of the
 * Mozilla Public License, v. 2.0 (hereinafter referred to as the "License").
 *
 * A copy of the License is also available at http://mozilla.org/MPL/2.0/.
 */

package com.yandex.yoctodb.util.mutable.impl;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.yandex.yoctodb.util.OutputStreamWritable;
import com.yandex.yoctodb.util.UnsignedByteArray;
import com.yandex.yoctodb.util.mutable.ByteArrayIndexedList;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link ByteArrayIndexedList} with variable sized elements
 *
 * @author incubos
 */
@NotThreadSafe
public final class VariableLengthByteArrayIndexedList
        implements ByteArrayIndexedList {
    private final List<UnsignedByteArray> elements =
            new LinkedList<UnsignedByteArray>();
    private long elementSize = 0;

    @Override
    public void add(
            @NotNull
            final UnsignedByteArray e) {
        elements.add(e);
        elementSize += e.length();
    }

    @Override
    public long getSizeInBytes() {
        return 4L + // Element count
               8L * (elements.size() + 1L) + // Element offsets
               elementSize; // Element array size
    }

    @Override
    public void writeTo(
            @NotNull
            final OutputStream os) throws IOException {
        // Element count
        os.write(Ints.toByteArray(elements.size()));

        // Element offsets
        long elementOffset = 0;
        for (OutputStreamWritable e : elements) {
            os.write(Longs.toByteArray(elementOffset));
            elementOffset += e.getSizeInBytes();
        }
        os.write(Longs.toByteArray(elementOffset));

        // Elements
        for (OutputStreamWritable e : elements)
            e.writeTo(os);
    }

    @Override
    public String toString() {
        return "VariableLengthByteArrayIndexedList{" +
               "elementsCount=" + elements.size() +
               '}';
    }
}
