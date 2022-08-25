package com.hbaseapi.java.java;

import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

public class HBaseConnection {
    public static Connection conn = null;

   static{
      try {
        // 创建连接
        conn = HBaseUtil.getConnection();

      } catch (IOException e) {
        e.printStackTrace();
      }
   }
   public static void closeConnection(){
       if(conn != null){
           try {
               conn.close();
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
   }

/*    public static void main(String[] args) {
       //测试
        System.out.println(HBaseConnection.conn);
        //关闭连接
        HBaseConnection.closeConnection();

    }*/
}





