package com.shimao.iot.core.utils;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;
import static org.javatuples.Triplet.with;

/**
 * @author striver.cradle
 */
public class ListUtil {

    public static <T> List<T> intersect(List<T> left, List<T> right) {
        return differ(left, right, Objects::equals).getValue1().stream().map(Pair::getValue0).collect(toList());
    }

    public static <P, Q> Triplet<List<P>, List<Pair<P, Q>>, List<Q>> differ(List<P> pList, List<Q> qList) {
        return differ(pList, qList, Objects::equals);
    }

    public static <P, Q> Triplet<List<P>, List<Pair<P, Q>>, List<Q>> differ(List<P> pList, List<Q> qList,
                                                                            BiPredicate<P, Q> equator) {
        List<P> pOnly = pList.stream()
                .filter(p -> !contains(qList, p, (q, p1) -> equator.test(p1, q)))
                .collect(toList());

        List<Pair<P, Q>> both = pList.stream()
                .map(p -> containsGet(qList, p, (q1, p1) -> equator.test(p1, q1)).map(q -> Pair.with(p, q)).orElse(null))
                .filter(Objects::nonNull)
                .collect(toList());

        List<Q> qOnly = qList.stream()
                .filter(q -> !contains(pList, q, equator))
                .collect(toList());

        return with(pOnly, both, qOnly);
    }

    public static <T, U> boolean contains(List<T> list, U u, BiPredicate<T, U> equator) {
        return list.stream().anyMatch(t -> equator.test(t, u));
    }

    public static <T, U> Optional<T> containsGet(List<T> list, U u, BiPredicate<T, U> equator) {
        return list.stream().filter(t -> equator.test(t, u)).findAny();
    }

    /**
     * 使用方法：
     * <pre>
     *     List<Order> orders = new ArrayList<>();
     *     orders.add(...);
     *     ...
     *     orders.stream().filter(distinctByKey(Order::getOrderNo)).collect(toList());
     * </pre>
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keySelector) {
        Map<Object, Boolean> seen = new HashMap<>(16);
        return t -> seen.putIfAbsent(keySelector.apply(t), TRUE) == null;
    }

    /**
     * 并集
     */
    @SafeVarargs
    public static <T> List<T> union(List<T> list, List<T>... moreLists) {
        if (moreLists.length == 0) {
            return new ArrayList<>(list);
        }
        int size = list.size() + Stream.of(moreLists).mapToInt(List::size).sum();
        List<T> result = new ArrayList<>(5 + size + (size / 10));
        result.addAll(list);
        Stream.of(moreLists).forEach(result::addAll);
        return result;
    }

    /**
     * 添加元素到集合
     */
    @SafeVarargs
    public static <T> List<T> add(List<T> list, T... moreElements) {
        if (moreElements.length == 0) {
            return new ArrayList<>(list);
        }
        int size = list.size() + moreElements.length;
        List<T> result = new ArrayList<>(5 + size + (size / 10));
        result.addAll(list);
        result.addAll(Arrays.asList(moreElements));
        return result;
    }

    /**
     * 集合转化成数组
     */
    public static String[] toStringArray(List<String> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new String[0]);
    }

    /**
     * 判断集合是否为单元素集合
     */
    public static <T> boolean isSingle(List<T> list) {
        return list != null && list.size() == 1;
    }
}
