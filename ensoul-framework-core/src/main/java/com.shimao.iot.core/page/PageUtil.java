package com.shimao.iot.core.page;

/**
 * @author striver.cradle
 */
public abstract class PageUtil {

    public static int getFirstPageNumber() {
        return getFirstPageNumber(PageDefaults.PAGE_NUMBER_ONE_INDEXED);
    }

    public static int getFirstPageNumber(boolean pageNumberOneIndexed) {
        return pageNumberOneIndexed ? 1 : 0;
    }

    public static int getLastPageNumber(long total, int pageSize) {
        return (int) Math.ceil((double) total / (double) pageSize);
    }

    public static int getOffset(int firstPageNumber, int pageNumber, int pageSize) {
        return (pageNumber - firstPageNumber) * pageSize;
    }
}
