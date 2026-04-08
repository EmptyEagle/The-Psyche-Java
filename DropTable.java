import java.util.ArrayList;

public class DropTable
{
    private Inventory inv;
    
    public DropTable(Inventory inv) {
        this.inv = inv;
    }
    
    public void rollDrop(String monster) {
        if (monster.equals("Armor")) {
            int random = (int)(Math.random() * 100 + 1);
            if (random > 90 && !inv.getEquipment().contains("Helmet")) {
                inv.addEquipment("Helmet");
                System.out.println("The Armor dropped its helmet.");
            }
        }
        else if (monster.equals("Skeleton")) {
            int random = (int)(Math.random() * 100 + 1);
            if (random > 95) {
                inv.addItem("caltrops");
                System.out.println("The Skeleton dropped some caltrops.");
            }
        }
        else if (monster.equals("Husk")) {
            int random = (int)(Math.random() * 100 + 1);
            if (random > 95) {
                inv.addItem("health vial");
                System.out.println("The Husk dropped a health vial.");
            }
        }
    }
}