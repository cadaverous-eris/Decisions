public class Character{
    private String name;
    private Attributes attributes;
    private Equippables equippables;
    private Consumables consumables;

    public Character(){
        attributes  = new Attributes();
        equippables = new Equippables();
        consumables = new Consumables();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Equippables getEquippables() {
        return equippables;
    }

    public void setEquippables(Equippables equippables) {
        this.equippables = equippables;
    }

    public Consumables getConsumables() {
        return consumables;
    }

    public void setConsumables(Consumables consumables) {
        this.consumables = consumables;
    }
}