package redisdemo.store;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Description: use to store the urls
 * @Author: smx_Morgan
 * @Date: 2022/8/20 17:26
 */

public class UrlStorage {
    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis = jedisPool.getResource();

    public void read(String url){
        jedis.set(url,url);
    }
    public Set<String> getAllKey(){
        //当前数据库key的数量
        Long size = jedis.dbSize();
        //查询当前数据库的所有key

        //ljy 注释 2022.8.20  20：32
        if (size <= 0) {
            return null;
        } else {
            Set<String> set = jedis.keys("*");
            return set;
        }



        //set.forEach(System.out::println);
    }
    public void dele(String url){
        jedis.del(url);

    }
}
