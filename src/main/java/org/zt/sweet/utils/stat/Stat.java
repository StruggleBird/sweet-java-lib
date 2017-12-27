package org.zt.sweet.utils.stat;

import java.util.concurrent.atomic.AtomicLong;

public class Stat {

    private Long startTime; // 开始时间

    private AtomicLong totalExec = new AtomicLong(0L); // 执行总数

    private AtomicLong totalRT = new AtomicLong(0L); // 总响应时长


    private long duration; // 开始后持续时间 ，秒

    private long prevCount;// 上次统计的执行总数量

    private AtomicLong errorCount = new AtomicLong(0);// 错误总数


    public void begin() {
        duration = prevCount = 0L;
        totalExec.set(0);
        totalRT.set(0);

        startTime = System.currentTimeMillis();

    }

    public Long getStartTime() {
        return startTime;
    }

    public AtomicLong getTotalExec() {
        return totalExec;
    }

    public Long setTotalExec(AtomicLong count) {
        this.totalExec = count;
        return this.totalExec.longValue();
    }


    public Long getAvgTPS() {
        Long totalSeconds = (System.currentTimeMillis() - startTime) / 1000;
        if (totalSeconds <= 0) {
            return 0L;
        }
        return totalExec.longValue() / totalSeconds;
    }

    /**
     * @return the duration
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    Long addAndGetDuration() {
        return this.duration++;
    }

    public Long addAndGet(int delta) {
        return this.totalExec.addAndGet(delta);
    }

    public Long addAndGet(int delta, Long costTime) {
        totalRT.addAndGet(costTime);
        return this.totalExec.addAndGet(delta);
    }

    public Long getAvgRT() {
        long avgRT = totalExec.get() == 0 ? -1 : totalRT.get() / totalExec.get();
        return avgRT;
    }

    public void setPrevCount(long longValue) {
        prevCount = longValue;
    }

    public long getPrevCount() {
        return prevCount;
    }


    /**
     * @return the errorCount
     */
    public long getErrorCount() {
        return errorCount.get();
    }

    public long addAndGetError(int num) {
        long errNum = errorCount.addAndGet(num);
        return errNum;
    }

}
