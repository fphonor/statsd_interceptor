package io.github.fphonor.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import io.github.fphonor.jfinal.interceptor.InvocationConsumer;

public class MonitorByRegex implements Interceptor {
    private Pattern pattern;
    private double sampleRate;
    private List<StatsInterceptor> interceptors = new ArrayList<>();
    private Function<String, InvocationConsumer> consumerFunction;

    public MonitorByRegex(String regex) { this(regex, 1, true); }

    public MonitorByRegex(String regex, double sampleRate, boolean caseSensitive) {
        if (StrKit.isBlank(regex))
            throw new IllegalArgumentException("regex can not be blank.");

        this.sampleRate = sampleRate;
        pattern = caseSensitive ? Pattern.compile(regex) : Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        prepare();
    }

    public MonitorByRegex add(StatsInterceptor interceptor) {
        this.interceptors.add(interceptor);
        prepare();
        return this;
    }

    public MonitorByRegex add(Class<? extends StatsInterceptor> interceptorClass) {
        try {
            return add(interceptorClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepare() {
        this.consumerFunction = metricName -> this.interceptors.stream().reduce(
            inv -> inv.invoke(),
            (invConsumer, interceptor) -> interceptor.enhance(invConsumer, metricName, sampleRate),
            (invConsumer1, invConsumer2) -> invConsumer1.enhance(invConsumer2, metricName, sampleRate)
        );
    }

    public void intercept(final Invocation inv) {
        String target = inv.isActionInvocation() ? inv.getActionKey() : inv.getMethodName();
        if (pattern.matcher(target).matches()) {
            this.consumerFunction.apply(target.replaceAll("/", ".").substring(1)).accept(inv);
        } else {
            inv.invoke();
        }
    }
}
