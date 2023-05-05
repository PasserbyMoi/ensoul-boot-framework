package com.shimao.iot.core.page;

import com.shimao.iot.common.enums.IStringCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author striver.cradle
 */
@Getter
@AllArgsConstructor
public enum PageableVerbose implements IStringCode {
    NONE("none", "不返回冗余"),
    ALL("all", "返回全部冗余"),
    PAGE("page", "返回当前分页详情"),
    SORT("sort", "返回当前排序规则");

    private String code;
    private String name;
}
