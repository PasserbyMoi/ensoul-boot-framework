package com.shimao.iot.core.page;

/**
 * @author striver.cradle
 */
public interface PageDefaults {

    // 最大页码
    int MAX_PAGE_SIZE = 2000;

    // 默认每页显示数量
    int PAGE_SIZE = 20;

    // 默认要查询总记录数
    boolean NEED_TOTAL = true;

    // 默认要查询记录列表
    boolean NEED_CONTENT = true;

    // 默认不自动修正分页参数
    boolean IS_FIX_EDGE = false;

    // 默认不返回分页冗余信息
    PageableVerbose PAGEABLE_VERBOSE = PageableVerbose.NONE;

    // 默认起始页为1
    boolean PAGE_NUMBER_ONE_INDEXED = true;
}
