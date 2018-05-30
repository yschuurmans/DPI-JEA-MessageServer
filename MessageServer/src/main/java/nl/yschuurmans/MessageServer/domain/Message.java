package nl.yschuurmans.MessageServer.domain;

public class Message {
    private String messageId;
    private String message;
    private String target;
    private String sender;


    public Message(String messageId, String target, String message) {
        this.messageId = messageId;
        this.target = target;
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
