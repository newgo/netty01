package org.ltc.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ltc
 * @Title: ${FILE_NAME}
 * @Package org.ltc.io
 * @Description: 线程池
 * @date 2018/6/323:11
 */
public class TimeServerHandlerExecutePool {
    private ExecutorService executor;

    public TimeServerHandlerExecutePool(int maxPoolSize,int queeuSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queeuSize));
    }
    public  void execute(Runnable task){
        executor.execute(task);
    }
}
