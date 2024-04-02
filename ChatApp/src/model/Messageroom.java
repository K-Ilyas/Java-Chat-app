package model;

import java.io.Serializable;

public class MessageRoom implements Serializable{
    private String uuidRoom;
    private String uuidUser;
    private String message;
    private String messageDate;
    private boolean isDeleted;

    // Add getters and setters for the above variables

    public MessageRoom() {
        // Initialize any variables or perform any necessary setup here
    }

    public MessageRoom( String uuidRoom, String uuidUser, String message, String messageDate,
            boolean isDeleted) {
        this.uuidRoom = uuidRoom;
        this.uuidUser = uuidUser;
        this.message = message;
        this.messageDate = messageDate;
        this.isDeleted = isDeleted;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getUuidRoom() {
        return uuidRoom;
    }

    public String getUuidUser() {
        return uuidUser;
    }
    
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }


    public void setUuidRoom(String uuidRoom) {
        this.uuidRoom = uuidRoom;
    }

    public void setUuidUser(String uuidUser) {
        this.uuidUser = uuidUser;
    }

}
