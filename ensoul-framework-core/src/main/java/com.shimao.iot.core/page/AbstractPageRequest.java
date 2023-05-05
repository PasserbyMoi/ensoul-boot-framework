package com.shimao.iot.core.page;

import com.shimao.iot.core.sort.Order;
import com.shimao.iot.core.sort.Sort;
import com.shimao.iot.core.sort.SortDefaults;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.function.Function;

/**
 * @author striver.cradle
 */
@ToString
@EqualsAndHashCode
public abstract class AbstractPageRequest implements Pageable {

    private final int page;
    private final int size;

    @Getter
    private final int defaultPageSize;
    @Getter
    private final int maxPageSize;

    private final boolean needTotal;
    private final boolean needContent;

    @Getter
    private final boolean fixEdge;
    private final PageableVerbose verbose;
    @Getter
    private final boolean pageNumberOneIndexed;
    private final ResortStrategy resortStrategy;

    private Sort sort;

    public AbstractPageRequest(int page, int size) {
        this(page, size,
                PageDefaults.PAGE_SIZE,
                PageDefaults.MAX_PAGE_SIZE,
                PageDefaults.NEED_TOTAL,
                PageDefaults.NEED_CONTENT,
                PageDefaults.IS_FIX_EDGE,
                PageDefaults.PAGEABLE_VERBOSE,
                PageDefaults.PAGE_NUMBER_ONE_INDEXED,
                SortDefaults.RESORT_STRATEGY
        );
    }

    /**
     * Creates a new {@link AbstractPageRequest}.
     *
     * @param page                 页码
     * @param size                 每页数量
     * @param needTotal            是否需要查询总记录数
     * @param needContent          是否需要查询记录列表
     * @param fixEdge              是否纠正分页边界错误，比如当page<起始页时，自动设置page=起始页
     * @param verbose              返回冗余数据范围
     * @param pageNumberOneIndexed 页码是否从1开始
     * @param resortStrategy       全局重排序策略
     * @param orders               排序规则
     */
    public AbstractPageRequest(final int page,
                               final int size,
                               final int defaultPageSize,
                               final int maxPageSize,
                               final boolean needTotal,
                               final boolean needContent,
                               final boolean fixEdge,
                               final PageableVerbose verbose,
                               final boolean pageNumberOneIndexed,
                               final ResortStrategy resortStrategy,
                               final Order... orders) {
        this.defaultPageSize = defaultPageSize;
        this.maxPageSize = maxPageSize;
        this.needTotal = needTotal;
        this.needContent = needContent;
        this.verbose = verbose == null ? PageDefaults.PAGEABLE_VERBOSE : verbose;
        this.fixEdge = fixEdge;
        this.pageNumberOneIndexed = pageNumberOneIndexed;
        this.resortStrategy = resortStrategy == null ? SortDefaults.RESORT_STRATEGY : resortStrategy;
        this.size = size < 1 ? defaultPageSize : Math.min(size, maxPageSize);
        if (fixEdge) {
            this.page = Math.max(page, getFirstPageNumber());
        } else {
            this.page = page;
        }
        if (this.page < getFirstPageNumber()) {
            throw new PageRequestException("page number must not be less than " + getFirstPageNumber());
        }
        if (orders.length > 0) {
            this.sort = new Sort(orders);
        }
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public boolean needTotal() {
        return needTotal;
    }

    @Override
    public boolean needContent() {
        return needContent;
    }

    @Override
    public PageableVerbose verbose() {
        return verbose;
    }

    @Override
    public ResortStrategy resortStrategy() {
        return resortStrategy;
    }

    @Nullable
    @Override
    public Sort getSort(boolean applyResortStrategy) {
        return sort == null ? null : applyResortStrategy ? sort.resort(resortStrategy) : sort;
    }

    @Override
    public void resort(String property, String... newProperties) {
        Sort sort = getSort();
        if (sort != null) {
            this.sort = sort.resort(property, newProperties);
        }
    }

    @Override
    public void resort(String property, Function<Order, Sort> resortHandler) {
        Sort sort = getSort();
        if (sort != null) {
            this.sort = sort.resort(property, resortHandler);
        }
    }
}
