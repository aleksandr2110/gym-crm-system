package epam.bpp;

import epam.annotation.ExecutionTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//@Component
public class TimeExecutingMethodBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = Logger.getLogger(TimeExecutingMethodBeanPostProcessor.class.getName());

    private final Map<String, Class<?>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(ExecutionTime.class)) {
                map.put(beanName, beanClass);
                break;
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {

            return Proxy.newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    (proxy, method, args) -> {

                        Method targetMethod = beanClass.getMethod(method.getName(), method.getParameterTypes());

                        if (targetMethod.isAnnotationPresent(ExecutionTime.class)) {
                            long start = System.currentTimeMillis();

                            // Execute the original method
                            Object result = method.invoke(bean, args);

                            long end = System.currentTimeMillis();
                            logger.info("Method " + method.getName() + " took " + (end - start) + " ms to execute.");
                            return result;
                        }

                        // Execute normally if not annotated
                        return method.invoke(bean, args);
                    }

            );
        }
        return bean;
    }
}
