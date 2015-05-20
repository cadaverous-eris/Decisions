import java.util.Random;

public class Character{
    int gender;
    int weight;
    int beauty;
    int charisma;
    int endurance;
    int luck;
    int perception;
    int wisdom;
    int willpower;


    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getBeauty() {
        return beauty;
    }
    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }
    public int getCharisma() {
        return charisma;
    }
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
    public int getEndurance() {
        return endurance;
    }
    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }
    public int getLuck() {
        return luck;
    }
    public void setLuck(int luck) {
        this.luck = luck;
    }
    public int getPerception() {
        return perception;
    }
    public void setPerception(int perception) {
        this.perception = perception;
    }
    public int getWisdom() {
        return wisdom;
    }
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }
    public int getWillpower() {
        return willpower;
    }
    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

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