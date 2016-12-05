package org.zt.sweet.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Stats {

    public static Stat start() {
        final Stat stat = new Stat();
        stat.begin();
        Thread t = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        stat.setPrevCount(stat.getTotalExec().longValue());
                        stat.addAndGetDuration();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.printf("Total:%7s,Current TPS:%7s,Avg TPS: %7s,Avg RT: %7s,Duration: %7ss,Err num: %7s \r\n", stat.getTotalExec(),
                                    stat.getTotalExec().longValue() - stat.getPrevCount(), stat.getAvgTPS(), stat.getAvgRT(), stat.getDuration(),
                                    stat.getErrorCount());
                }
            }
        });

        t.start();
        return stat;
    }

    /**
     *
     * @param task 任务方法
     * @param threadnum 线程数量
     * @return
     * @date 2016年11月11日
     * @author Ternence
     */
    public static Stat start(final Runnable task, int threadnum) {
        
        
        return start(task, threadnum, 0);
    }
    
    /**
     *
     * @param task 任务方法
     * @param threadnum 线程数量
     * @param warmupTime 预热时间，单位秒
     * @return
     * @date 2016年11月11日
     * @author Ternence
     */
    public static Stat start(final Runnable task, int threadnum,final int warmupTime){
        final Stat stat = Stats.start();
        if (warmupTime > 0 ) {
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        Thread.sleep(warmupTime * 1000);
                        System.out.println("warmup complated...........................................");
                        stat.begin();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadnum);
        for (int i = 0; i < threadnum; i++) {
            executor.submit(new Runnable() {

                @Override
                public void run() {
                    while(true){
                        long start = System.currentTimeMillis();
                        try {
                            task.run();
                        } catch (Throwable e) {
                            stat.addAndGetError(1);
                            e.printStackTrace();
                        }
                        long costTime = System.currentTimeMillis() - start;
                        stat.addAndGet(1,costTime);
                    }
                    
                }
            });
        }
        
        return stat;
    }

}
