package com.fedevela.asic.util;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.jms.core.MessageCreator;

public class MessageCreatorImpl implements MessageCreator, Serializable {

    private Serializable srlMessage = null;
    private Message message = null;

    public MessageCreatorImpl(Serializable srlMessage) {
        this.srlMessage = srlMessage;
    }

    @Override
    public Message createMessage(Session sn) throws JMSException {
        if (srlMessage instanceof java.lang.String) {
            message = sn.createTextMessage(TypeCast.toString(srlMessage));
        } else {
            message = sn.createObjectMessage(srlMessage);
        }
        return message;
    }

    public String getMessageID() throws JMSException {
        return message.getJMSMessageID();
    }
}
