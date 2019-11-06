package io.github.easyprofiler.model;

import java.time.LocalDateTime;


public class MethodAccessInfo {
    private String method;
    private String uri;

    //历史的调用信息
    private long invokeCount;
    private long okCount;
    private long errorCount;
    private long minMills;
    private long maxMills;
    private long avgMills;
    private LocalDateTime maxInvokeAt;

    //最近一天的调用信息
    private long lastDayCount;
    private long lastDayOkCount;
    private long lastDayErrorCount;
    private long lastDayMinMills;
    private long lastDayMaxMills;
    private long lastDayAvgMills;
    private LocalDateTime lastDayMaxInvokeAt;

    //上一次的调用信息
    private long lastMills;
    private LocalDateTime lastInvokeAt;


    public MethodAccessInfo maxInvokeAt(LocalDateTime maxInvokeAt) {
        this.maxInvokeAt = maxInvokeAt;
        return this;
    }

    public MethodAccessInfo lastDayCount(long lastDayCount) {
        this.lastDayCount = lastDayCount;
        return this;
    }

    public MethodAccessInfo lastDayOkCount(long lastDayOkCount) {
        this.lastDayOkCount = lastDayOkCount;
        return this;
    }

    public MethodAccessInfo lastDayErrorCount(long lastDayErrorCount) {
        this.lastDayErrorCount = lastDayErrorCount;
        return this;
    }

    public MethodAccessInfo lastDayMinMills(long lastDayMinMills) {
        this.lastDayMinMills = lastDayMinMills;
        return this;
    }

    public MethodAccessInfo lastDayMaxMills(long lastDayMaxMills) {
        this.lastDayMaxMills = lastDayMaxMills;
        return this;
    }

    public MethodAccessInfo lastDayAvgMills(long lastDayAvgMills) {
        this.lastDayAvgMills = lastDayAvgMills;
        return this;
    }

    public MethodAccessInfo lastDayMaxInvokeAt(LocalDateTime lastDayMaxInvokeAt) {
        this.lastDayMaxInvokeAt = lastDayMaxInvokeAt;
        return this;
    }

    public MethodAccessInfo lastMills(long lastMills) {
        this.lastMills = lastMills;
        return this;
    }


    public MethodAccessInfo lastInvokeAt(LocalDateTime lastInvokeAt) {
        this.lastInvokeAt = lastInvokeAt;
        return this;
    }

    public MethodAccessInfo method(String method) {
        this.method = method;
        return this;
    }

    public MethodAccessInfo uri(String uri) {
        this.uri = uri;
        return this;
    }


    public MethodAccessInfo invokeCount(long invokeCount) {
        this.invokeCount = invokeCount;
        return this;
    }

    public MethodAccessInfo okCount(long okCount) {
        this.okCount = okCount;
        return this;
    }

    public MethodAccessInfo errorCount(long errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public MethodAccessInfo minMills(long minMills) {
        this.minMills = minMills;
        return this;

    }

    public MethodAccessInfo maxMills(long maxMills) {
        this.maxMills = maxMills;
        return this;
    }

    public MethodAccessInfo avgMills(long avgMills) {
        this.avgMills = avgMills;
        return this;
    }


    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }


    public long getInvokeCount() {
        return invokeCount;
    }


    public long getOkCount() {
        return okCount;
    }


    public long getErrorCount() {
        return errorCount;
    }


    public long getMinMills() {
        return minMills;
    }


    public long getMaxMills() {
        return maxMills;
    }


    public long getAvgMills() {
        return avgMills;
    }


    public LocalDateTime getMaxInvokeAt() {
        return maxInvokeAt;
    }


    public long getLastDayOkCount() {
        return lastDayOkCount;
    }


    public long getLastDayCount() {
        return lastDayCount;
    }


    public long getLastDayErrorCount() {
        return lastDayErrorCount;
    }


    public long getLastDayMinMills() {
        return lastDayMinMills;
    }


    public long getLastDayMaxMills() {
        return lastDayMaxMills;
    }


    public long getLastDayAvgMills() {
        return lastDayAvgMills;
    }


    public LocalDateTime getLastDayMaxInvokeAt() {
        return lastDayMaxInvokeAt;
    }


    public long getLastMills() {
        return lastMills;
    }


    public LocalDateTime getLastInvokeAt() {
        return lastInvokeAt;
    }

}
