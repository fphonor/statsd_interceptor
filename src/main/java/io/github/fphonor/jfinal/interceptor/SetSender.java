package io.github.fphonor.jfinal.interceptor;

public abstract class SetSender extends StatsBase {
    @Override
    public InvocationConsumer enhance(InvocationConsumer consumer, String metricName, double sampleRate) {
        return inv -> {
            try {
                consumer.accept(inv);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                client.recordSetEvent(metricName, getEventName());
            }
        };
    }

    public abstract String getEventName();
}
