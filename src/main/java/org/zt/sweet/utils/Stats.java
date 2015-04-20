package org.zt.sweet.utils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Stats {

    public static Stat start() {
        final Stat stat = new Stat();
        stat.begin();
        Thread t = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        stat.setPrevCount(stat.getTotal().longValue());
                        stat.addAndGetDuration();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Total:" + stat.getTotal() + ",Per Sec:"
                            + (stat.getTotal().longValue() - stat.getPrevCount()) + ",Avg:"
                            + stat.getAvg() + ",Duration:" + stat.getDuration()+"s");
                }
            }
        });

        t.start();
        return stat;
    }
    


    
}

 class Stat {

    private Date startTime; // 开始时间

    private AtomicLong total = new AtomicLong(0L); // 总数

    private Long prevCount = 0L; // 上一秒的数量

    private Long perSecCount = 0L; // 每秒数量

    private Long duration; // 开始后持续时间 ，秒


    public void begin() {
        startTime = new Date();
        duration = 0L;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public AtomicLong getTotal() {
        return total;
    }

    public Long setTotal(AtomicLong count) {
        this.total = count;
        return this.total.longValue();
    }

    public Long getPrevCount() {
        return prevCount;
    }

    public void setPrevCount(Long prevCount) {
        this.prevCount = prevCount;
    }

    public Long getPerSecCount() {
        return perSecCount;
    }

    public void setPerSecCount(Long perSecCount) {
        this.perSecCount = perSecCount;
    }

    public Long getAvg() {
        Date now = new Date();
        Long totalSeconds = (now.getTime() - startTime.getTime()) / 1000;
        return total.longValue() / totalSeconds;
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
    
    public Long addAndGet(int delta){
        return this.total.addAndGet(delta);
    }


}
