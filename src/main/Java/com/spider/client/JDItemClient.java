package com.spider.client;

import com.alibaba.fastjson.JSON;
import com.hbaseapi.java.java.HBaseConnection;
import com.spider.entity.Item;
import com.spider.entity.SearchItemInfo;
import com.spider.processor.itemProcessor.GetItem;
import com.spider.processor.itemProcessor.GetJDItem;
import com.spider.processor.priceProcessor.GetJDPrice;
import com.spider.processor.priceProcessor.GetPrice;
import jdk.nashorn.internal.scripts.JD;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.io.IOException;

import static com.hbaseapi.java.java.HBaseDDL.*;
import static com.hbaseapi.java.java.HBaseDML.*;

/**
 * @ClassName: ItemClient
 * @Description: TODO
 * @author: zjh
 * @date: 2022/8/20  17:31
 * @Version: 1.0
 */
public class JDItemClient {

    GetItem itemspider = new GetJDItem();
    GetPrice priceSpider = new GetJDPrice();

    /**
    *@Description: 根据调度获得的json信息进一步执行爬虫，获取信息
    *@Param: [searchItemInfo]
    *@return: void
    */
    public void getInfoByItem(String searchItemInfo){
        //爬取到的信息
        SearchItemInfo info = JSON.parseObject(searchItemInfo, SearchItemInfo.class);
        Item item = new Item(info);
        item.setTitle(itemspider.getItemInfo(item.getUrl()));
        item.setPrice(Double.parseDouble(priceSpider.getPrice(item.getSku())));
        //System.out.println("爬取信息如下："+item);

        //存储数据
        String namespace = "JDProducts", tableName = "Phones";
        String RowKey = item.getSku();

        try {
            if(!namespaceExists(namespace)){
                createNamespace(namespace);
            }

            if(!isTableExists(namespace,tableName)){
                createTable(namespace,tableName,"info1","info2");
            }
            //插入数据
            //参数不能为null,否则无法插入，也无需插入
            if(item.getSpu()!= null){
                putCell(namespace,tableName,RowKey,"info1","SPU",item.getSpu());
            }

            if(item.getTitle()!= null){
                putCell(namespace,tableName,RowKey,"info1","Title",item.getTitle());
            }

            if(item.getPrice()!= null){
                putCell(namespace,tableName,RowKey,"info1","Price",item.getPrice().toString());
            }
            if(item.getPic()!= null){
                putCell(namespace,tableName,RowKey,"info1","PicUrl",item.getPic());
            }
            if(item.getUrl()!= null){
                putCell(namespace,tableName,RowKey,"info1","Url",item.getUrl());
            }

            if(item.getCreateTime()!= null){
                putCell(namespace,tableName,RowKey,"info2","CreateTime",item.getCreateTime().toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }

        //检验是否已存储
        String[] colNameList = new String[]{"SPU","Title","Price","PicUrl","Url"};
        for(String columnName : colNameList){
            Cell cells[] = new Cell[0];
            try {
                cells = getCells(namespace,tableName,RowKey,"info1",columnName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            println(cells);

        }
        try {
            println(getCells(namespace,tableName,RowKey,"info2","CreateTime"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
