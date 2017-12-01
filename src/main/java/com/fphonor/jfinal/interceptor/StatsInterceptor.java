package com.fphonor.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import java.util.function.Consumer;

public interface StatsInterceptor extends Interceptor {
    default boolean isMonitored (Invocation inv) { return true; }

    InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate);

    @FunctionalInterface
    interface InvocationConsumer extends Consumer<Invocation>, StatsInterceptor{
        @Override
        default InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
            return this.enhance(consumer, metricName, sampleRate);
        }
        @Override
        default void intercept(Invocation invocation) { invocation.invoke(); }
    }
}