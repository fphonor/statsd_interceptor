package io.github.fphonor.jfinal.interceptor;

interface Enhancer<T> {
    T enhance(T t, String metricName, double sampleRate);
}
