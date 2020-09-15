package com.hamdeen.mq.service;

import com.hamdeen.mq.dto.QueueConfiguration;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.stereotype.Service;

import javax.jms.*;

@Service
public class IbmMQService {

    public void Send(QueueConfiguration config, String correlationId, String msg) throws Exception {

        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName(config.getHost());
        connectionFactory.setPort(config.getPort());
        connectionFactory.setTransportType(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
        connectionFactory.setQueueManager(config.getQueueManager());
        connectionFactory.setChannel(config.getChannel());
        QueueConnection queueConn = connectionFactory.createQueueConnection(config.getUsername(), config.getPassword());
        QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender queueSender = queueSession.createSender(queueSession.createQueue(config.getQueueName()));
        queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = queueSession.createTextMessage(msg);
        message.setJMSCorrelationID(correlationId);
        queueSender.send(message);
        queueConn.close();

    }

    public String Receive(QueueConfiguration config, String selector) throws Exception {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setHostName(config.getHost());
        connectionFactory.setPort(config.getPort());
        connectionFactory.setTransportType(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
        connectionFactory.setQueueManager(config.getQueueManager());
        connectionFactory.setChannel(config.getChannel());
        QueueConnection queueConn = connectionFactory.createQueueConnection(config.getUsername(), config.getPassword());
        queueConn.start();
        QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = queueSession.createQueue(config.getQueueName());
        MessageConsumer consumer = queueSession.createConsumer(queue, selector);
        TextMessage textMessage = (TextMessage) consumer.receive(5000);
        String receivedMsg = textMessage.getText();
        queueSession.close();

        return receivedMsg;
    }
}
