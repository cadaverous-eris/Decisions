import java.io.File;
import java.util.*;

public class Life{
    List<Character> characters;
    public Life(){
        characters = new ArrayList<Character>();
    }

    public void parser(String id) {
        if (id.equals("death")) { System.out.println("You die sad and alone."); return; }
        Scanner console = new Scanner(System.in);
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
            String event = file.nextLine();
            if (event.contains("{" + id + "}")){
                ArrayList<String> options = new ArrayList<String>();
                event = event.substring(event.indexOf('}') + 1);
                while(file.hasNextLine()){
                	options.add(file.nextLine());
                	if (options.get(options.size() - 1).charAt(0) == '{') {
                        options.remove(options.size() - 1);
                		break;
                	}
                }

                decide((String[]) options.toArray());
            }

        }

    }

    public String eventParser(String event, ArrayList<String> options){
        if (event.charAt(0)=='('){
            if (event.toLowerCase().charAt(1)=='f') {//set importance
                int i = 0;
                int f = Integer.parseInt(event.substring(event.indexOf(' '), event.indexOf(')')));
                while(options.get(i).charAt(0) == '>') i++;
                options.add(i, ">" + options.remove(f));
            }
            if (event.toLowerCase().charAt(1)=='r') {
                String r = options.get(new Random().nextInt(options.size()));
                options.clear();
                options.add(r);
            }
            if (event.toLowerCase().charAt(1)=='c') {
                
            }
            return event.substring(event.indexOf('('),event.indexOf(')'));
        }
        if (event.charAt(0)=='"'){
            out(event = event.substring(1,event.indexOf('"', 1)));
            return event;
        }
    }
    public String optionParser(){}

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