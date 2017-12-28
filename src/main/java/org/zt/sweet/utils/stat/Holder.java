package org.zt.sweet.utils.stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 统计持有者，开放给开发者来使用的对象
 * @author Ternence
 * @date 2017年12月28日
 */
public class Holder {

    
    private Stat stat;
    
    private ExecutorService executor;
    
    

    public Holder(Stat stat, ExecutorService executor) {
        super();
        this.stat = stat;
        this.executor = executor;
    }

    public Stat getStat() {
        return stat;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
    
    
    /**
     * 终止测试，最多等待seconds秒后结束
     * @param seconds
     * @throws InterruptedException
     * @date 2017年12月28日
     * @author Ternence
     * @return true:所有工作线程停止后退出,false:达到超时时间，强制退出
     */
    public boolean termination(int seconds) throws InterruptedException{
        executor.shutdown();
       return executor.awaitTermination(seconds, TimeUnit.SECONDS);
    }

    
    
}
