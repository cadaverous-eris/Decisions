import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.*;

public class Life{
    List<Character> characters;
    List<Event> events;
    public Life(){
        characters = new ArrayList<Character>();
    }

    public void loader(String id) {
        //if (id.equals("death")) { System.out.println("You die sad and alone."); return; }
        //Scanner console = new Scanner(System.in);
        Scanner file;
        File choices;
        try {
            choices = new File("choices.txt");
            file = new Scanner(choices);
        } catch (Exception e) {
            System.out.println("No file found");
            return;
        }
        while (file.hasNextLine()) {
            String l =  file.nextLine();
            if (l.charAt(0) != '{') break;
            Event event = new Event(l);
            event = event.substring(event.indexOf('}') + 1);
            while(file.hasNextLine()){
                event.getOptions().add(file.nextLine());
                if (options.get(options.size() - 1).charAt(0) == '{') {
                    options.remove(options.size() - 1);
                    break;
                }
            }
            decide((String[]) options.toArray());
            //}

        }

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
        parser("birth");
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