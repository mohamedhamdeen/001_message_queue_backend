package com.hamdeen.mq.service;

import com.hamdeen.mq.dto.QueueConfiguration;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class ActiveMQService {

    private String receivedMsg = null;

    public void Send(QueueConfiguration config, String correlationId, String msg) throws Exception {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(config.getUrl());
        factory.setUserName(config.getUsername());
        factory.setPassword(config.getPassword());
        JmsTemplate template = new JmsTemplate();
        template.setDefaultDestinationName(config.getQueueName());
        template.setConnectionFactory(factory);
        template.convertAndSend(msg, m -> {
            m.setJMSCorrelationID(correlationId);
            return m;

        });
    }

    public String Receive(QueueConfiguration config, String selector) throws Exception {
        AtomicBoolean result = new AtomicBoolean(true);
        Connection connection = null;

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(config.getUrl());
        factory.setUserName(config.getUsername());
        factory.setPassword(config.getPassword());
        connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(config.getQueueName());
        MessageConsumer consumer = session.createConsumer(queue, selector);
        consumer.setMessageListener(new messageListener(result));
        Thread.sleep(2000);
        session.close();

        return receivedMsg;
    }

    class messageListener implements MessageListener {
        private AtomicBoolean result;

        messageListener(AtomicBoolean result) {
            this.result = result;
        }

        @Override
        public void onMessage(Message msg) {
            TextMessage textMessage = (TextMessage) msg;
            try {
                receivedMsg = textMessage.getText();
            } catch (JMSException e) {
                e.printStackTrace();
                result.set(false);
            }

        }

    }
}
