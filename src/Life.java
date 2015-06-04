import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.*;

public class Life{
    public List<Character> characters;
    public List<Event> events;
    public int miscEvents;
    public Life(){
        characters = new ArrayList<Character>();
        characters.add(new Character());
        events = new ArrayList<Event>();
        miscEvents = 0;
    }
    /*---------------------
             LOADING
     ----------------------*/

    public Event load(String id) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getID().equals(id)) {
                return events.get(i);
            }
        }
        return loadFromFile(id);
    }
    public Event loadFromFile(String id){
        Scanner file;
        File choices;
        Event event = null;
        try {
            choices = new File("events.txt");
            file = new Scanner(choices);
        } catch (Exception e) {
            System.out.println("No file found");
            return null;
        }
        while (file.hasNextLine()) {
            String l = file.nextLine();
            l = l.replaceAll("^\\s+|\\s+$", "");
            if (l.contains(id)) {
                event = new Event(l);
                events.add(event);
                addOptions(file, event.getOptions());
                break;
            }
        }
        return event;
    }
    private String addOptions(Scanner file, ArrayList<Option> options){
        while(file.hasNextLine()){
            String o = file.nextLine();
            o = o.replaceAll("^\\s+|\\s+$", "");
            if (o.contains("{}")) {
                Event misc = new Event("{" + (miscEvents += 1) + "}" + o.substring(o.indexOf('}') + 1));
                for (int i = 0; i < options.size(); i++)
                   if (options.get(i).getPointer() == null)
                        options.get(i).setPointer(misc);
                addOptions(file, misc.getOptions());
                events.add(misc);
                return o;
            } else if (o.contains("{"))
                return o; //return first line which is not an option
            if (o.length() > 0 && !o.equals("")) {
                if (!o.contains("\""))
                    o = o.substring(0, o.indexOf(')') + 1) + "\"\"" + o;
                options.add(new Option(o));
            }
        }
        return null;
    }

    /*---------------------
              IO
     ----------------------*/

    public void out(String out){
        System.out.print(out);
    }
    public int decide(String[] choices){
        int choice;
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].length() == 0)
                choices[i] = "Continue...";
            out("\n[" + (i + 1) + "]" + choices[i]);
        }
        do{
            choice = (new Scanner(System.in)).nextInt() - 1;
            if (choice == 100) {
                for (Attributes.Attribute a : characters.get(0).getAttributes().getAttributes())
                    System.out.print(a.getName() + ": " + a.getValue() + "\n");
                choice = -1;
            }
        }while (choice == -1);
        return choice;
    }

    /*---------------------
             Init
     ----------------------*/

    public void start(){

        load("{birth}");
        int i = 0;
        for (Event e: events)
            for (Option o: e.getOptions())
                if (o.getPointer() == null) {
                    if (!o.getPointerID().equals("death"))
                        out("\n" + o.getPointerID());
                    i++;
                }
        out("" + events.size());
        events.get(0).execute();
        out("\nYou die, sad, and alone.");

    }
}