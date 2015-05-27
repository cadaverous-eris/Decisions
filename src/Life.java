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
        //out(id + "\n");
        while (file.hasNextLine()) {
            String l = file.nextLine();
            l = l.replaceAll("^\\s+|\\s+$", "");
            if (l.contains(id)) {
                //out(l + "\n");
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
                events.add(misc);
                for (int i = 0; i < options.size(); i++) options.get(i).setPointer(misc);
                addOptions(file, misc.getOptions());
            } else if (o.contains("{"))
                return o; //return first line which is not an option
            if (o.length() > 0)
                options.add(new Option(o));
        }
        return null;
    }

    /*---------------------
            PARSING
     ----------------------*/


    /*---------------------
              IO
     ----------------------*/

    public void out(String out){
        System.out.print(out);
    }
    public int decide(String[] choices){
        int choice;
        for (int i = 0; i < choices.length; i++)
            out("[" + (i + 1) + "]" + choices[i]);
        do{
            choice = (new Scanner(System.in)).nextInt() - 1;
        }while (choice == -1);
        return choice;
    }

    public void start(){
        load("{birth}");
        System.out.println(events.size());
        events.get(0).execute();

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