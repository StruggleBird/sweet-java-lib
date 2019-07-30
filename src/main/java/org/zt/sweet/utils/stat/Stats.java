package org.zt.sweet.utils.stat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Stats {

    private static Stat start() {
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
                        return;
                    }

                    System.out.printf("Total:%7s,Current TPS:%7s,Avg TPS: %7s,Avg RT: %7sms,Duration: %7ss,Err num: %7s,Err percent: %4s%%\r\n",
                                    stat.getTotalExec(), stat.getTotalExec().get() - stat.getPrevCount(), stat.getAvgTPS(), stat.getAvgRT(),
                                    stat.getDuration(), stat.getErrorCount(),
                                    String.format("%.2f", stat.getErrorCount() * 100 / (double) stat.getTotalExec().get()));
                }
            }
        });

        t.start();
        stat.setStatThread(t);
        return stat;
    }

    /**
     *
     * @param task 任务方法
     * @param threads 线程数量
     * @return
     * @date 2016年11月11日
     * @author Ternence
     */
    public static Holder start(final Callable<?> task, int threads) {


        return start(task, StatBuilder.builder().threads(threads).build());
    }

    /**
     *
     * @param task 任务方法
     * @param threadnum 线程数量
     * @param warmupTime 预热时间，单位秒
     * @param duration 持续时间，秒
     * @return
     * @date 2016年11月11日
     * @author Ternence
     */
    public static Holder start(final Callable<?> task, final StatConfig config) {

        final int warmupTime = config.getWarmUp();
        final int threadnum = config.getThreadNum();


        final Stat stat = Stats.start();
        final ExecutorService executor = Executors.newFixedThreadPool(threadnum);
        if (warmupTime > 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(warmupTime * 1000);
                        System.out.println("Warmup complated...........................................");
                        stat.begin();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }



        for (int i = 0; i < threadnum; i++) {
            executor.submit(new Runnable() {

                @Override
                public void run() {
                    long loopTimes = 0;
                    while (!Thread.interrupted() && (config.getLoop() <= 0 || loopTimes < config.getLoop())) {
                        long start = System.currentTimeMillis();
                        try {
                            task.call();
                        } catch (InterruptedException e) {
                            // 线程被中断 返回
                            return;
                        } catch (Throwable e) {
                            stat.addAndGetError(1);
                        } finally {
                            loopTimes++;
                        }
                        long costTime = System.currentTimeMillis() - start;
                        stat.addAndGet(1, costTime);
                    }

                }
            });
        }


        return new Holder(stat, executor);
    }

}
