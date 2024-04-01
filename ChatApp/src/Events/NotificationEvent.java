package Events;
import javafx.event.EventType;
import javafx.event.Event;
import model.MessageTo;
import model.UserInformation;


public class NotificationEvent extends Event {
    public static final EventType<NotificationEvent> NOTIFICATION_EVENT_TYPE = new EventType<>("NOTIFICATION");

    private final UserInformation sender;
    private final MessageTo message;

    public NotificationEvent(UserInformation sender, MessageTo message) {
        super(NOTIFICATION_EVENT_TYPE);
        this.sender = sender;
        this.message = message;
    }

    public UserInformation getSender() {
        return sender;
    }

    public MessageTo getMessage() {
        return message;
    }
}