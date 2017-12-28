package io.github.fphonor.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import java.util.function.Consumer;

public interface StatsInterceptor extends Interceptor, Enhancer<InvocationConsumer> {
    InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate);
}
