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

    public static Event load(String id) {
        //if (id.equals("death")) { System.out.println("You die sad and alone."); return; }
        //Scanner console = new Scanner(System.in);
        for (int i = 0; i < events.size(); i++)
            if (events.get(i).getID().equals(id))
                return events.get(i);
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
            System.out.println("Line: " + l);
            System.out.println("ID: " + id);
            if (l.contains(id)) {
                System.out.println("FOUND: " + id);
                event = new Event(l);
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
            System.out.println("Option: " + o);
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

    public String eventParser(String event, ArrayList<String> options){//make void
        while(event.length() > 0) event = modParser(outParser(priorityParser(event, options)));
        return event;
    }
    public String optionParser(String event){return event;}
    public String priorityParser(String event, ArrayList<String> options){
        if (event.charAt(0)=='<'){
            if (event.toLowerCase().charAt(1)=='f') {//set importance
                int i = 0;
                int f = Integer.parseInt(event.substring(event.indexOf(' '), event.indexOf('>')));
                while(options.get(i).charAt(0) == '>') i++;
                options.add(i, ">" + options.remove(f));
            }
            if (event.toLowerCase().charAt(1)=='r') {
                String r = options.get(new Random().nextInt(options.size()));
                options.clear();
                options.add(r);
            }
            //if (event.toLowerCase().charAt(1)=='c') {

            //}
            event = event.substring(event.indexOf('>'));
        }
        return event;
    }
    public String outParser(String str){
        if (str.charAt(0) == '"') out(str.substring(1, str.indexOf('"', 1)));
        return str.substring(str.indexOf('"', 1));
    }
    public String modParser(String str){//take first attribute as toMod, set = to tokenized string using javascript
        if (str.charAt(0) == '['){
            String mod;
            int toMod = -1;
            out(mod = str.substring(1, str.indexOf(']')).toLowerCase());
            str = str.substring(str.indexOf(']') + 1);
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
        return str;
    }

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

    public void init(){
        load("{birth}");
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