/**
 * Created by BLUE on 22.05.15.
 */
public class Option{
    Event event;
    Event pointer;

    public void setPointer(Event pointer) {
        this.pointer = pointer;
    }

    public void setExecutable(String executable) {
        this.event = new Event(executable);
    }

    public Option(String executable) {
        String nextID = executable.substring(Math.max(Math.max(Math.max(executable.lastIndexOf(']') + 1, executable.lastIndexOf('>') + 1), executable.lastIndexOf('"') + 1), executable.lastIndexOf(')') + 1));
        event = new Event(executable.replace(nextID, ""));
        //System.out.println(event.getExecutable());
        if (nextID.length() > 0) {
            pointer = Decisions.life.load("{" + nextID + "}");
//            System.out.println(pointer.getExecutable());
        }
    }
    public Event getEvent(){
        return event;
    }
    public String getExecutable() {
        return event.getExecutable();
    }
    public Event getPointer() {
        return pointer;
    }
}
