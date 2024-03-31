package classes;
public class FriendRequest {
    private String uuidSender;
    private String uuidReceiver;
    private String requestStatus;
    private String request_date;

    public FriendRequest(String uuidSender, String uuidReceiver, String requestStatus, String request_date) {
        this.uuidSender = uuidSender;
        this.uuidReceiver = uuidReceiver;
        this.requestStatus = requestStatus;
        this.request_date = request_date;
    }

    public FriendRequest() {
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getUuidReceiver() {
        return uuidReceiver;
    }

    public String getUuidSender() {
        return uuidSender;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public void setUuidReceiver(String uuidReceiver) {
        this.uuidReceiver = uuidReceiver;
    }

    public void setUuidSender(String uuidSender) {
        this.uuidSender = uuidSender;
    }

    public String toString() {
        return "FriendRequest{" +
                "uuidSender='" + uuidSender + '\'' +
                ", uuidReceiver='" + uuidReceiver + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", request_date='" + request_date + '\'' +
                '}';
    }
}
