/**
 * 
 */
package org.zt.sweet.lang;

import org.junit.Test;
import org.zt.sweet.utils.StopWatch;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class StopWatchTest {

    @Test
    public void testA() {
        StopWatch sw = StopWatch.beginNano();
        Lang.sleep(1000);
        // sw.stop();
        System.out.println(sw.getDuration());
    }

    @Test
    public void testFor() {
        StopWatch sw = StopWatch.begin();
        long sum = 0;
        for (int i = 0; i < 1000000000; i++) {
            sum += i;
        }
        sw.stop();
        System.out.println("sum:" + sum);
        System.out.println("Duration:" + sw.getDuration() + "ms");
    }

}
