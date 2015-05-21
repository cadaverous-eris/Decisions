import java.io.File;
import java.util.*;

/**
 * Created by BLUE on 20.05.15.
 */
public class Attributes {
    public class Attribute{
        private String name;
        private int value;
        public Attribute(String name){
            this.name = name;
            value = 0;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
    }
    List<Attribute> attributes;
    public Attributes(){
        attributes = new ArrayList<Attribute>();
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
            this.attributes.add(new Attribute(file.nextLine()));
        }
    }
}
