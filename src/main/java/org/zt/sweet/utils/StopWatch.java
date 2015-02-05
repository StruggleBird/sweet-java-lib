package org.zt.sweet.utils;


/**
 * 秒表
 * 
 */
public class StopWatch {

    private boolean nano;

    private long from;

    private long to;

    /**
     * 秒表开始计时，计时时间的最小单位是毫秒
     * 
     * @return 开始计时的秒表对象
     */
    public static StopWatch begin() {
        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    /**
     * 秒表开始计时，计时时间的最小单位是毫微秒
     * 
     * @return 开始计时的秒表对象
     */
    public static StopWatch beginNano() {
        StopWatch sw = new StopWatch();
        sw.nano = true;
        sw.start();
        return sw;
    }

    /**
     * 创建一个秒表对象，该对象的计时时间的最小单位是毫秒
     * 
     * @return 秒表对象
     */
    public static StopWatch create() {
        return new StopWatch();
    }

    /**
     * 创建一个秒表对象，该对象的计时时间的最小单位是毫微秒
     * 
     * @return 秒表对象
     */
    public static StopWatch createNano() {
        StopWatch sw = new StopWatch();
        sw.nano = true;
        return sw;
    }

    public static StopWatch run(Runnable atom) {
        StopWatch sw = begin();
        atom.run();
        sw.stop();
        return sw;
    }

    public static StopWatch runNano(Runnable atom) {
        StopWatch sw = beginNano();
        atom.run();
        sw.stop();
        return sw;
    }

    /**
     * 开始计时，并返回开始计时的时间，该时间最小单位由创建秒表时确定
     * 
     * @return 开始计时的时间
     */
    public long start() {
        from = currentTime();
        return from;
    }

    private long currentTime() {
        return nano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * 停止计时，并返回停止计时的时间，该时间最小单位由创建秒表时确定
     * 
     * @return 停止计时的时间
     */
    public long stop() {
        to = currentTime();
        return to;
    }

    /**
     * 返回计时结果
     * 
     * @return 计时结果
     */
    public long getDuration() {
        if (to == 0) {
            return currentTime() - from;
        }
        return to - from;
    }

    /**
     * 开始计时的时间，该时间最小单位由创建秒表时确定
     * 
     * @return 开始计时的时间
     */
    public long getStartTime() {
        return from;
    }

    /**
     * 停止计时的时间，该时间最小单位由创建秒表时确定
     * 
     * @return 停止计时的时间
     */
    public long getEndTime() {
        return to;
    }

    /**
     * 返回格式为 <code>Total: [计时时间][计时时间单位] : [计时开始时间]=>[计时结束时间]</code> 的字符串
     * 
     * @return 格式为 <code>Total: [计时时间][计时时间单位] : [计时开始时间]=>[计时结束时间]</code> 的字符串
     */
    @Override
    public String toString() {
        return String.format("Total: %d%s : [%s]=>[%s]", this.getDuration(), (nano ? "ns" : "ms"),
                new java.sql.Timestamp(from).toString(), new java.sql.Timestamp(to).toString());
    }

}
