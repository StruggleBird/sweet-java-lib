/**
 * 
 */
package org.zt.sweet.utils;

import java.io.IOException;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.zt.sweet.utils.stat.Holder;
import org.zt.sweet.utils.stat.StatBuilder;
import org.zt.sweet.utils.stat.Stats;


/**
 * @author Ternence
 * @create 2015年4月20日
 */
public class StatUtilsTest {
    @Test
    public void testStat() throws InterruptedException, IOException {
        Stats.start(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(RandomUtils.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, StatBuilder.builder().threads(100).build());
        Thread.sleep(5000);;

    }


    @Test
    public void testTermination() throws InterruptedException, IOException {
        Holder holder = Stats.start(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(RandomUtils.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, StatBuilder.builder().threads(100).build());
        holder.termination(5);

    }
}
