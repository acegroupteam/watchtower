package org.acegroup.watchtower.easyprofiler;

import org.acegroup.watchtower.easyprofiler.model.MethodAccessInfo;
import org.acegroup.watchtower.easyprofiler.util.NetUtil;
import org.acegroup.watchtower.easyprofiler.util.ProfileInfoHolder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @author ？？？
 */
public class ProfilerViewRender {

    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    public final static String titleTpl = "<span>启动时间:%s  主机:%s 处理中:%s 成功:%s 失败:%s Biz异常:%s</span>";
    public final static String threadTpl = "<span style='margin-left:20px'>就绪线程:%s 阻塞线程:%s 等待线程:%s 活动线程:%s 最大峰值:%s 守护线程:%s</span>";
    public final static String headerColumns = createTd("路径", "失败数", "成功数", "总数", "平均ms", "最大ms", "最小ms", "最大ms时间点", "累计成功数", "累计失败数", "累计调用数", "历史平均ms", "历史最小ms", "历史最大ms", "历史最大ms时间点");

    private final static String COLOR_WARN = "#b7929291";
    private static final int MAX_ERROR_COUNT = 1;
    private static final int MAX_MILLS = 500;

    static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();


    public static String statsRequest() {
        return String.format(titleTpl, formatDateTime(ProfileInfoHolder.getStartTime()), NetUtil.getLocalhostStr(), ProfileInfoHolder.PROCESSING_COUNT.get(), ProfileInfoHolder.SUCCESS_COUNT.get(), ProfileInfoHolder.FAILED_COUNT.get(), ProfileInfoHolder.BIZ_EX_COUNT.get());
    }

    public static String createTr(MethodAccessInfo ma) {
        String strLastDayMaxMills = ma.getLastDayMaxMills() >= MAX_MILLS ? "<span style='color:#c79696f5'>" + ma.getLastDayMaxMills() + "</span>" : String.valueOf(ma.getLastDayMaxMills());
        String s1 = createTd(ma.getUri(), ma.getLastDayErrorCount(), ma.getLastDayOkCount(), ma.getLastDayCount(), ma.getLastDayAvgMills(), strLastDayMaxMills, ma.getLastDayMinMills(), ma.getLastDayMaxInvokeAt());
        String s2 = createTd(ma.getOkCount(), ma.getErrorCount(), ma.getInvokeCount(), ma.getAvgMills(), ma.getMinMills(), ma.getMaxMills(), ma.getMaxInvokeAt());

        StringBuilder sb = new StringBuilder(1000);
        String bgColor = ma.getLastDayErrorCount() > MAX_ERROR_COUNT || ma.getLastDayAvgMills() > MAX_MILLS ? "background-color:" + COLOR_WARN + ";" : "";
        sb.append("<tr style='height:40px;").append(bgColor).append("'>");
        sb.append(s1);
        sb.append(s2);
        sb.append("</tr>");
        return sb.toString();

    }


    public static String createTd(Object... columns) {
        StringBuilder buf = new StringBuilder();
        Arrays.stream(columns).forEach(colItem -> {
            if (colItem instanceof LocalDateTime) {
                LocalDateTime dt = (LocalDateTime) colItem;
                colItem = formatDateTime(dt);
            }
            buf.append("<td style='padding:3px;'>").append(colItem).append("</td>");
        });


        return buf.toString();
    }


    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }


    public static String statsThread() {
        int newCount = 0, terminatedCount = 0;
//        long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
        int runnableCount = 0, blockedCount = 0, waitingCount = 0, timedWaitingCount = 0;
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        int threadCount = threadMXBean.getThreadCount();

        long[] ids = ManagementFactory.getThreadMXBean().getAllThreadIds();
        for (long id : ids) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(id, Integer.MAX_VALUE);
            if (threadInfo == null) {
                continue;
            }
            Thread.State state = threadInfo.getThreadState();
            if (state == Thread.State.NEW) {
                newCount++;
            }
            if (state == Thread.State.RUNNABLE) {
                runnableCount++;
            }
            if (state == Thread.State.BLOCKED) {
                blockedCount++;
            }
            if (state == Thread.State.WAITING) {
                waitingCount++;
            }
            if (state == Thread.State.TIMED_WAITING) {
                timedWaitingCount++;
            }
            if (state == Thread.State.TERMINATED) {
                terminatedCount++;
            }
        }
        return String.format(threadTpl, runnableCount, blockedCount, waitingCount + timedWaitingCount, threadCount, peakThreadCount, daemonThreadCount);


    }


}
