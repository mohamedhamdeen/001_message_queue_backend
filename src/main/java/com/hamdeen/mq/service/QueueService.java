package com.hamdeen.mq.service;

import com.hamdeen.mq.dto.QueueConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    @Autowired
    private ActiveMQService active;

    @Autowired
    private IbmMQService ibm;

    @Autowired
    private RedHatAMQService redHat;

    public void SendMessage(QueueConfiguration config, String correlationId, String msg) throws Exception {

        switch (config.getBrokerName()) {

            case "active_mq":
                active.Send(config, correlationId, msg);
                break;

            case "ibm_mq":
                ibm.Send(config, correlationId, msg);
                break;

            case "redhat_amq":
                redHat.Send(config, correlationId, msg);
                break;
        }
    }

    public String Receive(QueueConfiguration config, String correlationIdReceive) throws Exception {
        String receivedMsg = null;
        String selector = "JMSCorrelationID='" + correlationIdReceive + "'";

        switch (config.getBrokerName()) {

            case "active_mq":
                receivedMsg = active.Receive(config, selector);
                break;

            case "ibm_mq":
                receivedMsg = ibm.Receive(config, selector);
                break;

            case "redhat_amq":
                receivedMsg = redHat.Receive(config, selector);
                break;
        }
        return receivedMsg;
    }
}
