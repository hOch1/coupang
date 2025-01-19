package ecommerce.coupang.service.order.strategy;

import ecommerce.coupang.dto.request.order.CreateOrderRequest;

import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderStrategyProvider {

    private final Map<Class<?>, OrderStrategy<?>> strategyMap = new HashMap<>();

    public OrderStrategyProvider(List<OrderStrategy<?>> orderStrategies) {
        for (OrderStrategy<?> strategy : orderStrategies) {
            Class<?> targetClass = AopUtils.getTargetClass(strategy); // 프록시에서 원본 클래스 추출
            Class<?> genericType = getGenericType(targetClass);
            if (genericType != null)
                strategyMap.put(genericType, strategy);
        }
    }

    private Class<?> getGenericType(Class<?> strategy) {
        return (Class<?>) ((ParameterizedType) strategy.getGenericInterfaces()[0])
            .getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public <T extends CreateOrderRequest> OrderStrategy<T> getStrategy(Class<T> requestType) {
        return (OrderStrategy<T>) strategyMap.get(requestType);
    }
}
