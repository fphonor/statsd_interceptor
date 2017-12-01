package com.fphonor.jfinal.interceptor;

public abstract class GaugeSender extends StatsBase {
    @Override
    public InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
        return inv -> {
            try {
                consumer.accept(inv);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                client.recordGaugeDelta(this.metricName, this.getGauge());
            }
        };
    }

    public abstract long getGauge();
}
