package org.zt.sweet.utils.stat;

/**
 * 统计构造类
 * 
 * @author Ternence
 * @date 2017年12月26日
 */
public class StatBuilder {

    public static Builder builder() {
        return new StatBuilder().new Builder();
    }

    public class Builder {

        private StatConfig config = new StatConfig();

        public Builder threads(int threadNum) {
            config.setThreadNum(threadNum);
            return this;
        }


        public Builder duration(int duration) {
            config.setDuration(duration);
            return this;
        }

        public Builder warmup(int warmup) {
            config.setWarmUp(warmup);
            return this;
        }


        public Builder loop(long loop) {
            config.setLoop(loop);
            return this;
        }

        public StatConfig build() {
            return config;
        }
    }

}
