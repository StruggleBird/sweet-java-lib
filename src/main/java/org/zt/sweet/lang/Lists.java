package org.zt.sweet.lang;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;

import org.zt.sweet.utils.Assert;

/**
 * Static utility methods pertaining to {@link List} instances. Also see this class's counterparts
 * {@link Sets}, {@link Maps} and {@link Queues}.
 *
 * <p>
 * See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/CollectionUtilitiesExplained#Lists" >
 * {@code Lists}</a>.
 *
 * @author Kevin Bourrillion
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library)
 */
public final class Lists {
    private Lists() {}

    // ArrayList

    /**
     * Creates a <i>mutable</i>, empty {@code ArrayList} instance (for Java 6 and earlier).
     *
     * <p>
     * <b>Note:</b> if mutability is not required, use {@link ImmutableList#of()} instead.
     *
     * <p>
     * <b>Note for Java 7 and later:</b> this method is now unnecessary and should be treated as
     * deprecated. Instead, use the {@code ArrayList} {@linkplain ArrayList#ArrayList() constructor}
     * directly, taking advantage of the new <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>.
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements.
     *
     * <p>
     * <b>Note:</b> essentially the only reason to use this method is when you will need to add or
     * remove elements later. Otherwise, for non-null elements use {@link ImmutableList#of()} (for
     * varargs) or {@link ImmutableList#copyOf(Object[])} (for an array) instead. If any elements
     * might be null, or you need support for {@link List#set(int, Object)}, use
     * {@link Arrays#asList}.
     *
     * <p>
     * Note that even when you do need the ability to add or remove, this method provides only a
     * tiny bit of syntactic sugar for {@code newArrayList(} {@link Arrays#asList asList}
     * {@code (...))}, or for creating an empty list then calling {@link Collections#addAll}. This
     * method is not actually very useful and will likely be deprecated in the future.
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        Assert.notNull(elements); // for GWT
        // Avoid integer overflow when a large array is passed in
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    static int computeArrayListCapacity(int arraySize) {

        return (int) Math.ceil(arraySize / 0.7);
    }


}
