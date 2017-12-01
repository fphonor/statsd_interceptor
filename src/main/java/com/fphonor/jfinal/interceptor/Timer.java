package com.fphonor.jfinal.interceptor;

public class Timer extends StatsBase {
    @Override
    public InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
        return inv -> {
            long start = System.currentTimeMillis();
            try {
                consumer.accept(inv);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                client.recordExecutionTime(metricName, Math.max(0, System.currentTimeMillis() - start), sampleRate);
            }
        };
    }
}