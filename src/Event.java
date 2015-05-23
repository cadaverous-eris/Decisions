import java.util.ArrayList;

/**
 * Created by BLUE on 21.05.15.
 */

public class Event {
    /*---------------------
            VARIABLES
     ----------------------*/
    ArrayList<Option> options;
    String id;
    String executeable;
    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }
    public ArrayList<Option> getOptions() {
        return options;
    }
    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
    public String getExecuteable() {
        return executeable;
    }
    public void setExecuteable(String executeable) {
        this.executeable = executeable;
    }

    /*---------------------------
            INITIALIZATION
     ----------------------------*/

    public Event(String init) {
        id = init.substring(0, init.indexOf('}') + 1);
        System.out.println("EVENT ID: " + id);
        executeable = init.substring(init.indexOf('}') + 1);
        options = new ArrayList<Option>();
    }

}
