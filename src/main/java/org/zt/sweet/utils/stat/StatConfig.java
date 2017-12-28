package org.zt.sweet.utils.stat;

public class StatConfig {
    /**
     * 测试持续时长 ，秒
     */
    private int duration;

    /**
     * 预热时间，秒
     */
    private int warmUp;

    /**
     * 线程个数
     */
    private int threadNum;

    /**
     * 循环次数，0：永远（默认）
     */
    private long loop;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWarmUp() {
        return warmUp;
    }

    public void setWarmUp(int warmUp) {
        this.warmUp = warmUp;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public long getLoop() {
        return loop;
    }

    public void setLoop(long loop) {
        this.loop = loop;
    }



}
