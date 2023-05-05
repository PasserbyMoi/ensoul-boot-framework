package com.shimao.iot.core.sort;


import com.shimao.iot.core.page.ResortStrategies;

/**
 * Add some description about this class.
 *
 * @author striver.cradle
 */
public interface SortDefaults {

    // 默认排序字段的命名规则：驼峰 -> 下划线
    ResortStrategies RESORT_STRATEGY = ResortStrategies.SAME_CASE;
}
