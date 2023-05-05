package com.shimao.iot.core.page;


import com.shimao.iot.core.sort.Direction;
import com.shimao.iot.core.sort.Order;
import com.shimao.iot.core.sort.Sort;
import com.shimao.iot.core.sort.SortDefaults;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author striver.cradle
 */
public class PageBuilder {
    private int page;
    private int size = PageDefaults.PAGE_SIZE;
    private int defaultPageSize = PageDefaults.PAGE_SIZE;
    private int maxPageSize = PageDefaults.MAX_PAGE_SIZE;

    private boolean needTotal = PageDefaults.NEED_TOTAL;
    private boolean needContent = PageDefaults.NEED_CONTENT;
    private boolean fixEdge = PageDefaults.IS_FIX_EDGE;
    private PageableVerbose verbose = PageDefaults.PAGEABLE_VERBOSE;
    private boolean pageNumberOneIndexed = PageDefaults.PAGE_NUMBER_ONE_INDEXED;
    private ResortStrategy resortStrategy = SortDefaults.RESORT_STRATEGY;
    private List<Order> orders = new ArrayList();

    private PageBuilder(int page) {
        this.page = page;
    }

    public static PageBuilder firstPage() {
        return page(1);
    }

    public static PageBuilder page(int page) {
        return new PageBuilder(page);
    }

    public PageBuilder size(int size) {
        this.size = size;
        return this;
    }

    public PageBuilder defaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
        return this;
    }

    public PageBuilder maxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
        return this;
    }

    public PageBuilder needTotal(boolean needTotal) {
        this.needTotal = needTotal;
        return this;
    }

    public PageBuilder needContent(boolean needContent) {
        this.needContent = needContent;
        return this;
    }

    public PageBuilder fixEdge(boolean fixEdge) {
        this.fixEdge = fixEdge;
        return this;
    }

    public PageBuilder verbose(PageableVerbose verbose) {
        this.verbose = verbose;
        return this;
    }

    public PageBuilder pageNumberOneIndexed(boolean pageNumberOneIndexed) {
        this.pageNumberOneIndexed = pageNumberOneIndexed;
        this.page = Math.max(PageUtil.getFirstPageNumber(pageNumberOneIndexed), this.page);
        return this;
    }

    public PageBuilder resortStrategy(ResortStrategy resortStrategy) {
        this.resortStrategy = resortStrategy;
        return this;
    }

    public PageBuilder sort(Direction direction, String... fields) {
        Stream.of(fields).forEach(field -> orders.add(new Order(direction, field)));
        return this;
    }

    public PageBuilder sort(@Nullable Sort sort) {
        if (sort != null) {
            sort.forEach(orders::add);
        }
        return this;
    }

    public Pageable build() {
        return new PageRequest(
                page, size, defaultPageSize, maxPageSize,
                needTotal, needContent,
                fixEdge, verbose, pageNumberOneIndexed, resortStrategy,
                orders.toArray(new Order[]{})
        );
    }
}
