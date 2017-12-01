package com.fphonor.jfinal.interceptor;

import com.jfinal.aop.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fphonor.statsd.Client;
import com.timgroup.statsd.StatsDClient;

import java.util.HashMap;
import java.util.Map;

public abstract class StatsBase implements StatsInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(StatsBase.class);
    protected StatsDClient client = null;
    protected String metricName = null;
    protected float sampleRate = 1;

    public StatsBase() {
        this.client = Client.getInstance();

        Map<String, Object> me = new HashMap<>();

        init(me);

        if (this.metricName == null && me.get("metricName") != null) {
            this.metricName = (String) me.get("metricName");
        } else {
            this.metricName = this.getClass().getName();
        }
        if (me.get("sampleRate") != null) {
            this.sampleRate = (float) me.get("sampleRate");
        }
        logMetricName(this.metricName);
    }

    @Override
    public void intercept(Invocation invocation) {
        if (isMonitored(invocation)) {
            enhance(inv -> inv.invoke(), metricName, sampleRate).accept(invocation);
        } else {
            invocation.invoke();
        }
    }

    protected void init(Map<String, Object> me) {}

    private void logMetricName(String metricName) {
        logger.info("Stats Metric [{}]({}) initiated.\n", metricName, this);
    }
}