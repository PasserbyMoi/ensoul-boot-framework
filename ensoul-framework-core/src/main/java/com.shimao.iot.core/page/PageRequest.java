package com.shimao.iot.core.page;


import com.shimao.iot.core.sort.Order;
import com.shimao.iot.core.sort.SortDefaults;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author striver.cradle
 */
public class PageRequest extends AbstractPageRequest {

    public PageRequest(int page) {
        super(page, PageDefaults.PAGE_SIZE);
    }

    public PageRequest(int page, int size) {
        super(page, size);
    }

    /**
     * @see PageBuilder
     */
    PageRequest(int page,
                int size,
                int defaultPageSize,
                int maxPageSize,
                boolean needTotal,
                boolean needContent,
                boolean fixEdge,
                PageableVerbose verbose,
                boolean pageNumberOneIndexed,
                ResortStrategy resortStrategy,
                Order... orders) {
        super(page, size, defaultPageSize, maxPageSize, needTotal, needContent, fixEdge, verbose, pageNumberOneIndexed,
                resortStrategy, orders);
    }

    @NonNull
    @Override
    public Pageable jumpTo(int page) {
        return PageBuilder.page(page)
                .size(getPageSize())
                .defaultPageSize(getDefaultPageSize())
                .maxPageSize(getMaxPageSize())
                .needTotal(needTotal())
                .needContent(needContent())
                .fixEdge(isFixEdge())
                .verbose(verbose())
                .pageNumberOneIndexed(isPageNumberOneIndexed())
                .resortStrategy(resortStrategy())
                .sort(getSort())
                .build();
    }

    @NonNull
    @Override
    public Pageable config(PageConfigKey key, boolean enabled) {
        return PageBuilder.page(getPageNumber())
                .size(getPageSize())
                .defaultPageSize(getDefaultPageSize())
                .maxPageSize(getMaxPageSize())
                .needTotal(key == PageConfigKey.NEED_TOTAL ? enabled : needTotal())
                .needContent(key == PageConfigKey.NEED_CONTENT ? enabled : needTotal())
                .fixEdge(key == PageConfigKey.FIX_EDGE ? enabled : isFixEdge())
                .verbose(verbose())
                .pageNumberOneIndexed(key == PageConfigKey.PAGE_NUMBER_ONE_INDEXED ? enabled : isPageNumberOneIndexed())
                .resortStrategy(resortStrategy())
                .sort(getSort())
                .build();
    }

    @NonNull
    @Override
    public Pageable config(@Nullable PageableVerbose verbose) {
        verbose = verbose == null ? PageDefaults.PAGEABLE_VERBOSE : verbose;
        return PageBuilder.page(getPageNumber())
                .size(getPageSize())
                .defaultPageSize(getDefaultPageSize())
                .maxPageSize(getMaxPageSize())
                .needTotal(needTotal())
                .needContent(needTotal())
                .fixEdge(isFixEdge())
                .verbose(verbose)
                .pageNumberOneIndexed(isPageNumberOneIndexed())
                .resortStrategy(resortStrategy())
                .sort(getSort())
                .build();
    }

    @NonNull
    @Override
    public Pageable config(@Nullable ResortStrategy strategy) {
        strategy = strategy == null ? SortDefaults.RESORT_STRATEGY : strategy;
        return PageBuilder.page(getPageNumber())
                .size(getPageSize())
                .defaultPageSize(getDefaultPageSize())
                .maxPageSize(getMaxPageSize())
                .needTotal(needTotal())
                .needContent(needTotal())
                .fixEdge(isFixEdge())
                .verbose(verbose())
                .pageNumberOneIndexed(isPageNumberOneIndexed())
                .resortStrategy(strategy)
                .sort(getSort())
                .build();
    }
}
