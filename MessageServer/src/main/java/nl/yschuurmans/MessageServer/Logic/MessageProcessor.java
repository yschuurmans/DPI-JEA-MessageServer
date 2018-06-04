package nl.yschuurmans.MessageServer.Logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.yschuurmans.MessageServer.domain.ConfirmMessage;
import nl.yschuurmans.MessageServer.domain.Envelope;
import nl.yschuurmans.MessageServer.domain.Message;
import nl.yschuurmans.MessageServer.domain.MessageContent;
import nl.yschuurmans.MessageServer.kafka.sender.KafkaSender;
import nl.yschuurmans.MessageServer.repositories.ConfirmMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MessageProcessor {

    Logger LOGGER = Logger.getLogger(MessageProcessor.class.getName());

    @Autowired
    KafkaSender sender;

    @Autowired
    ConfirmMessageRepo confirmMessageRepo;

    public void ProcessEnvelope(Envelope envelope) {
        try {

            Message message = openEnvelope(envelope);

            if (message.getMessageContent().equals("confirm")) {
                HandleConfirmMessage(message);
                return;
            }

            SendMessageConfirmation(message);

            String returnEnvelopeJson = closeEnvelope(message);

            sender.send(returnEnvelopeJson);
            awaitMessageConfirmation(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message openEnvelope(Envelope envelope) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String messageJson = envelope.getMessage();
        return mapper.readValue(messageJson, Message.class);
    }

    private String closeEnvelope(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String returnMessageJson = mapper.writeValueAsString(message);
        Envelope returnEnvelope = new Envelope(message.getTarget(), returnMessageJson);
        return mapper.writeValueAsString(returnEnvelope);
    }

    private void HandleConfirmMessage(Message confirmMessage) {
        LOGGER.log(Level.INFO, "Received Confirm message for: " + confirmMessage.getMessageId());
        removeMessageConfirmation(confirmMessage);
    }

    @Scheduled(fixedDelay = 5000)
    public void checkToResendMessages() {
        String operationGuid = UUID.randomUUID().toString();
        Iterable<ConfirmMessage> confirmMessages = confirmMessageRepo.findAll();
        List<ConfirmMessage> toCheck = new ArrayList<>();
        List<Long> toCheckIds = new ArrayList<>();
        for (ConfirmMessage confirmMessage : confirmMessages) {
            if (System.currentTimeMillis() - confirmMessage.getLastUpdateTime() > 4900) {
                toCheck.add(confirmMessage);
                confirmMessage.setUpdateReserve(operationGuid);
                toCheckIds.add(confirmMessage.getId());
            }
        }
        confirmMessageRepo.saveAll(toCheck);
        confirmMessageRepo.flush();

        if (toCheck.size() > 0) {
            Iterable<ConfirmMessage> result = confirmMessageRepo.findAllById(toCheckIds);
            for (ConfirmMessage confirmMessage : result) {
                if (!confirmMessage.getUpdateReserve().equals(operationGuid))
                    return;

                try {
                    String returnEnvelopeJson = closeEnvelope(confirmMessage.getMessage());
                    sender.send(returnEnvelopeJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void awaitMessageConfirmation(Message message) {
        ConfirmMessage confirmMessage =
                new ConfirmMessage(message.getMessageId(), message.getTarget(), message);
        confirmMessageRepo.save(confirmMessage);
    }

    public void removeMessageConfirmation(Message message) {
        Iterable<ConfirmMessage> confirmMessages = confirmMessageRepo.findAll();
        long toRemoveMessage = 0;
        for (ConfirmMessage confirmMessage : confirmMessages) {
            if (confirmMessage.getMessageGuid().equals(message.getMessageId()) &&
                    confirmMessage.getSender().equals(message.getSender())) {
                toRemoveMessage = confirmMessage.getId();
            }
        }
        confirmMessageRepo.deleteById(toRemoveMessage);
    }

    private void SendMessageConfirmation(Message msg) {
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
