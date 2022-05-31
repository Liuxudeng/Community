package com.nowcoder.community;

import com.nowcoder.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes(){
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","zhangshan");
        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));

    }

    @Test
    public void testSets(){
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,"刘备","关羽","张飞","赵云","诸葛亮");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));

    }

    /**
     * redis编程式事务
     */

    @Test
    public void testTransactional(){
        Object obj =   redisTemplate.execute(new SessionCallback() {

            @Override
         public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx1";
                operations.multi();

                operations.opsForSet().add(redisKey,"zhangshan");
                operations.opsForSet().add(redisKey,"lisi");
                operations.opsForSet().add(redisKey,"wangwu");
                System.out.println(operations.opsForSet().members(redisKey));

                return operations.exec();
            }
        });
        System.out.println(obj);
    }


    /**
     * 统计20万个数据的独立总数
     */

    @Test
    public void testHyberLoglog(){

        String redisKey = "test:h1:02";
        for (int i = 0; i < 1000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey,i);

        }

        for (int i = 0; i < 1000; i++) {

            int r = (int)(Math.random()*1000+1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);

        }

        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);

        System.out.println(size);

    }


    @Test
    public void test0529(){

       String rediskey = "test0529";
//        redisTemplate.opsForValue().set (rediskey,"666");
//      //  System.out.println(redisTemplate.opsForValue().get(rediskey));
//        System.out.println(redisTemplate.opsForValue().getClass());
//        System.out.println( redisTemplate.opsForValue().get ("test0529"));
redisTemplate.delete(rediskey);
        System.out.println( redisTemplate.opsForValue().get ("test0529"));

    }
}
