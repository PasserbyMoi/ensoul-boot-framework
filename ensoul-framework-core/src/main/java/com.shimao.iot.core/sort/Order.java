package com.shimao.iot.core.sort;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
 * {@link Sort}
 *
 * @author striver.cradle
 */
@Data
public class Order {
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    private static final boolean DEFAULT_IGNORE_CASE = false;

    private final Direction direction;
    private final String property;
    private final boolean ignoreCase;
    private final NullHandling nullHandling;

    public Order(String property) {
        this(DEFAULT_DIRECTION, property);
    }

    public Order(Direction direction, String property) {
        this(direction, property, DEFAULT_IGNORE_CASE, null);
    }

    public Order(Direction direction, String property, NullHandling nullHandling) {
        this(direction, property, DEFAULT_IGNORE_CASE, nullHandling);
    }

    /**
     * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
     * {@link Order#DEFAULT_DIRECTION}
     *
     * @param direction    can be {@literal null}, will default to {@link Order#DEFAULT_DIRECTION}
     * @param property     must not be {@literal null} or empty.
     * @param ignoreCase   true if sorting should be case insensitive. false if sorting should be case sensitive.
     * @param nullHandling can be {@literal null}, will default to {@link NullHandling#NATIVE}.
     */
    private Order(Direction direction, String property, boolean ignoreCase, NullHandling nullHandling) {
        if (StringUtils.isBlank(property)) {
            throw new IllegalArgumentException("Property must not null or empty!");
        }
        this.direction = direction == null ? DEFAULT_DIRECTION : direction;
        this.property = property;
        this.ignoreCase = ignoreCase;
        this.nullHandling = nullHandling == null ? NullHandling.NATIVE : nullHandling;
    }

    /**
     * Returns a new {@link Order} with case insensitive sorting enabled.
     */
    public Order ignoreCase() {
        return new Order(direction, property, true, nullHandling);
    }

    /**
     * Returns a {@link Order} with the given property.
     */
    public Order with(String property) {
        if (StringUtils.isNotBlank(property)) {
            return new Order(direction, property, ignoreCase, nullHandling);
        }
        return this;
    }

    /**
     * Returns a {@link Order} with the given {@link NullHandling}.
     *
     * @param nullHandling can be {@literal null}.
     */
    public Order with(NullHandling nullHandling) {
        return new Order(direction, this.property, ignoreCase, nullHandling);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NULLS_FIRST} as null handling hint.
     */
    public Order nullsFirst() {
        return with(NullHandling.NULLS_FIRST);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NULLS_LAST} as null handling hint.
     */
    public Order nullsLast() {
        return with(NullHandling.NULLS_LAST);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NATIVE} as null handling hint.
     */
    public Order nullsNative() {
        return with(NullHandling.NATIVE);
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     */
    public boolean isAscending() {
        return this.direction == Direction.ASC;
    }

    /**
     * Enumeration for null handling hints that can be used in {@link Order} expressions.
     *
     * @author Thomas Darimont
     */
    public enum NullHandling {

        /**
         * Lets the data store decide what to do with nulls.
         */
        NATIVE,

        /**
         * A hint to the used data store to order entries with null values before non null entries.
         */
        NULLS_FIRST,

        /**
         * A hint to the used data store to order entries with null values after non null entries.
         */
        NULLS_LAST
    }

    public String toQueryString() {
        return this.property + "," + this.direction.toString().toLowerCase();
    }

    org.springframework.data.domain.Sort.Order toSpringOrder() {
        org.springframework.data.domain.Sort.Direction direction =
                org.springframework.data.domain.Sort.Direction.fromString(this.direction.name());
        return new org.springframework.data.domain.Sort.Order(direction, this.property);
    }
}
