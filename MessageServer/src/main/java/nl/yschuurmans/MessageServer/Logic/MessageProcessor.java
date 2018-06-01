package nl.yschuurmans.MessageServer.Logic;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.yschuurmans.MessageServer.domain.Envelope;
import nl.yschuurmans.MessageServer.domain.Message;
import nl.yschuurmans.MessageServer.kafka.sender.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageProcessor {


    @Autowired
    KafkaSender sender;

    public void ProcessEnvelope(Envelope envelope) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String messageJson = envelope.getMessage();
            Message message = mapper.readValue(messageJson, Message.class);

            SendMessageConfirmation(message);

            String returnMessageJson = mapper.writeValueAsString(message);
            Envelope returnEnvelope = new Envelope(message.getTarget(), returnMessageJson);
            String returnEnvelopeJson = mapper.writeValueAsString(returnEnvelope);

            sender.send(returnEnvelopeJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendMessageConfirmation(Message msg) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message toConfirm = new Message();
            toConfirm.setMessageId(msg.getMessageId());
            toConfirm.setMessageContent("confirm");
            String toConfirmJson = mapper.writeValueAsString(toConfirm);

            Envelope toConfirmEnvelope = new Envelope(msg.getSender(), toConfirmJson);
            String toConfirmEnvelopeJson = mapper.writeValueAsString(toConfirmEnvelope);
            sender.send(toConfirmEnvelopeJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
