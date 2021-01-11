package com.example.rabbitmq.Controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ConnectionUtil {
      private final static String QUEUE_NAME="rabmq.test";
    /**
     * 获取连接
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        //设置vhost
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("admin");
        //通过工厂获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

    //创建队列，发送消息
//    public static void main(String[] args) throws Exception {
//        //获取连接
//        Connection connection = ConnectionUtil.getConnection();
//        //创建通道
//        Channel channel = connection.createChannel();
//        //声明创建队列
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
//        //消息内容
//        String message = "Hello World!";
//        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
//        System.out.println("发送消息："+message);
//        //关闭连接和通道
//        channel.close();
//        connection.close();
//    }

    //消费者消费消息
    public static void main(String[] args) throws Exception {
        //获取连接和通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明通道
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);

        while(true){
            //这个方法会阻塞住，直到获取到消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("接收到消息："+message);
        }
    }
}
