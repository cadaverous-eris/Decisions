import java.util.ArrayList;

/**
 * Created by BLUE on 21.05.15.
 */
public class Event {
    ArrayList<Event> options;
    String executeable;

    public Event(String executeable) {
        this.options = options;
        this.executeable = executeable;
    }

    public ArrayList<Event> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Event> options) {
        this.options = options;
    }

    public String getExecuteable() {
        return executeable;
    }

    public void setExecuteable(String executeable) {
        this.executeable = executeable;
    }
}
