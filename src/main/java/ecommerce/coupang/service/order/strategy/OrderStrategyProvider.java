package ecommerce.coupang.service.order.strategy;

import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderStrategyProvider {

    private final Map<Class<?>, OrderStrategy<?>> strategyMap = new HashMap<>();

    public OrderStrategyProvider(ApplicationContext applicationContext) {
        Map<String, OrderStrategy> strategies = applicationContext.getBeansOfType(OrderStrategy.class);

        for (OrderStrategy<?> strategy : strategies.values()) {
            Class<?> genericType = getGenericType(strategy);
            if (genericType != null)
                strategyMap.put(genericType, strategy);
        }
    }

    private Class<?> getGenericType(OrderStrategy<?> strategy) {
        return (Class<?>) ((ParameterizedType) strategy.getClass().getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public <T extends CreateOrderRequest> OrderStrategy<T> getStrategy(Class<T> requestType) {
        return (OrderStrategy<T>) strategyMap.get(requestType);
    }
}
