import java.util.Scanner;

public class JavaRPG
{
    public static void main(String[] args)
    {
        System.out.println("The Psyche | Java Demo");
        System.out.println("\nControls:\nMove - left, right, up, down\nSelection - 1, 2, 3, yes, no\n");
        
        Scanner input = new Scanner(System.in);
        Map map = new Map();
        Inventory inv = new Inventory(map);
        DropTable drop = new DropTable(inv);
        int random;
        // Variables for later
        int turnsUntilFog = 3;
        int nightmareFlee = 0;
        // Starting game
        map.initializeCaltrops();
        map.createInitialMap();
        
        // Gameplay Loop
        while (!map.isGameOver()) {
            System.out.println("\nWhich direction would you like to go?");
            String direction = input.nextLine();
            while (!(direction.equals("left") || direction.equals("right") || direction.equals("up") || direction.equals("down") || direction.equals("konami"))) {
                direction = input.nextLine();
            }
            boolean isValidMove = map.checkMove(direction, map.getMap());
            if (isValidMove) {
                System.out.print("\033[H\033[2J");
                if (direction.equals("right")) {
                    if (map.getMap().equals("map24_nightmare1")) {
                        map.topStale();
                        map.loadMap("map24_nightmare2");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare5")) {
                        map.topStale();
                        map.loadMap("map24_nightmare4");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare6")) {
                        map.topStale();
                        map.loadMap("map24_nightmare1");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare8")) {
                        map.topStale();
                        if (map.canChapter2()) map.loadMap("map24_end");
                        else map.loadMap("map24_nightmare5");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_end")) {
                        map.topStale();
                        map.loadMap("map24_nightmare4");
                        map.resetStale();
                    }
                    else map.loadMapToRight();
                }
                else if (direction.equals("left")) {
                    if (map.getMap().equals("map24_nightmare1")) {
                        map.topStale();
                        map.loadMap("map24_nightmare6");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare2")) {
                        map.topStale();
                        map.loadMap("map24_nightmare1");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare4")) {
                        map.topStale();
                        if (map.canChapter2()) map.loadMap("map24_end");
                        else map.loadMap("map24_nightmare5");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare5")) {
                        map.topStale();
                        map.loadMap("map24_nightmare8");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_end")) {
                        map.topStale();
                        map.loadMap("map24_nightmare8");
                        map.resetStale();
                    }
                    else map.loadMapToLeft();
                }
                else if (direction.equals("up")) {
                    if (map.getMap().equals("map23_forcefieldOff")) {
                        map.topStale();
                        map.loadMap("map24_nightmare0");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare3")) {
                        map.topStale();
                        map.loadMap("map24_nightmare2");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare4")) {
                        map.topStale();
                        map.loadMap("map24_nightmare3");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare7")) {
                        map.topStale();
                        map.loadMap("map24_nightmare6");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare8")) {
                        map.topStale();
                        map.loadMap("map24_nightmare7");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_end")) {
                        map.demoEnd();
                    }
                    else map.loadMapToTop();
                }
                else {
                    if (map.getMap().equals("map24_nightmare5")) {
                        map.topStale();
                        map.loadMap("map23_forcefieldOff");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare2")) {
                        map.topStale();
                        map.loadMap("map24_nightmare3");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare3")) {
                        map.topStale();
                        map.loadMap("map24_nightmare4");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare6")) {
                        map.topStale();
                        map.loadMap("map24_nightmare7");
                        map.resetStale();
                    }
                    else if (map.getMap().equals("map24_nightmare7")) {
                        map.topStale();
                        map.loadMap("map24_nightmare8");
                        map.resetStale();
                    }
                    else map.loadMapToBottom();
                }
            }
            
            // These rooms cannot spawn monsters
            if (map.getMap().contains("map22") || map.getMap().contains("map8") || map.getMap().contains("map999") || map.getMap().contains("map9") || map.getMap().contains("map24")) map.setHasMonster(false);
            
            // Events on map load
            if (map.getMap().equals("map22_generatorOn")) {
                System.out.println("There is a generator in the room, shooting a beam of blinding light into the sky above.");
                System.out.println("Turn off the machine?");
                String selection = input.nextLine();
                if (selection.equals("yes")) {
                    System.out.print("\033[H\033[2J");
                    map.disableGenerator();
                    map.loadMap("map22_generatorOff");
                    System.out.println("You turn off the machine.");
                }
                else System.out.println("You decide to leave the generator, which you have no right to meddle with, alone.");
            }
            else if (map.getMap().equals("map22_generatorOff")) System.out.println("There is a generator here.");
            else if (map.getMap().equals("map8_bonepile")) {
                if (!map.isBonePileChecked()) {
                    System.out.println("A pile of jagged bones sits in the corner, a faint glow coming from within.");
                    System.out.println("Inspect the glow?");
                    String selection = input.nextLine();
                    if (selection.equals("yes")) {
                        random = (int)(Math.random() * 2);
                        if (random == 1 && map.getPlayerHealth() > 1) {
                            map.damagePlayer(1);
                            System.out.println("The bones cut into your hand, and you take 1 damage.");
                        }
                        random = (int)(Math.random() * 2);
                        if (random == 0) {
                            inv.addItem("health vial");
                            System.out.println("You put your hand inside the pile, and you find a health vial.");
                        }
                        else {
                            inv.addItem("health potion");
                            System.out.println("You put your hand inside the pile, and you find a health potion.");
                        }
                        map.checkBonePile();
                    }
                }
                else System.out.println("The bones remain to be motionless.");
            }
            else if (map.getMap().equals("map999_secret")) {
                if (!map.checkVendingMachine()) {
                    System.out.println("There is a vending machine here. You press a button, and...");
                    inv.addRandom();
                    map.vendingMachineCheck();
                }
                else System.out.println("The machine is empty.");
            }
            else if (map.getMap().equals("map9_watcher")) {
                inv.addWatcherItem();
            }
            else if (map.getMap().equals("map9_watcher2")) {
                System.out.println("You feel watched.");
            }
            else if (map.getMap().equals("map10_bloodknife")) {
                inv.addBloodItem();
            }
            
            // Nightmare Boss
            else if (map.getMap().equals("map24_nightmare0")) {
                System.out.println("You feel a chill in the air. Involutarily, your heart starts beating faster and faster.");
                System.out.print("...");
                input.nextLine();
                System.out.print("\033[H\033[2J");
                map.loadMap("map24_nightmare1");
                map.setMonster("Nightmare");
                System.out.println("\nYou shiver.");
                map.setNightmare(true);
            }
            
            if (map.getMap().length() > 7 && nightmareFlee > 0 && map.getMap().substring(7).contains(Integer.toString(nightmareFlee))) map.setHasMonster(true);
            
            // Combat
            if (map.hasMonster()) {
                if (map.getMonster().equals("Nightmare")) {
                    System.out.println("\nThe Nightmare sets in.");
                }
                else System.out.println("\n"+map.getMonster()+" appears.");
                map.checkForCaltrop();
                
                while (map.hasMonster() && !map.isGameOver()) {
                    if (map.getMonster().equals("Nightmare") && turnsUntilFog <= 0) {
                        System.out.println("The darkness consumes your will. (-1 Max Health)");
                        map.witherPlayerMaxHealth(1);
                        turnsUntilFog = 3;
                    }
                    System.out.println("1. Attack\n2. Inventory\n3. Flee");
                    String answer = input.nextLine();
                    while (!(answer.equals("1") || answer.equals("2") || answer.equals("3"))) answer = input.nextLine();
                    
                    // Refreshing screen
                    System.out.print("\033[H\033[2J");
                    map.topStale();
                    map.loadMap(map.getMap());
                    map.resetStale();
                    System.out.println();
                    
                    if (answer.equals("1")) {
                        random = (int)(Math.random() * 3);
                        String monster = map.getMonster();
                        if (map.hasWeapon() && random > 0) {
                            int previouslyKilled = map.monstersKilled();
                            map.decreaseMonsterHealth(random + inv.weaponDamage());
                            inv.weaponEffect();
                        }
                        else map.decreaseMonsterHealth(random);
                        if (map.getMonsterHealth() <= 0) drop.rollDrop(monster);
                    }
                    else if (answer.equals("2")) {
                        if (inv.getInventory().isEmpty()) {
                            System.out.println("You don't have any items.");
                            continue;
                        }
                        else {
                            inv.printInventory();
                            System.out.println("Use which item? (cancel - back to menu)");
                            String selection = input.nextLine();
                            while (!inv.getInventory().contains(selection) && !selection.equals("cancel")) {
                                selection = input.nextLine();
                            }
                            if (selection.equals("health vial")) {
                                System.out.println("You drank the health vial.");
                                inv.useItem("health vial");
                            }
                            else if (selection.equals("health potion")) {
                                System.out.println("You drank the health potion.");
                                inv.useItem("health potion");
                            }
                            else if (selection.equals("caltrops")) {
                                System.out.println("You threw the caltrops on the ground.");
                                inv.useItem("caltrops");
                            }
                            else if (selection.equals("cancel")) continue;
                        }
                    }
                    else if (answer.equals("3")) {
                        if (map.getMonster().equals("Nightmare")) System.out.println("You cannot run from a Nightmare.");
                        else if (inv.hasEquipment("Watchers' Emblem")) map.run(19);
                        else map.run(0);
                    }
                    if (map.canMonsterAttack()) map.monsterAttack(inv.getEquipment());
                    if (map.getMonster().equals("Nightmare")) {
                        turnsUntilFog--;
                        nightmareFlee = (int)(Math.random() * 9);
                        while (map.getMap().substring(7).contains(Integer.toString(nightmareFlee))) nightmareFlee = (int)(Math.random() * 9);
                        if (nightmareFlee > 0) {
                            map.setHasMonster(false);
                            System.out.println("The Nightmare vanishes into the shadows.");
                        }
                    }
                    if (map.isNightmare() && map.getMonsterHealth() <= 0) {
                        map.setNightmare(false);
                        map.setHasMonster(false);
                        map.unlockChapter2();
                    }
                }
            }
            
            
            // Debugging/Testing Codes
            if (direction.equals("konami")) {
                System.out.println("Debugging tools enabled.");
                String command = input.nextLine();
                while (!(command.equals("map_id") || command.equals("generator_toggle") || command.equals("get_stale") || command.equals("get_loaded") || command.equals("get_item") || command.equals("get_equipment") || command.equals("caltrops_check") || command.equals("caltrops_place"))) {
                    command = input.nextLine();
                }
                if (command.equals("map_id")) {
                    command = input.nextLine();
                    System.out.print("\033[H\033[2J");
                    map.loadMap(command);
                    System.out.println(command+" loaded.");
                }
                else if (command.equals("generator_toggle")) {
                    if (!map.isGeneratorOn()) {
                        map.enableGenerator();
                        System.out.println("Generator on.");
                    }
                    else {
                        map.disableGenerator();
                        System.out.println("Generator off.");
                    }
                }
                else if (command.equals("get_stale")) {
                    System.out.println(map.getStale());
                }
                else if (command.equals("get_loaded")) {
                    System.out.println(map.getRoomsLoaded());
                }
                else if (command.equals("get_item")) {
                    command = input.nextLine();
                    while (!(command.equals("health vial") || command.equals("health potion") || command.equals("caltrops"))) {
                        System.out.println("Item not found.");
                        command = input.nextLine();
                    }
                    inv.addItem(command);
                    System.out.println("Added "+command+" to inventory.");
                }
                else if (command.equals("get_equipment")) {
                    command = input.nextLine();
                    while (!(command.equals("Watchers' Emblem") || command.equals("Coagulated Blade") || command.equals("Helmet"))) {
                        System.out.println("Equipment not found.");
                        command = input.nextLine();
                    }
                    if (command.equals("Coagulated Blade")) map.getBloodItem();
                    else if (command.equals("Watchers' Emblem")) map.getWatcherItem();
                    System.out.println("Added "+command+" to inventory.");
                    inv.addEquipment(command);
                }
                else if (command.equals("caltrops_check")) {
                    System.out.println(map.hasCaltrops());
                }
                else if (command.equals("caltrops_place")) {
                    map.placeCaltrops();
                    System.out.println("Caltrops placed.");
                }
            }
            
            if (map.isDemoDone()) {
                System.out.print("\033[H\033[2J");
                System.out.println("Thanks for playing the demo!");
                System.out.print("———————\nRESULTS\nRooms: "+map.getRoomsLoaded()+"\nKills: "+map.monstersKilled()+"\nEquipment: ");
                if (inv.getEquipment().size() >= 1) {
                    inv.printEquipmentAt(0);
                }
                else System.out.print("None");
                for (int i = 1; i < inv.getEquipment().size(); i++) {
                    System.out.print(", ");
                    inv.printEquipmentAt(i);
                }
                if (map.isHitless()) System.out.println("\n\nYou never got hit!");
                System.exit(0);
            }
        }
        
        /*
        System.out.print("\u001B[A");
        This moves the selector up one line, and can be used to rewrite old lines.
        
        System.out.print("\033[H\033[2J");
        This clears the entire screen.
        */
    }
}