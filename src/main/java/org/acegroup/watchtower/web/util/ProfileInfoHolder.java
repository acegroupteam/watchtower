package org.acegroup.watchtower.web.util;


import org.acegroup.watchtower.web.model.ControllerAccessInfo;
import org.acegroup.watchtower.web.model.MethodAccessInfo;
import org.acegroup.watchtower.web.model.Profile;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class ProfileInfoHolder {

    private static ConcurrentHashMap<Class<?>, ControllerAccessInfo> map = new ConcurrentHashMap<>();

    private static LocalDateTime startTime;


    public static final AtomicLong processingCount = new AtomicLong(0);
    public static final AtomicLong failedCount = new AtomicLong(0);
    public static final AtomicLong bizExCount = new AtomicLong(0);
    public static final AtomicLong successCount = new AtomicLong(0);


    public static synchronized void setStartTime(LocalDateTime st) {
        startTime = st;
    }

    public static LocalDateTime getStartTime() {
        return startTime;
    }

    public static void addProfilerInfo(Profile profileInfo) {
        if (profileInfo == null) {
            return;
        }
        if (profileInfo.isSuccess()) {
            successCount.incrementAndGet();
        } else {
            failedCount.incrementAndGet();
        }

        Class<?> controllerClazz = profileInfo.getClazz();
        Method method = profileInfo.getMethod();
        ControllerAccessInfo cai = findByClass(controllerClazz);
        List<MethodAccessInfo> methodAccessInfos = findByMethod(cai);
        MethodAccessInfo mai = getMethodAccessInfo(methodAccessInfos, method);
        if (mai == null) {
            mai = newMethodAccessInfo(profileInfo);
            methodAccessInfos.add(mai);
            return;
        }
        setMethodAccess(mai, profileInfo);
    }


    private static ControllerAccessInfo findByClass(Class controllerClazz) {
        ControllerAccessInfo cai = map.get(controllerClazz);
        if (cai == null) {
            cai = new ControllerAccessInfo();
            cai.setClazz(controllerClazz);
            map.putIfAbsent(controllerClazz, cai);
        }
        return cai;
    }

    public static List<MethodAccessInfo> findByMethod(ControllerAccessInfo cai) {
        List<MethodAccessInfo> methodAccessInfos = cai.getMethods();
        if (methodAccessInfos == null) {
            methodAccessInfos = new ArrayList<>();
            cai.setMethods(methodAccessInfos);
        }
        return methodAccessInfos;
    }

    private static MethodAccessInfo getMethodAccessInfo(List<MethodAccessInfo> mais, Method method) {
        for (MethodAccessInfo mai : mais) {
            if (method.getName().equals(mai.getMethod())) {
                return mai;
            }
        }
        return null;
    }

    public static Map<String, List<MethodAccessInfo>> getAllAccessInfo() {
        Map<String, List<MethodAccessInfo>> result = new HashMap<>();
        for (Map.Entry<Class<?>, ControllerAccessInfo> entry : map.entrySet()) {
            ControllerAccessInfo cai = entry.getValue();
            result.put(cai.getClazz().getSimpleName(), cai.getMethods());
        }
        return result;
    }


    private static void setMethodAccess(MethodAccessInfo mai, Profile profileInfo) {


        long useTime = profileInfo.getEndTime() - profileInfo.getStartTime();
        boolean occurError = profileInfo.isSuccess() == false;
        LocalDateTime lastInvokeTime = mai.getLastInvokeAt();
        LocalDate now = LocalDate.now();

        if (lastInvokeTime.getMonth() != now.getMonth() || lastInvokeTime.getDayOfMonth() != now.getDayOfMonth()) {
            mai.lastDayCount(0).lastDayErrorCount(0).lastDayOkCount(0).lastDayAvgMills(useTime);
            mai.lastDayMinMills(useTime).lastDayMaxMills(useTime).lastDayMaxInvokeAt(LocalDateTime.now());
        }

        if (useTime < mai.getMinMills()) {
            mai.minMills(useTime);
        }
        if (useTime < mai.getLastDayMinMills()) {
            mai.lastDayMinMills(useTime);
        }
        if (useTime > mai.getMaxMills()) {
            mai.maxMills(useTime);
            mai.maxInvokeAt(LocalDateTime.now());
        }
        if (useTime > mai.getLastDayMaxMills()) {
            mai.lastDayMaxMills(useTime);
            mai.lastDayMaxInvokeAt(LocalDateTime.now());
        }
        mai.invokeCount(mai.getInvokeCount() + 1);
        mai.lastDayCount(mai.getLastDayCount() + 1);
        if (occurError) {
            mai.errorCount(mai.getErrorCount() + 1);
            mai.lastDayErrorCount(mai.getLastDayErrorCount() + 1);
        } else {
            mai.okCount(mai.getOkCount() + 1);
            mai.lastDayOkCount(mai.getLastDayOkCount() + 1);
        }
        mai.avgMills((mai.getAvgMills() + useTime) / 2);
        mai.lastDayAvgMills((mai.getLastDayAvgMills() + useTime) / 2);
        mai.lastInvokeAt(LocalDateTime.now());
        mai.lastMills(useTime);
    }


    private static MethodAccessInfo newMethodAccessInfo(Profile profile) {

        long useTime = profile.getEndTime() - profile.getStartTime();
        String uri = profile.getUri();
        MethodAccessInfo info = new MethodAccessInfo();
        LocalDateTime now = LocalDateTime.now();

        info.method(profile.getMethod().getName()).uri(uri);
        info.invokeCount(1).minMills(useTime).maxMills(useTime).avgMills(useTime).maxInvokeAt(now).maxInvokeAt(now).lastMills(useTime).lastInvokeAt(now);
        info.lastDayCount(1).lastDayMinMills(useTime).lastDayMaxMills(useTime).lastDayAvgMills(useTime).lastDayMaxInvokeAt(now).lastDayMaxInvokeAt(now);

        if (profile.isSuccess()) {
            info.okCount(1).lastDayOkCount(1);
        } else {
            info.errorCount(1).lastDayErrorCount(1);
        }
        return info;
    }
}
