package nl.yschuurmans.MessageServer.domain;

public class Envelope {
    private String sender;
    private String target;
    private String message;

    public Envelope() {
    }

    public Envelope(String sender, String target, String message) {
        this.sender = sender;
        this.target = target;
        this.message = message;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
