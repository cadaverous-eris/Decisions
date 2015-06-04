/**
 * Created by BLUE on 22.05.15.
 */
public class Option{
    Event event;
    Event pointer;
    String pointerID;
    public void setPointer(Event pointer) {
        this.pointer = pointer;
    }

    public void setExecutable(String executable) {
        this.event = new Event(executable);
    }

    public Option(String executable) {
        String nextID = executable.substring(Math.max(Math.max(Math.max(executable.lastIndexOf(']') + 1, executable.lastIndexOf('>') + 1), executable.lastIndexOf('"') + 1), executable.lastIndexOf(')') + 1));
        executable = executable + "#ID#";
        pointerID = nextID;
        event = new Event(executable.replace(nextID + "#ID#", ""));
        if (nextID.length() > 0) {
            pointer = Decisions.life.load("{" + nextID + "}");
        }
    }
    public String getPointerID() {
        return pointerID;
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
