/**
 * Created by BLUE on 22.05.15.
 */
public class Option{
    String executable;
    Event pointer;

    public void setPointer(Event pointer) {
        this.pointer = pointer;
    }

    public Option(String executable) {
        String nextID = executable.substring(Math.max(Math.max(Math.max(executable.lastIndexOf(']') + 1, executable.lastIndexOf('>') + 1), executable.lastIndexOf('"') + 1), executable.lastIndexOf(')') + 1));
        System.out.println("Find: " + nextID);
        this.executable = executable.replace(nextID, "");
        if (nextID.length() > 0) {
            pointer = Life.load("{" + nextID + "}");
//            System.out.println(pointer.getExecuteable());
        }
    }
    public String getEvent() {
        return executable;
    }
    public Event getPointer() {
        return pointer;
    }
}
