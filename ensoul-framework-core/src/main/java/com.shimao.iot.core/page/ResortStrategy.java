package com.shimao.iot.core.page;


import com.shimao.iot.core.sort.Order;
import org.springframework.lang.NonNull;

/**
 * 重排序策略
 *
 * @author striver.cradle
 */
public interface ResortStrategy {
    Order transfer(Order order);
}
