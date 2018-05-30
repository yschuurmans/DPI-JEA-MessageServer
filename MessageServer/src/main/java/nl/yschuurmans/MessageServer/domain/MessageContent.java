package nl.yschuurmans.MessageServer.domain;

public class MessageContent {
    private String messageText;

    public MessageContent() {
    }

    public MessageContent(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
