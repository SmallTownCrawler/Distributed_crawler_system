package redisdemo.flink.flinkInterface;



import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

public interface FlinkInterface {
    // 获取redis中的urls
    //public List<String> getUrls1();

    //public List<String> getUrls2();

    public void filter(int id) throws Exception;


    public List<Tuple2<String, Integer>> getUrls();

    public void writeToRedis(String jsonUrl);
}
