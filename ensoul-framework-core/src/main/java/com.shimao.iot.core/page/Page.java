package com.shimao.iot.core.page;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * 分页查询结果
 *
 * @author striver.cradle
 */
public interface Page<T> extends Iterable<T> {

    /**
     * 结果集
     */
    List<T> getContent();

    /**
     * 总数（如果不做count查询，则为null）
     */
    Long getTotal();

    /**
     * 总页数（如果不做count查询，则如果结果集大小不等于每页数量，就返回当前页，反之，返回null）
     */
    @Nullable
    default Integer getLastPageNumber() {
        if (getTotal() == null) {
            return getContent().size() != current().getPageSize() ? current().getPageNumber() : null;
        }
        return PageUtil.getLastPageNumber(getTotal(), current().getPageSize());
    }

    /**
     * 当前页是否为第一页
     */
    default boolean isFirst() {
        return current().getPageNumber() == current().getFirstPageNumber();
    }

    /**
     * 当前页是否为最后一页（如果不做count查询，则只要结果集大小不等于每页数量，就返回false）
     */
    default boolean isLast() {
        if (getLastPageNumber() == null) {
            return getContent().size() != current().getPageSize();
        }
        return current().getPageNumber() == getLastPageNumber();
    }

    /**
     * 当前页是否有上一页
     */
    default boolean hasPrevious() {
        return current().getPageNumber() > current().getFirstPageNumber();
    }

    /**
     * 当前页是否有下一页（如果不做count查询，则只要结果集大小等于每页数量，就返回true）
     */
    default boolean hasNext() {
        if (getLastPageNumber() == null) {
            return getContent().size() == current().getPageSize();
        }
        return current().getPageNumber() < getLastPageNumber();
    }

    /**
     * 第一页
     */
    @NonNull
    default Pageable first() {
        return current().jumpTo(current().getFirstPageNumber());
    }

    /**
     * 上一页
     */
    @NonNull
    default Pageable previous() {
        return hasPrevious() ? current().jumpTo(current().getPageNumber() - 1) : first();
    }

    /**
     * 当前页
     */
    @NonNull
    Pageable current();

    /**
     * 下一页
     * 1.如果有下一页，则返回下一页
     * 2.如果没有最后一页，则始终跳至下一页
     * <p>
     * 注：若无count查询，则等到结果集数量小于每页数量，一定会产生最后一页
     */
    @NonNull
    default Pageable next() {
        if (hasNext()) {
            return current().jumpTo(current().getPageNumber() + 1);
        }
        Pageable last = last();
        return last == null ? current().jumpTo(current().getPageNumber() + 1) : last;
    }

    /**
     * 最后一页（
     * <p>
     * 1.如果没有下一页，则当前页就是最后一页
     * 2.如果不做count查询，则为null
     * <p>
     * 注：若无count查询，则等到结果集数量小于每页数量，一定会产生最后一页
     */
    default Pageable last() {
        if (!hasNext()) {
            return current().copy();
        }
        return getLastPageNumber() == null ? null : current().jumpTo(getLastPageNumber());
    }

    /**
     * Returns a new {@link Page} with the content of the current one mapped by the given {@link Function}.
     *
     * @param mapper must not be {@literal null}.
     */
    <S> Page<S> map(Function<? super T, ? extends S> mapper);

    <S> Page<S> mapAll(Function<List<T>, List<S>> mapper);

    @NonNull
    @Override
    default Iterator<T> iterator() {
        if (getContent() == null) {
            return Collections.emptyIterator();
        }
        return getContent().iterator();
    }
}
