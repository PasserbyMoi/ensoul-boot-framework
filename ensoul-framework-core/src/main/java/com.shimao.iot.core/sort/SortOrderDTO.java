package com.shimao.iot.core.sort;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author striver.cradle
 */
@Data
@RequiredArgsConstructor
public class SortOrderDTO {

    @ApiModelProperty("字段")
    private final String property;

    @ApiModelProperty("顺序（ASC：升序 DESC：降序）")
    private final Direction direction;

    public SortOrderDTO(Order order) {
        this.direction = order.getDirection();
        this.property = order.getProperty();
    }
}
