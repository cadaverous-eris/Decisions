import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.*;

public class Life{
    public static List<Character> characters;
    public static List<Event> events;
    public static int miscEvents;
    public Life(){
        characters = new ArrayList<Character>();
        events = new ArrayList<Event>();
        miscEvents = 0;
    }
    /*---------------------
             LOADING
     ----------------------*/

    public static Event load(String id) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i) != null && events.get(i).getID().equals(id)) {
                return events.get(i);

            }
        }
        return loadFromFile(id);
    }
    public static Event loadFromFile(String id){
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
            if (l.contains(id)) {
                event = new Event(l);
                events.add(event);
                addOptions(file, event.getOptions());
                break;
            }
        }
        return event;
    }
    private static String addOptions(Scanner file, ArrayList<Option> options){
        while(file.hasNextLine()){
            String o = file.nextLine();
            o = o.replaceAll("^\\s+|\\s+$", "");
            if (o.contains("{}")) {
                Event misc = new Event("{" + (miscEvents += 1) + "}" + o.substring(o.indexOf('}') + 1));
                events.add(misc);
                for (int i = 0; i < options.size(); i++) options.get(i).setPointer(misc);
                addOptions(file, misc.getOptions());
            } else if (o.contains("{"))
                return o; //return line which is not an option
            if (o.length() > 0)
                options.add(new Option(o));
        }
        return null;
    }

    /*---------------------
            PARSING
     ----------------------*/

    public Event eventParser(Event event){//make void
        while(event.getExecutable().length() > 0)
            modParser(outParser(priorityParser(event)));
        return event;
    }
    public Event optionParser(Event event){
        int i = 0;
        for (Option o: event.getOptions()) {
            if (o.getExecutable().charAt(0) == '>') {
                i++;
                if (conditionalParser(o.getEvent())) {
                    eventParser(o.getEvent());
                    eventParser(o.getPointer());
                }
            }
        }
        ArrayList<String> options = new ArrayList<String>();
        for (Option o: event.getOptions())
            if (conditionalParser(o.getEvent())) {
                options.add(o.getExecutable().substring(o.getExecutable().indexOf('"'), o.getExecutable().lastIndexOf('"')));
                o.setExecutable(o.getExecutable().replace(options.get(options.size() - 1), ""));
            }
        Option o = event.getOptions().get(decide((String[]) options.toArray()));
        eventParser(o.getEvent());
        eventParser(o.getPointer());
        return event;
    }

    public Event priorityParser(Event event){
        if (event.getExecutable().charAt(0)=='<'){
            if (event.getExecutable().toLowerCase().charAt(1)=='f') {//set importance
                int i = 0;
                int f = Integer.parseInt(event.getExecutable().substring(event.getExecutable().indexOf(' ') + 1, event.getExecutable().indexOf('>')));
                while(event.getOptions().get(i).getExecutable().charAt(0) == '>') i++;
                if (f < event.getOptions().size())
                    event.getOptions().get(f).setExecutable(">" + event.getOptions().get(i).getExecutable());
            }
            if (event.getExecutable().toLowerCase().charAt(1)=='r') {
                Option r = event.getOptions().get(new Random().nextInt(event.getOptions().size()));
                event.getOptions().clear();
                event.getOptions().add(r);
            }
            event.setExecutable(event.getExecutable().substring(event.getExecutable().indexOf('>')));
        }
        return event;
    }
    public Event outParser(Event event){
        if (event.getExecutable().charAt(0) == '"') {
            out(event.getExecutable().substring(1, event.getExecutable().indexOf('"', 1)));
            event.setExecutable(event.getExecutable().substring(1, event.getExecutable().indexOf('"', 1)));
        }
        return event;
    }
    public Event modParser(Event event){//take first attribute as toMod, set = to tokenized string using javascript
        if (event.getExecutable().charAt(0) == '['){
            String mod;
            int toMod = -1;
            out(mod = event.getExecutable().substring(1, event.getExecutable().indexOf(']')).toLowerCase());
            event.setExecutable(event.getExecutable().substring(event.getExecutable().indexOf(']') + 1));
            for  (int i = 0; i < characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (mod.contains(characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toMod = characters.get(0).getAttributes().getAttributes().get(i).getValue();
                mod = mod.replaceAll(characters.get(0).getAttributes().getAttributes().get(i).getName(), "" + characters.get(0).getAttributes().getAttributes().get(i).getValue());
            }
            if (toMod != -1){
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                try{
                    characters.get(0).getAttributes().getAttributes().get(toMod).setValue((Integer) engine.eval(mod));
                }catch (ScriptException e){
                    out("Script Exception: Javascript: Cannot Evaluate: " + mod);
                }
            }
        }
        return event;
    }
    public Boolean conditionalParser(Event event){
        if (event.getExecutable().charAt(0) == '('){
            String check;
            int toCheck = -1;
            out(check = event.getExecutable().substring(1, event.getExecutable().indexOf(')')).toLowerCase());
            event.setExecutable(event.getExecutable().substring(event.getExecutable().indexOf(')') + 1));
            for  (int i = 0; i < characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (check.contains(characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toCheck = characters.get(0).getAttributes().getAttributes().get(i).getValue();
                check = check.replaceAll(characters.get(0).getAttributes().getAttributes().get(i).getName(), "" + characters.get(0).getAttributes().getAttributes().get(i).getValue());
            }
            if (toCheck != -1){
                String[] ints = check.split("\\s+");
                if (Integer.parseInt(ints[1]) < Integer.parseInt(ints[0]) && Integer.parseInt(ints[0]) < Integer.parseInt(ints[2]))
                    return true;
                return false;
            }else
                out("That's not an attribute");
        }
        return true;
    }

    /*---------------------
              IO
     ----------------------*/

    private void out(String out){
        System.out.print(out);
    }
    private int decide(String[] choices){
        int choice;
            for (int i = 0; i < choices.length; i++)
                out("[" + (i + 1) + "]" + choices[i]);
        do{
            choice = (new Scanner(System.in)).nextInt() - 1;
        }while (choice == -1);
        return choice;
    }

    public void start(){
        eventParser(load("{birth}"));
    }
/*
    private void birth(){
        characters.add(new Character());
        if (characters.get(0).getLuck() > 0){
            out("\nYou are born, it is a rather unremarkable affair, and you, are a rather unremarkable baby.\n");
            childhood();
        }
        out("\nYou are still born");
        death();
        //TODO: siamese twin
    }
    private void childhood(){
        out("As you grow up, your life remains relatively dull.\n\n");
        out("In middle school you wake up every morning and eat:\n\n");
        switch(decide(("Cereal\n,Eggs and Bacon\n,Oatmeal\n").split(", ?"))){
            case 0:
                characters.get(0).setWeight(characters.get(0).getWeight() + 20);
                break;
            case 1:
                characters.get(0).setWeight(characters.get(0).getWeight() + 5);
                characters.get(0).setEndurance(characters.get(0).getEndurance() + 1);
                break;
            default:
                break;
        }
        out("Then you always:\n\n");
        switch(decide(("Just go to school\n,Cut and go hang out\n").split(", ?"))){
            case 0:
                characters.get(0).setWeight(characters.get(0).getWeight() + 20);
                break;
            case 1:
                characters.get(0).setWeight(characters.get(0).getWeight() + 5);
                characters.get(0).setEndurance(characters.get(0).getEndurance() + 1);
                break;
            default:
                break;
        }
    }
    private void death(){
        out("You die sad and alone.");
    }

*/}