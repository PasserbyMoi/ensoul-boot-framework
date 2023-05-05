package com.shimao.iot.core.sort;

import com.shimao.iot.core.page.ResortStrategy;
import com.shimao.iot.core.utils.ListUtil;
import lombok.ToString;
import lombok.val;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author striver.cradle
 */
@ToString
public class Sort implements Iterable<Order> {

    private final List<Order> orders;

    public Sort(Order... orders) {
        this(Arrays.asList(orders));
    }

    public Sort(List<Order> orders) {
        this(orders, true);
    }

    public Sort(String... properties) {
        this(Order.DEFAULT_DIRECTION, properties);
    }

    public Sort(Direction direction, String... properties) {
        this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
    }

    public Sort(Direction direction, List<String> properties) {
        this(_buildOrders(direction, properties), true);
    }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param orders 多个排序规则
     * @param clean  是否清理错误的规则
     */
    private Sort(List<Order> orders, boolean clean) {
        this.orders = clean ? _clean(orders) : orders;
    }

    /**
     * Returns a new {@link Sort} consisting of the {@link Order}s of the current {@link Sort} combined with the given
     * ones.
     */
    public Sort and(@Nullable Sort sort) {
        if (sort == null) {
            return this;
        }
        ArrayList<Order> these = new ArrayList<>(this.orders);
        for (Order order : sort) {
            these.add(order);
        }
        return new Sort(these);
    }

    /**
     * Returns the order registered for the given property.
     */
    public Optional<Order> findOrderFor(String property) {
        for (Order order : this) {
            if (order.getProperty().equals(property)) {
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    @Override
    @NonNull
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    public List<String> toQueryStrings() {
        return this.orders.stream().map(Order::toQueryString).collect(toList());
    }

    /**
     * 全局重排序
     */
    public Sort resort(ResortStrategy strategy) {
        Objects.requireNonNull(strategy);
        val orders = this.orders.stream().map(strategy::transfer).collect(toList());
        return new Sort(orders, true);
    }

    /**
     * 修改指定属性的排序规则
     */
    public Sort resort(String property, String... newProperties) {
        return resort(property, order -> new Sort(order.getDirection(), newProperties));
    }

    /**
     * 修改指定属性的排序规则
     */
    public Sort resort(String property, Function<Order, Sort> resortHandler) {
        Objects.requireNonNull(property);
        Objects.requireNonNull(resortHandler);
        return removeOrderFor(property).map(order -> and(resortHandler.apply(order))).orElse(this);
    }

    public Optional<Order> removeOrderFor(String property) {
        for (int i = 0; i < orders.size(); i++) {
            final Order order = orders.get(i);
            if (order.getProperty().equals(property)) {
                orders.remove(i);
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    public org.springframework.data.domain.Sort toSpringSort() {
        return org.springframework.data.domain.Sort.by(this.orders.stream().map(Order::toSpringOrder).collect(toList()));
    }

    /*-------------------------------私有方法-------------------------------*/

    /**
     * 清理错误的排序规则，比如a,asc&&a,desc，只保留a,asc
     */
    private List<Order> _clean(List<Order> orders) {
        return orders.stream().filter(ListUtil.distinctByKey(Order::getProperty)).collect(toList());
    }

    private static List<Order> _buildOrders(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
        List<Order> orders = new ArrayList<>(properties.size());
        for (String property : properties) {
            orders.add(new Order(direction, property));
        }
        return orders;
    }
}
