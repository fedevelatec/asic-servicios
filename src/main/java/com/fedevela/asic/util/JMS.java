package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.io.Serializable;
import javax.jms.JMSException;
import org.springframework.jms.core.JmsTemplate;

public class JMS {

    public static String sendMessage(final Serializable message, JmsTemplate jmsTemplate) throws JMSException {
        MessageCreatorImpl mci = new MessageCreatorImpl(message);
        jmsTemplate.send(mci);
        return mci.getMessageID();
    }
}
