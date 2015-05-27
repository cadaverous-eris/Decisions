import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by BLUE on 21.05.15.
 */

public class Event {
    /*---------------------
            VARIABLES
     ----------------------*/
    private ArrayList<Option> options;
    private String id;
    private String executable;
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
    public String getExecutable() {
        return executable;
    }
    public void setExecutable(String executeable) {
        this.executable = executeable;
    }

    /*---------------------------
            INITIALIZATION
     ----------------------------*/

    public Event(String init) {
        id = init.substring(0, init.indexOf('}') + 1);
        executable = init.substring(init.indexOf('}') + 1);
        options = new ArrayList<Option>();
    }

    public void execute(){
        Decisions.life.out("Execute: " + executable);
        while(modParser(outParser(priorityParser(executable))).length() > 0) Decisions.life.out("\nMain exec: " + this.getExecutable());
        optionParser(this).execute();
    }

    public Event optionParser(Event event){
        Decisions.life.out("Option: " + event);
        int i = 0;
        for (Option o: event.getOptions()) {
            if (o.getExecutable().length() > 0 && o.getExecutable().charAt(0) == '>') {
                i++;
                o.setExecutable(o.getExecutable().substring(1));
                if (conditionalParser(o.getEvent().getExecutable())) {
                    (o.getEvent()).execute();
                    (o.getPointer()).execute();
                }
            }
        }
        ArrayList<String> options = new ArrayList<String>();
        options.add("");
        for (Option o: event.getOptions())
            if (o.getExecutable().length() > 0 && conditionalParser(o.getExecutable())) {
                //options.add(new Integer(8));
                options.add(o.getExecutable().substring(o.getExecutable().indexOf('"'), o.getExecutable().lastIndexOf('"')));
                o.setExecutable(o.getExecutable().replace(options.get(options.size() - 1), ""));
            }
        String[] decisions = new String[options.size()];
        decisions = (String[]) options.toArray(decisions);
        Option o = event.getOptions().get(Decisions.life.decide(decisions));
        o.getEvent().execute();
        return o.getPointer();
    }

    public String priorityParser(String event){
        Decisions.life.out("\nPrio: " + event);
        if (event.length() > 0 && event.charAt(0)=='<'){
            int i = 0;
            int f = 0;
            if (event.toLowerCase().charAt(1)=='f')
                f = Integer.parseInt(event.substring(event.indexOf(' ') + 1, event.indexOf('>')));
            if (event.toLowerCase().charAt(1)=='r')
                f = new Random().nextInt(options.size());
            while(options.get(f).getExecutable().charAt(0) == '>')
                i++;
            options.get(f).setExecutable(">" + options.get(f).getExecutable());
            if (f < getOptions().size())
                options.add(i, options.remove(f));
            event = (event.substring(event.indexOf('>') + 1));
        }
        return event;
    }
    public String outParser(String event){
        Decisions.life.out("\nOut: " + event);
        if (event.length() > 0 && event.charAt(0) == '"') {
            Decisions.life.out(event.substring(1, event.indexOf('"', 1)));
            event = (event.substring(event.indexOf('"', 1)));
        }
        return event;
    }
    public String modParser(String event){//take first attribute as toMod, set = to tokenized string using javascript
        Decisions.life.out("\nMod: " + event);
        if (event.length() > 0 && event.charAt(0) == '['){
            String mod;
            int toMod = -1;
            Decisions.life.out(mod = event.substring(1, event.indexOf(']')).toLowerCase());
            event = (event.substring(event.indexOf(']') + 1));
            for  (int i = 0; i < Decisions.life.characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (mod.contains(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toMod = Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue();
                mod = mod.replaceAll(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName(), "" + Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue());
            }
            if (toMod != -1){
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                try{
                    Decisions.life.characters.get(0).getAttributes().getAttributes().get(toMod).setValue((Integer) engine.eval(mod));
                }catch (ScriptException e){
                    Decisions.life.out("Script Exception: Javascript: Cannot Evaluate: " + mod);
                }
            }
        }
        return event;
    }
    public Boolean conditionalParser(String event){
        Decisions.life.out("\nCon: " + event);
        if (event.length() > 0 && event.charAt(0) == '('){
            String check;
            int toCheck = -1;
            Decisions.life.out(check = event.substring(1, event.indexOf(')')).toLowerCase());
            event = (event.substring(event.indexOf(')') + 1));
            for  (int i = 0; i < Decisions.life.characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (check.contains(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toCheck = Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue();
                check = check.replaceAll(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName(), "" + Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue());
            }
            if (toCheck != -1){
                String[] ints = check.split("\\s+");
                if (Integer.parseInt(ints[1]) < Integer.parseInt(ints[0]) && Integer.parseInt(ints[0]) < Integer.parseInt(ints[2]))
                    return true;
                return false;
            }else
                Decisions.life.out("That's not an attribute");
        }
        return true;
    }

}
