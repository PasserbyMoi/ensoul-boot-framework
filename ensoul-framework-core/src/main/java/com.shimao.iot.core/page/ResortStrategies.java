package com.shimao.iot.core.page;

import com.shimao.iot.core.NamingStrategy;
import com.shimao.iot.core.sort.Order;
import org.springframework.lang.NonNull;

/**
 * Add some description about this class.
 *
 * @author striver.cradle
 */
public enum ResortStrategies implements ResortStrategy {

    SAME_CASE {
        @Override
        public Order transfer(@NonNull Order order) {
            String property = NamingStrategy.SAME_CASE.translate(order.getProperty());
            return order.with(property);
        }
    },

    SNAKE_CASE {
        @Override
        public Order transfer(@NonNull Order order) {
            String property = NamingStrategy.SNAKE_CASE.translate(order.getProperty());
            return order.with(property);
        }
    }
}
