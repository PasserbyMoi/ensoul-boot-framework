package com.shimao.iot.core.page;

import com.shimao.iot.core.sort.Order;
import com.shimao.iot.core.sort.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract interface for pagination information.
 *
 * @author striver.cradle
 */
public interface Pageable {

    /**
     * Returns the page to be returned.
     *
     * @return the page to be returned.
     */
    int getPageNumber();

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page
     */
    int getPageSize();

    /**
     * Returns the offset to be taken according to the underlying page and page size.
     *
     * @return the offset to be taken
     */
    default int getOffset() {
        return PageUtil.getOffset(getFirstPageNumber(), getPageNumber(), getPageSize());
    }

    /**
     * Returns the sorting parameters.
     */
    @Nullable
    default Sort getSort() {
        return getSort(false);
    }

    /**
     * Returns the sorting parameters.
     */
    @Nullable
    Sort getSort(boolean applyResortStrategy);

    /**
     * 是否需要查询总记录数
     */
    boolean needTotal();

    /**
     * 是否需要查询记录列表
     */
    boolean needContent();

    /**
     * 是否纠正分页边界错误，比如当page<1时，自动设置page=1
     * <p>
     * 不合法的分页参数情况（如果fixEdge=false，则前3个应抛异常）：
     * 1.page < first_page_number
     * 2.size < 1
     * 3.size > max_page_size
     * 4.page > last_page_number（做count查询后才能发现）
     */
    boolean isFixEdge();

    default Pageable fixEdge(Long total) {
        if (isFixEdge() && total != null && getOffset() > total) {
            return jumpTo(PageUtil.getLastPageNumber(total, getPageSize()));
        }
        return this;
    }

    /**
     * 分页冗余选项
     */
    @NonNull
    PageableVerbose verbose();

    /**
     * 全局重排序策略
     */
    ResortStrategy resortStrategy();

    /**
     * 页码是否从1开始
     */
    boolean isPageNumberOneIndexed();

    default int getFirstPageNumber() {
        return PageUtil.getFirstPageNumber(isPageNumberOneIndexed());
    }

    @NonNull
    Pageable jumpTo(int page);

    @NonNull
    default Pageable copy() {
        return jumpTo(getPageNumber());
    }

    @NonNull
    default Pageable enable(PageConfigKey key) {
        return config(key, true);
    }

    @NonNull
    default Pageable disabled(PageConfigKey key) {
        return config(key, false);
    }

    @NonNull
    Pageable config(PageConfigKey key, boolean enabled);

    @NonNull
    Pageable config(@Nullable PageableVerbose verbose);

    @NonNull
    Pageable config(@Nullable ResortStrategy strategy);

    @NonNull
    default org.springframework.data.domain.Pageable toSpringPageable() {
        Sort sort = getSort();
        if (sort == null) {
            if (isPageNumberOneIndexed()) {
                return org.springframework.data.domain.PageRequest.of(getPageNumber() - 1, getPageSize());
            } else {
                return org.springframework.data.domain.PageRequest.of(getPageNumber(), getPageSize());
            }
        } else {
            if (isPageNumberOneIndexed()) {
                return org.springframework.data.domain.PageRequest.of(getPageNumber() - 1, getPageSize(),
                        sort.toSpringSort());
            } else {
                return org.springframework.data.domain.PageRequest.of(getPageNumber(), getPageSize(),
                        sort.toSpringSort());
            }
        }
    }

    default List<String> toSortQueryStrings() {
        Sort sort = getSort();
        if (sort == null) {
            return Collections.emptyList();
        }
        return sort.toQueryStrings();
    }

    @Deprecated
    default Map<String, Object> toQueryMap() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("pageSize", getPageSize());
        map.put("pageNum", getPageNumber());
        map.put("sort", toSortQueryStrings());
        map.put("needTotal", needTotal());
        map.put("needContent", needContent());
        map.put("verbose", verbose());
        map.put("fixEdge", isFixEdge());
        return map;
    }

    /**
     * 手动重排序，将指定属性重命名
     *
     * @param property      待替换的排序规则的属性名
     * @param newProperties 获取重排序后的新规则
     */
    void resort(String property, String... newProperties);

    /**
     * 手动重排序，将指定属性的排序规则替换成新的排序规则
     *
     * @param property      待替换的排序规则的属性名
     * @param resortHandler 获取重排序后的新规则
     */
    void resort(String property, Function<Order, Sort> resortHandler);
}
