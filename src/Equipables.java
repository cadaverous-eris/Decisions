
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by BLUE on 20.05.15.
 */
public class Equipables {
    public class Equipable{
        private String modifiers;
        private String name;
        private int quantity;
        public Equipable(String modifiers, String name){
            this.name = name;
            this.modifiers = modifiers;
            quantity = 0;
        }

        public String getModifiers() {
            return modifiers;
        }
        public void setModifiers(String modifiers) {
                this.modifiers = modifiers;
            }
        public String getName() {
                return name;
            }
        public void setName(String name) {
                this.name = name;
            }
        public int getQuantity() {
                return quantity;
            }
        public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
    }
    List<Equipable> equipables;
    public Equipables(){
        equipables = new ArrayList<Equipable>();
        Scanner file;
        File attributes;
        try {
            attributes = new File("attributes.txt");
            file = new Scanner(attributes);
        } catch (Exception e) {
            System.out.println("No file found");
            return;
        }
        while (file.hasNextLine()) {
            String l = file.nextLine();
            this.equipables.add(new Equipable(l.substring(l.indexOf(":") + 1),l.substring(0, l.indexOf(":"))));
        }
    }
}


