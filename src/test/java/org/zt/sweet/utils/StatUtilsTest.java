/**
 * 
 */
package org.zt.sweet.utils;

import org.junit.Test;


/**
 * @author Ternence
 * @create 2015年4月20日
 */
public class StatUtilsTest {
    @Test
    public void testStat() throws InterruptedException{
        Stat stat = Stats.start();
        
        while (true) {
            Thread.sleep(1);
            stat.addAndGet(1);
        }
        
    }
}
