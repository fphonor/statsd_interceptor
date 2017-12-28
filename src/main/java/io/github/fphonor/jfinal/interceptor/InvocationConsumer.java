package io.github.fphonor.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import java.util.function.Consumer;

@FunctionalInterface
interface InvocationConsumer extends Enhancer<InvocationConsumer>, Consumer<Invocation>, Interceptor{
    @Override
    default InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
        return this.enhance(consumer, metricName, sampleRate);
    }

    @Override
    default void intercept(Invocation invocation) { invocation.invoke(); }
}
