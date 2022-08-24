package redisdemo.flink;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.calcite.shaded.com.google.common.reflect.TypeToken;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import redisdemo.flink.flinkInterface.FlinkInterface;
import redisdemo.store.UrlStorage;

import java.util.*;
import java.util.List;
public class FlinkImpl implements FlinkInterface {

    private UrlStorage urlStorage;





    @Override
    public List<Tuple2<String, Integer>> getUrls() {
        Set<String> allKey = urlStorage.getAllKey();

        List<String> allKeyList = new LinkedList<>(allKey);
        List<Tuple2<String, Integer>> urls = new LinkedList<>();

        for (int i = 0; i < allKeyList.size(); i++) {
            Random r = new Random();
            //随机产生0，1
            int id = r.nextInt(2);
            urls.add(new Tuple2<>(allKeyList.get(i), id));
        }

        //清空redis的key
        for (Tuple2<String, Integer> key : urls) {
            urlStorage.dele(key.f0);
        }

        return urls;
    }

    //筛选标识为0或1的url
    @Override
    public void filter(int id) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<List<Tuple2<String, Integer>>> urlStreams = env.fromElements(getUrls());

        SingleOutputStreamOperator<List<Tuple2<String, Integer>>> urls = urlStreams.filter(new FilterFunction<List<Tuple2<String, Integer>>>() {
            @Override
            public boolean filter(List<Tuple2<String, Integer>> tuple2s) throws Exception {
                for (int i = 0; i < tuple2s.size(); i++) {
                    if (tuple2s.get(i).f1 == id) {
                        return tuple2s.get(i).f1 == id;
                    }

                }
                return true;
            }
        });

        urls.print();

        env.execute();
    }

    //将一条url写入redis
    @Override
    public void writeToRedis(String jsonUrl) {

        //解析json字符串

        Gson gson = new Gson();

        List<String> urlList = gson.fromJson(jsonUrl, new TypeToken<List<String>>() {
        }.getType());

        for (String message : urlList) {
            JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
            String url = jsonObject.get("url").getAsString();

            urlStorage.read(url);
            System.out.println(url);
        }





    }



}
