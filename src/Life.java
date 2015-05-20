import java.util.*;

public class Life{
    List<Character> characters;
    public Life(){
        characters = new ArrayList<Character>();
    }

    private void out(String out){
        System.out.print(out);
    }
    private int decide(String[] choices){
        int choice;
        do{
            out("Your options are:");
            for (int i = 0; i < choices.length; i++)
                out(choices[i]);
            choice = (new Scanner(System.in)).nextInt();
        }while (choice == -1);
        return choice;
    }

    private void birth(){
        characters.add(new Character());
        if (characters.get(0).getLuck() > 0) out("\nYou are born, it is a rather unremarkable affair, and you, are a rather unremarkable baby.\n");
        out("\nYou are still born");
        death();
    }
    private void death(){
        out("You die sad and alone.");
    }
    private void childhood(){


    }

}