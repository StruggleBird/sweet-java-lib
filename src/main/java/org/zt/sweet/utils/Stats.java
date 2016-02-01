package org.zt.sweet.utils;



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

                    System.out.printf("Total:%7s,Per Sec:%7s,Avg: %7s,Duration: %7s s\r\n", stat.getTotal(),
                                    stat.getTotal().longValue() - stat.getPrevCount(), stat.getAvg(), stat.getDuration());
                }
            }
        });

        t.start();
        return stat;
    }

}
