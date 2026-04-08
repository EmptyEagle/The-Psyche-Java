import java.util.ArrayList;

public class Inventory
{
    // Making it use the same Map object for modification
    private Map map;
    private ArrayList<String> inv = new ArrayList<String>();
    private ArrayList<String> equip = new ArrayList<String>();
    private boolean itemError;
    
    // Passing the Map object into the constructor
    public Inventory(Map map) {
        this.map = map;
    }
    
    public void addItem(String item) {
        inv.add(item);
    }
    
    public void removeItem(String item) {
        inv.remove(item);
    }
    
    public void printInventory() {
        System.out.println(inv);
    }
    
    public ArrayList getInventory() {
        return inv;
    }
    
    public void printEquipmentAt(int position) {
        System.out.print(equip.get(position));
    }
    
    public void addEquipment(String equipment) {
        equip.add(equipment);
    }
    
    public ArrayList getEquipment() {
        return equip;
    }
    
    // Things items do
    public void useItem(String item) {
        if (item.equals("health vial")) {
            int heal = (int)(Math.random() * 2 + 1);
            if (map.getPlayerHealth() + heal > 5) {
                System.out.println("You have healed "+(map.getMaxHealth() - map.getPlayerHealth())+" hit points.");
                map.healPlayer(heal);
            }
            else {
                map.healPlayer(heal);
                System.out.println("You have healed "+heal+" hit points.");
            }
        }
        else if (item.equals("health potion")) {
            int heal = (int)(Math.random() * 3 + 2);
            if (map.getPlayerHealth() + heal > 5) {
                System.out.println("You have healed "+(map.getMaxHealth() - map.getPlayerHealth())+" hit points.");
                map.healPlayer(heal);
            }
            else {
                map.healPlayer(heal);
                System.out.println("You have healed "+heal+" hit points.");
            }
        }
        else if (item.equals("caltrops")) {
            if (!map.hasCaltrops()) map.placeCaltrops();
            else {
                System.out.println("There are already caltrops here.");
                itemError = true;
            }
        }
        if (!itemError) removeItem(item);
        itemError = false;
    }
    
    public boolean hasEquipment(String equipment) {
        if (equip.contains(equipment)) {
            return true;
        }
        return false;
    }
    
    public void addRandom() {
        int random = (int)(Math.random() * 3);
        if (random == 0) {
            addItem("health vial");
            System.out.println("You got a health vial.");
        }
        else if (random == 1) {
            addItem("health potion");
            System.out.println("You got a health potion.");
        }
        else if (random == 2) {
            addItem("caltrops");
            System.out.println("You got some caltrops.");
        }
    }
    
    public void addWatcherItem() {
        if (!map.hasWatcherItem()) {
            if (map.monstersKilled() < 2) {
            System.out.println("You have been merciful on your journey. Take this as a token of my respect.");
            addEquipment("Watchers' Emblem");
            System.out.println("You got the Watchers' Emblem.");
            }
            else System.out.println("Your will is strong, but your compassion is lacking. You are not worthy.");
        }
        else System.out.println("The creature peers at you curiously.");
        map.getWatcherItem();
    }
    
    public void addBloodItem() {
        if (!map.hasBloodItem()) {
            if (map.monstersKilled() > 7) {
                System.out.println("A blade coagulates from the pool.");
                addEquipment("Coagulated Blade");
                System.out.println("You got the Coagulated Knife.");
            }
            else System.out.println("The pool of blood disgusts you.");
        }
        else System.out.println("There is a pool of blood here.");
        map.getBloodItem();
    }
    
    public void weaponEffect() {
        if (hasEquipment("Coagulated Blade")) {
            if (map.lastMonsterKilled() && map.getPlayerHealth() < map.getMaxHealth()) {
                map.healPlayer(1);
                System.out.println("The blood mends you.");
            }
        }
    }
    
    public int weaponDamage() {
        if (hasEquipment("Coagulated Blade")) {
            return 1;
        }
        return 0;
    }
}