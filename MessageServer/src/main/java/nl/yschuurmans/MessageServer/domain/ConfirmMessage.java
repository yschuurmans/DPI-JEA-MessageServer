package nl.yschuurmans.MessageServer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConfirmMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String messageGuid;
    private String sender;
    private Message message;
    private long lastUpdateTime;
    private String updateReserve;

    public ConfirmMessage() {
    }

    public ConfirmMessage(String messageGuid, String sender, Message message) {
        this.messageGuid = messageGuid;
        this.message = message;
        this.sender = sender;
        this.lastUpdateTime = System.currentTimeMillis();
        this.updateReserve = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageGuid() {
        return messageGuid;
    }

    public void setMessageGuid(String messageGuid) {
        this.messageGuid = messageGuid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUpdateReserve() {
        return updateReserve;
    }

    public void setUpdateReserve(String updateReserve) {
        this.updateReserve = updateReserve;
    }
}
