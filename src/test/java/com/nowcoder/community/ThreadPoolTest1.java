package com.nowcoder.community;

import com.nowcoder.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.DatabaseMetaData;
import java.util.Date;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest1 {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;


    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest1.class);

    //jdk普通线程池

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    //jdk可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);


    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //JDK普通线程池
    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello test01");
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        sleep(10000);
    }

    /**
     * jdk定时任务线程池
     */
    @Test
    public void testScheduledExecutorService(){
Runnable runnable = new Runnable() {
    @Override
    public void run() {
        logger.debug("hello test02");
    }


};
scheduledExecutorService.scheduleAtFixedRate(runnable,10000,500, TimeUnit.MILLISECONDS);

sleep(20000);
    }


    //Spring的普通线程池
    @Test
    public void testThreadPoolTaskExecutor(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello springTaskExecutor");
            }
        };

        for (int i = 0; i < 10; i++) {

            threadPoolTaskExecutor.submit(runnable);
        }

        sleep(10000);
    }

    //spring定时任务的线程池
    @Test
    public void testThreadPoolTaskScheduler(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello springSchedule");
            }
        };


        Date startTime = new Date(System.currentTimeMillis()+10000);

      threadPoolTaskScheduler.scheduleAtFixedRate(runnable,startTime,1000);

      sleep(20000);
    }

    @Test
    public void testThreadPoolTaskSchedulerSimple(){
        sleep(20000);
    }
}
