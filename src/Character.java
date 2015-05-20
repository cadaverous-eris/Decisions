import java.util.Random;

public class Character{
    String name;

    public Character(){
        Random i = new Random();
        gender      = i.nextInt(); //make even chance 0-1
        weight      = i.nextInt() * 200 + (gender - 1) * 80 + 100; //Set weight using gender, male max 200, female 220 min is 100lbs, will be altered
        beauty      = i.nextInt() * 10;
        charisma    = i.nextInt() * 5 + beauty / 2;
        endurance   = i.nextInt() * 5 + weight / 60;
        luck        = i.nextInt() * 10;
        perception  = i.nextInt() * 10;
        wisdom      = i.nextInt() * 5;  //altered
        willpower   = i.nextInt() * 5 + (2 * wisdom - endurance);
    }
}