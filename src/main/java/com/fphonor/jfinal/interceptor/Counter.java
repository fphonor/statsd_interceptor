package com.fphonor.jfinal.interceptor;

public class Counter extends StatsBase {
    @Override
    public InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
        return inv -> {
            try {
                consumer.accept(inv);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                client.count(metricName, 1, sampleRate);
            }
        };
    }
}