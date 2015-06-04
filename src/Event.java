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

    /*---------------------------
              PARSING
     ----------------------------*/

    public void execute(){
        String str = executable;
        if (str.length() == 0)
            return;
        if (str.charAt(0) == '>')
            str = str.substring(1);
        if (str.charAt(0) == '(')
            str = str.substring(str.lastIndexOf(')') + 1);
        while((str = modParser(outParser(priorityParser(str)))).length() > 0) ;
        Event e = optionParser(this);
        if (e != null)
                e.execute();
    }

    public Event optionParser(Event event){
        if (event.getOptions().size() == 0)
            return null;
        for (int j = 0; j < event.getOptions().size(); j++)
            if (event.getOptions().get(j).getExecutable().charAt(0) == '(')
                event.getOptions().add(0, event.getOptions().remove(j));
        int i = 0;
        int c = 0;
        ArrayList<String> restore = new ArrayList<String>();
        for (Option o: event.getOptions())
            restore.add(o.getExecutable());
        for (Option o: event.getOptions()) {
            if (o.getExecutable().length() > 0 && o.getExecutable().charAt(0) == '>') {
                i++;
                o.setExecutable(o.getExecutable().substring(1));
                if (conditionalParser(o.getExecutable())) {
                    String[] force = {""};
                    Decisions.life.decide(force);
                    (o.getEvent()).execute();
                    return o.getPointer();
                }
            }
        }
        ArrayList<String> options = new ArrayList<String>();
        for (int j = i; j < event.getOptions().size(); j++) { // set to start at i,
            if (event.getOptions().get(j).getExecutable().length() > 0 && conditionalParser(event.getOptions().get(j).getExecutable())) {
                options.add(event.getOptions().get(j).getExecutable().substring(event.getOptions().get(j).getExecutable().indexOf('"') + 1, event.getOptions().get(j).getExecutable().lastIndexOf('"')));
                event.getOptions().get(j).setExecutable(event.getOptions().get(j).getExecutable().replace(options.get(options.size() - 1), ""));
            }else if (event.getOptions().get(j).getExecutable().length() > 0){
                c++;
            }
        }
        String[] decisions = new String[options.size()];
        decisions = options.toArray(decisions);
        Option o = event.getOptions().get(Decisions.life.decide(decisions) + i + c);
        o.getEvent().execute();
        for (int j = 0; j < event.getOptions().size(); j++)
            event.getOptions().get(j).setExecutable(restore.get(j));
        return o.getPointer();
    }

    public String priorityParser(String event){
        if (event.length() > 0 && event.charAt(0)=='<'){
            int i = 0;
            int f = 0;
            if (event.toLowerCase().charAt(1)=='f')
                f = Integer.parseInt(event.substring(event.indexOf(' ') + 1, event.indexOf('>')));
            if (event.toLowerCase().charAt(1)=='r')
                f = new Random().nextInt(options.size());
            while(options.get(i).getExecutable().charAt(0) == '>')
                i++;
            if (f < getOptions().size()) {
                options.get(f).setExecutable(">" + options.get(f).getExecutable());
                options.add(i, options.remove(f));
            }
            event = (event.substring(event.indexOf('>') + 1));
        }
        return event;
    }
    public String outParser(String event){
        if (event.length() > 0 && event.charAt(0) == '"' && event.indexOf('"', 1) != -1) {
            Decisions.life.out(event.substring(1, event.indexOf('"', 1)));
            event = (event.substring(event.indexOf('"', 1) + 1));
        }
        return event;
    }
    public String modParser(String event){//take first attribute as toMod, set = to tokenized string using javascript

        if (event.length() > 0 && event.charAt(0) == '['){
            String mod;
            int toMod = -1;
            mod = event.substring(1, event.indexOf(']')).toLowerCase();
            event = (event.substring(event.indexOf(']') + 1));
            for  (int i = 0; i < Decisions.life.characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (mod.contains(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toMod = i;
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
        if (event.length() > 0 && event.charAt(0) == '('){
            String check;
            int toCheck = -1;
            check = event.substring(1, event.indexOf(')')).toLowerCase();
            event = (event.substring(event.indexOf(')') + 1));
            for  (int i = 0; i < Decisions.life.characters.get(0).getAttributes().getAttributes().size(); i++) {
                if (check.contains(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName()))
                    toCheck = Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue();
                check = check.replaceAll(Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getName(), "" + Decisions.life.characters.get(0).getAttributes().getAttributes().get(i).getValue());
            }
            if (toCheck != -1){
                String[] ints = check.split("\\s+");
                if (Integer.parseInt(ints[1]) <= Integer.parseInt(ints[0]) && Integer.parseInt(ints[0]) <= Integer.parseInt(ints[2]))
                    return true;
                return false;
            }else
                Decisions.life.out("That's not an attribute");
        }
        return true;
    }

}
