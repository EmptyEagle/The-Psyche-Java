import java.util.ArrayList;

public class Map
{
    private String c = "\u001B[34m<()>\u001B[0m";
    private String healthFull = "+++++";
    private String healthDisplay = healthFull;
    private boolean playerHitless = true;
    private String mapId;
    private int roomsLoaded;
    private int monstersKilled;
    private int monstersFled;
    /* When turning back on yourself, your stale will increase.
     * When going in a new direction, your stale will reset to 0. */
    private int stale;
    private String cameFrom = "";
    // List of all rooms: true if contains caltrops, false if doesn't contain caltrops
    private ArrayList<Boolean> caltropRooms = new ArrayList<Boolean>();
    // Keys (currently not used)
    private boolean hasRedKey;
    private boolean hasBlueKey;
    private boolean isRedDoorUnlocked;
    private boolean isBlueDoorUnlocked;
    /* Only load certain rooms under these conditions.
     * The transition blocked by the forcefield will lead to a unique room that isn't reachable by another transition. */
    private boolean isGeneratorOff;
    private boolean isLoadedBeyondForcefield;
    private boolean bonePileChecked;
    private boolean vendingMachineChecked;
    private boolean hasWatcherItem;
    private boolean hasBloodItem;
    private boolean hasWeapon;
    private boolean unlockedChapter2; // Checks if Chapter 1 has been completed (currently not used)
    // Player stats
    private int playerHealth = 5;
    private int maxPlayerHealth = 5;
    private int playerDamageMultiplier = 1; // Will multiply player damage by this value (currently not used)
    // Monsters
    private String monster = "";
    private int monsterHealth;
    private int monsterDamageMax;
    // Check if room has a monster
    private boolean hasMonster;
    private boolean lastMonsterKilled;
    private int roomsSinceLastKill;
    // monsterSpawner is in charge of spawning monsters at a faster rate if monsters are being left alone
    private int monsterSpawner;
    private boolean canMonsterAttack;
    // Game states
    private boolean isNightmare;
    private boolean isGameOver;
    private boolean isGameOver_Nightmare;
    private boolean demoCompleted;

    public void createInitialMap() {
        int random = (int)(Math.random() * 3);
        if (random == 0) loadMap("map1");
        else if (random == 1) loadMap("map2");
        else if (random == 2) loadMap("map7_crossroads");
    }
    
    public void loadMapToRight() {
        if (cameFrom.equals("right")) stale++;
        else stale = 0;
        if (!lastMonsterKilled) monsterSpawner++;
        if (lastMonsterKilled) roomsSinceLastKill++;
        if (!lastMonsterKilled && roomsLoaded > 5 && monsterSpawner >= 4) rollMonster();
        if (lastMonsterKilled && roomsLoaded > 5 && roomsSinceLastKill >= 7) rollMonster();
        if (hasMonster) monsterSpawner = 0;
        int random = (int)(Math.random() * 5);
        if (random == 0) loadMap("map3");
        else if (random == 1) loadMap("map2");
        else if (random == 2) {
            if (roomsLoaded > 5) {
                if (isGeneratorOff) loadMap("map23_forcefieldOff");
                else loadMap("map23_forcefieldOn");
            }
            else loadMapToRight();
        }
        else if (random == 3) loadMap("map7_crossroads");
        else if (random == 4) {
            random = (int)(Math.random() * 20 + 1);
            if (random == 1) {
                loadMap("map999_secret");
            }
            else loadMapToRight();
        }
        cameFrom = "left";
    }
    
    public void loadMapToLeft() {
        if (cameFrom.equals("left")) stale++;
        else stale = 0;
        if (!lastMonsterKilled) monsterSpawner++;
        if (lastMonsterKilled) roomsSinceLastKill++;
        if (!lastMonsterKilled && roomsLoaded > 5 && monsterSpawner >= 4) rollMonster();
        if (lastMonsterKilled && roomsLoaded > 5 && roomsSinceLastKill >= 7) rollMonster();
        if (hasMonster) monsterSpawner = 0;
        int random = (int)(Math.random() * 7);
        if (random == 0) loadMap("map1");
        else if (random == 1) loadMap("map4");
        else if (random == 2) {
            if (roomsLoaded > 5) {
                if (isGeneratorOff) loadMap("map23_forcefieldOff");
                else loadMap("map23_forcefieldOn");
            }
            else loadMapToLeft();
        }
        else if (random == 3) {
            if (roomsLoaded > 30) {
                if (!isGeneratorOff) loadMap("map22_generatorOn");
                else loadMap("map22_generatorOff");
            }
            else loadMapToLeft();
        }
        else if (random == 4) {
            loadMap("map7_crossroads");
        }
        else if (random == 5) {
            if (roomsLoaded > 20) loadMap("map8_bonepile");
            else loadMapToLeft();
        }
        else if (random == 6) {
            if (roomsLoaded > 50) {
                if (!hasBloodItem()) loadMap("map10_bloodknife");
                else loadMapToLeft();
            }
            else loadMapToLeft();
        }
        cameFrom = "right";
    }
    
    public void loadMapToTop() {
        if (cameFrom.equals("top")) stale++;
        else stale = 0;
        if (!lastMonsterKilled) monsterSpawner++;
        if (lastMonsterKilled) roomsSinceLastKill++;
        if (!lastMonsterKilled && roomsLoaded > 5 && monsterSpawner >= 4) rollMonster();
        if (lastMonsterKilled && roomsLoaded > 5 && roomsSinceLastKill >= 7) rollMonster();
        if (hasMonster) monsterSpawner = 0;
        int random = (int)(Math.random() * 4);
        if (random == 0) loadMap("map5");
        else if (random == 1) loadMap("map6");
        else if (random == 2) loadMap("map7_crossroads");
        else if (random == 3) {
            if (roomsLoaded > 20) {
                if (!hasWatcherItem()) loadMap("map9_watcher");
                else loadMap("map9_watcher2");
            }
            else loadMapToTop();
        }
        cameFrom = "bottom";
    }
    
    public void loadMapToBottom() {
        if (cameFrom.equals("bottom")) stale++;
        else stale = 0;
        if (!lastMonsterKilled) monsterSpawner++;
        if (lastMonsterKilled) roomsSinceLastKill++;
        if (!lastMonsterKilled && roomsLoaded > 5 && monsterSpawner >= 4) rollMonster();
        if (lastMonsterKilled && roomsLoaded > 5 && roomsSinceLastKill >= 7) rollMonster();
        if (hasMonster) monsterSpawner = 0;
        int random = (int)(Math.random() * 3);
        if (random == 0) loadMap("map5");
        else if (random == 1) loadMap("map7_crossroads");
        else if (random == 2) {
            if (roomsLoaded > 20) loadMap("map8_bonepile");
            else loadMapToBottom();
        }
        cameFrom = "top";
    }
    
    public void loadMap(String map) {
        if (map.equals("map1")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                              ");
            System.out.println("|             "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map1";
        }
        if (map.equals("map2")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                              |");
            System.out.println("              "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map2";
        }
        if (map.equals("map3")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map3";
        }
        if (map.equals("map4")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                              ");
            System.out.println("|             "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map4";
        }
        if (map.equals("map5")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|             "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map5";
        }
        if (map.equals("map6")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                              |");
            System.out.println("              "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map6";
        }
        if (map.equals("map7_crossroads")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map7_crossroads";
        }
        if (map.equals("map8_bonepile")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                              ");
            System.out.println("|             "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|    ^                        |");
            System.out.println("|   Xx^>                      |");
            System.out.println("|  x/X>x\\                     |");
            System.out.println("|_____________________________|");
            mapId = "map8_bonepile";
        }
        if (map.equals("map9_watcher")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                   (<O>)     |");
            System.out.println("|     (<O>)    ^^             |");
            System.out.println("|             <oo>            |");
            System.out.println("|   (<O>)     /  \\            |");
            System.out.println("|                       (<O>) |");
            System.out.println("|                             |");
            System.out.println("|   (<O>)     "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map9_watcher";
        }
        if (map.equals("map9_watcher2")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                   (<O>)     |");
            System.out.println("|     (<O>)                   |");
            System.out.println("|                             |");
            System.out.println("|   (<O>)                     |");
            System.out.println("|               *       (<O>) |");
            System.out.println("|                             |");
            System.out.println("|   (<O>)     "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|              *              |");
            System.out.println("|                             |");
            System.out.println("|____________   *  ___________|");
            mapId = "map9_watcher2";
        }
        if (map.equals("map10_bloodknife")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|      \u001B[31m_——__\u001B[0m                   ");
            System.out.println("|     \u001B[31m/     |\u001B[0m       "+c+"       ");
            System.out.println("|     \u001B[31m\\____/\u001B[0m                  |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map10_bloodknife";
        }
        if (map.equals("map22_generatorOff")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|       |TTT|                  ");
            System.out.println("|       || ||       "+c+"       ");
            System.out.println("|       || ||                 |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map22_generatorOff";
        }
        if (map.equals("map22_generatorOn")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|        ^*^                  |");
            System.out.println("|        ^*^                  |");
            System.out.println("|        ^*^                  |");
            System.out.println("|        ^*^                  |");
            System.out.println("|        ^*^                  |");
            System.out.println("|       |TTT|                  ");
            System.out.println("|       ||*||       "+c+"       ");
            System.out.println("|       ||*||                 |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map22_generatorOn";
        }
        if (map.equals("map23_forcefieldOff")) {
            System.out.println("____________X      X___________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map23_forcefieldOff";
        }
        if (map.equals("map23_forcefieldOn")) {
            System.out.println("____________X======X___________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map23_forcefieldOn";
        }
        if (map.equals("map24_nightmare0")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|___________X      X__________|");
            mapId = "map24_nightmare0";
            isLoadedBeyondForcefield = true;
        }
        if (map.equals("map24_nightmare1")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|___________X——————X__________|");
            mapId = "map24_nightmare1";
        }
        if (map.equals("map24_nightmare2")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                              |");
            System.out.println("              "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map24_nightmare2";
        }
        if (map.equals("map24_nightmare3")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|             "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map24_nightmare3";
        }
        if (map.equals("map24_nightmare4")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                              |");
            System.out.println("              "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map24_nightmare4";
        }
        if (map.equals("map24_nightmare5")) {
            System.out.println("____________X——————X___________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map24_nightmare5";
        }
        if (map.equals("map24_end")) {
            System.out.println("____________X      X___________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("                               ");
            System.out.println("              "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map24_end";
        }
        if (map.equals("map24_nightmare6")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                              ");
            System.out.println("|             "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map24_nightmare6";
        }
        if (map.equals("map24_nightmare7")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|             "+c+"            |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|____________      ___________|");
            mapId = "map24_nightmare7";
        }
        if (map.equals("map24_nightmare8")) {
            System.out.println("_____________      ____________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                              ");
            System.out.println("|             "+c+"             ");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map24_nightmare8";
        }
        if (map.equals("map999_secret")) {
            System.out.println("_______________________________  "+healthDisplay);
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                       ___   |");
            System.out.println("|                      |UUU|  |");
            System.out.println("|                      |UUU|  |");
            System.out.println("                       |UUU|  |");
            System.out.println("      "+c+"             |===|  |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|                             |");
            System.out.println("|_____________________________|");
            mapId = "map999_secret";
            // System.out.println("Is this the forbidden fifth vending machine?");
        }
        if (stale < 3) {
            roomsLoaded++;
        }
    }
    
    public String getMap() {
        return mapId;
    }
    
    // Note: don't need to check maps that would return false
    public boolean checkMove(String direction, String map) {
        if (direction.equals("left")) {
            if (map.equals("map2")) {
                return true;
            }
            if (map.equals("map3")) {
                return true;
            }
            if (map.equals("map6")) {
                return true;
            }
            if (map.equals("map7_crossroads")) {
                return true;
            }
            if (map.equals("map23_forcefieldOn")) {
                return true;
            }
            if (map.equals("map23_forcefieldOff")) {
                return true;
            }
            if (map.equals("map24_nightmare1")) {
                return true;
            }
            if (map.equals("map24_nightmare2")) {
                return true;
            }
            if (map.equals("map24_nightmare4")) {
                return true;
            }
            if (map.equals("map24_nightmare5")) {
                return true;
            }
            if (map.equals("map24_end")) {
                return true;
            }
            if (map.equals("map999_secret")) {
                return true;
            }
        }
        if (direction.equals("right")) {
            if (map.equals("map1")) {
                return true;
            }
            if (map.equals("map3")) {
                return true;
            }
            if (map.equals("map4")) {
                return true;
            }
            if (map.equals("map7_crossroads")) {
                return true;
            }
            if (map.equals("map8_bonepile")) {
                return true;
            }
            if (map.equals("map10_bloodknife")) {
                return true;
            }
            if (map.equals("map23_forcefieldOn")) {
                return true;
            }
            if (map.equals("map23_forcefieldOff")) {
                return true;
            }
            if (map.equals("map22_generatorOn")) {
                return true;
            }
            if (map.equals("map22_generatorOff")) {
                return true;
            }
            if (map.equals("map24_nightmare1")) {
                return true;
            }
            if (map.equals("map24_nightmare5")) {
                return true;
            }
            if (map.equals("map24_nightmare6")) {
                return true;
            }
            if (map.equals("map24_nightmare8")) {
                return true;
            }
            if (map.equals("map24_end")) {
                return true;
            }
        }
        if (direction.equals("up")) {
            if (map.equals("map1")) {
                return true;
            }
            if (map.equals("map2")) {
                return true;
            }
            if (map.equals("map5")) {
                return true;
            }
            if (map.equals("map7_crossroads")) {
                return true;
            }
            if (map.equals("map8_bonepile")) {
                return true;
            }
            if (map.equals("map23_forcefieldOn")) {
                System.out.println("A forcefield is blocking the way.");
            }
            if (map.equals("map23_forcefieldOff")) {
                return true;
            }
            if (map.equals("map24_nightmare3")) {
                return true;
            }
            if (map.equals("map24_nightmare4")) {
                return true;
            }
            if (map.equals("map24_nightmare7")) {
                return true;
            }
            if (map.equals("map24_nightmare8")) {
                return true;
            }
            if (map.equals("map24_end")) {
                return true;
            }
        }
        if (direction.equals("down")) {
            if (map.equals("map4")) {
                return true;
            }
            if (map.equals("map5")) {
                return true;
            }
            if (map.equals("map6")) {
                return true;
            }
            if (map.equals("map7_crossroads")) {
                return true;
            }
            if (map.equals("map9_watcher")) {
                return true;
            }
            if (map.equals("map9_watcher2")) {
                return true;
            }
            if (map.equals("map24_nightmare2")) {
                return true;
            }
            if (map.equals("map24_nightmare3")) {
                return true;
            }
            if (map.equals("map24_nightmare6")) {
                return true;
            }
            if (map.equals("map24_nightmare7")) {
                return true;
            }
        }
        return false;
    }
    
    public void disableGenerator() {
        isGeneratorOff = true;
    }
    
    public void checkBonePile() {
        bonePileChecked = true;
    }
    
    public boolean isBonePileChecked() {
        return bonePileChecked;
    }
    
    public void topStale() {
        stale = 3;
    }
    
    public void resetStale() {
        stale = 0;
    }
    
    public void rollMonster() {
        int roll = (int)(Math.random() * 100 + 1);
        // 10% chance for Armor
        if (roll > 90) {
            monster = "Armor";
            monsterHealth = 4;
            monsterDamageMax = 3;
            hasMonster = true;
            lastMonsterKilled = false;
            canMonsterAttack = true;
        }
        // 15% chance for Skeleton
        else if (roll > 75) {
            monster = "Skeleton";
            monsterHealth = 2;
            monsterDamageMax = 2;
            hasMonster = true;
            lastMonsterKilled = false;
            canMonsterAttack = true;
        }
        // 25% chance for Husk
        else if (roll > 50) {
            monster = "Husk";
            monsterHealth = 1;
            monsterDamageMax = 1;
            hasMonster = true;
            lastMonsterKilled = false;
            canMonsterAttack = true;
        }
        // 50% chance for nothing to spawn
        else {
            monster = "";
            monsterHealth = 0;
            monsterDamageMax = 0;
            hasMonster = false;
        }
    }
    
    public void decreaseMonsterHealth(int healthDecrease) {
        if (monsterHealth - healthDecrease > 0 && healthDecrease != 0) {
            monsterHealth -= healthDecrease;
            System.out.println("The "+monster+" took "+healthDecrease+" damage!");
        }
        else if (monsterHealth - healthDecrease <= 0) {
            monsterHealth = 0;
            System.out.println("The "+monster+" took "+healthDecrease+" damage and died!");
            monster = "";
            monsterDamageMax = 0;
            hasMonster = false;
            lastMonsterKilled = true;
            roomsSinceLastKill = 0;
            canMonsterAttack = false;
            monstersKilled++;
        }
        if (healthDecrease == 0) {
            System.out.println("Your attack misses!");
        }
    }
    
    public int playerDamageBonus(int bonus) {
        return bonus;
    }
    
    public void run(int bonus) {
        boolean isValidMove;
        int roll = (int)(Math.random() * 100 + 1 + bonus);
        int direction = (int)(Math.random() * 4);
        if (direction == 0) isValidMove = checkMove("right", getMap());
        else if (direction == 1) isValidMove = checkMove("left", getMap());
        else if (direction == 2) isValidMove = checkMove("up", getMap());
        else isValidMove = checkMove("down", getMap());
        if (roll > 40 && isValidMove == true) {
            monster = "";
            monsterHealth = 0;
            monsterDamageMax = 0;
            hasMonster = false;
            // Pick direction
            if (direction == 0) {
                System.out.print("\033[H\033[2J");
                System.out.println("You are quick on your feet and run to the right.");
                loadMapToRight();
                monstersFled++;
            }
            else if (direction == 1) {
                System.out.print("\033[H\033[2J");
                System.out.println("You are quick on your feet and run to the left.");
                loadMapToLeft();
                monstersFled++;
            }
            else if (direction == 2) {
                System.out.print("\033[H\033[2J");
                System.out.println("You are quick on your feet and run forward.");
                if (getMap().equals("map23_forcefieldOff")) {
                    loadMap("map24");
                    monstersFled++;
                    demoEnd();
                }
                else if (getMap().equals("map23_forcefieldOn")) {
                    System.out.println("You are unable to flee.");
                }
                else {
                    loadMapToTop();
                    monstersFled++;
                }
            }
            else {
                System.out.print("\033[H\033[2J");
                System.out.println("You are quick on your feet and run back.");
                if (getMap().equals("map24")) loadMap("map23_forcefieldOff");
                else loadMapToBottom();
                monstersFled++;
            }
            canMonsterAttack = false;
        }
        else System.out.println("You are unable to flee.");
    }
    
    public boolean canMonsterAttack() {
        return canMonsterAttack;
    }
    
    public int getMonsterHealth() {
        return monsterHealth;
    }
    
    public String getMonster() {
        return monster;
    }
    
    public boolean hasMonster() {
        return hasMonster;
    }
    
    public void setHasMonster(boolean isMonster) {
        hasMonster = isMonster;
    }
    
    public void setMonster(String monsterName) {
        monster = monsterName;
        hasMonster = true;
        canMonsterAttack = true;
        lastMonsterKilled = false;
        if (monsterName.equals("Nightmare")) {
            monsterHealth = 10;
            monsterDamageMax = 1;
            isNightmare = true;
        }
    }
    
    public void monsterAttack(ArrayList defense) {
        int roll = (int)(Math.random() * 100 + 1);
        int monsterDamage = (int)(Math.random() * monsterDamageMax + 1);
        if (defense.contains("Helmet") && monsterDamage > 1) monsterDamage--;
        if (roll > 50) {
            playerHealth -= monsterDamage;
            System.out.println("The "+monster+" attacks you, dealing "+monsterDamage+" damage.");
            updateHealthDisplay();
            playerHitless = false;
        }
        else System.out.println("The "+monster+"'s attack misses.");
    }
    
    public int getPlayerHealth() {
        return playerHealth;
    }
    
    public int getMaxHealth() {
        return maxPlayerHealth;
    }
    
    public void healPlayer(int heal) {
        playerHealth += heal;
        if (playerHealth > maxPlayerHealth) {
            playerHealth = maxPlayerHealth;
        }
        updateHealthDisplay();
    }
    
    public void damagePlayer(int damage) {
        playerHealth -= damage;
        updateHealthDisplay();
    }
    
    public void witherPlayerMaxHealth(int damage) {
        maxPlayerHealth -= damage;
        if (playerHealth > maxPlayerHealth) playerHealth = maxPlayerHealth;
        updateHealthDisplay();
    }
    
    public void updateHealthDisplay() {
        if (maxPlayerHealth == 5) {
            if (playerHealth == 5) healthDisplay = "+++++";
            else if (playerHealth == 4) healthDisplay = "++++-";
            else if (playerHealth == 3) healthDisplay = "+++--";
            else if (playerHealth == 2) healthDisplay = "++---";
            else if (playerHealth == 1) healthDisplay = "+----";
            else {
                healthDisplay = "-----";
                gameOver();
            }
        }
        else if (maxPlayerHealth == 4) {
            if (playerHealth == 4) healthDisplay = "++++#";
            else if (playerHealth == 3) healthDisplay = "+++-#";
            else if (playerHealth == 2) healthDisplay = "++--#";
            else if (playerHealth == 1) healthDisplay = "+---#";
            else {
                healthDisplay = "----#";
                gameOver();
            }
        }
        else if (maxPlayerHealth == 3) {
            if (playerHealth == 3) healthDisplay = "+++##";
            else if (playerHealth == 2) healthDisplay = "++-##";
            else if (playerHealth == 1) healthDisplay = "+--##";
            else {
                healthDisplay = "---##";
                gameOver();
            }
        }
        else if (maxPlayerHealth == 2) {
            if (playerHealth == 2) healthDisplay = "++###";
            else if (playerHealth == 1) healthDisplay = "+-###";
            else {
                healthDisplay = "--###";
                gameOver();
            }
        }
        else if (maxPlayerHealth == 1) {
            if (playerHealth == 1) healthDisplay = "+####";
            else {
                healthDisplay = "-####";
                gameOver();
            }
        }
        else {
            gameOver();
        }
    }
    
    public void gameOver() {
        if (isNightmare) {
            System.out.print("\033[H\033[2J");
            System.out.println("GAME OVER\nYou are trapped in an endless nightmare.");
            isGameOver = true;
            System.exit(0);
        }
        else {
            System.out.print("\033[H\033[2J");
            System.out.println("GAME OVER\nYour mind wanders in the darkness.");
            isGameOver = true;
            System.exit(0);
        }
    }
    
    public void gameOver_Nightmare() {
        
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public void initializeCaltrops() {
        for (int i = 0; i < 999; i++) {
            caltropRooms.add(false);
        }
    }
    
    public void placeCaltrops() {
        if (getMap().contains("map1")) caltropRooms.set(1, true);
        else if (getMap().contains("map2")) caltropRooms.set(2, true);
        else if (getMap().contains("map3")) caltropRooms.set(3, true);
        else if (getMap().contains("map4")) caltropRooms.set(4, true);
        else if (getMap().contains("map5")) caltropRooms.set(5, true);
        else if (getMap().contains("map6")) caltropRooms.set(6, true);
        else if (getMap().contains("map7")) caltropRooms.set(7, true);
        else if (getMap().contains("map8")) caltropRooms.set(8, true);
        else if (getMap().contains("map22")) caltropRooms.set(22, true);
        else if (getMap().contains("map23")) caltropRooms.set(23, true);
        else if (getMap().contains("map24")) caltropRooms.set(24, true);
        else if (getMap().contains("map999")) caltropRooms.set(999, true);
    }
    
    public void checkForCaltrop() {
        int mapNum = Integer.parseInt(getMap().replaceAll("[^0-9]", ""));
        if ((caltropRooms.get(mapNum) || isNightmare && caltropRooms.get(24)) && hasMonster()) {
            monsterHealth--;
            if (monsterHealth == 0) {
                System.out.println("The "+monster+" stepped on a caltrop and disintegrated.");
                monster = "";
                monsterDamageMax = 0;
                hasMonster = false;
                lastMonsterKilled = true;
                roomsSinceLastKill = 0;
                canMonsterAttack = false;
            }
            else System.out.println("The "+monster+" stepped on a caltrop.");
        }
    }
    
    public boolean hasCaltrops() {
        int mapNum = Integer.parseInt(getMap().replaceAll("[^0-9]", ""));
        return caltropRooms.get(mapNum);
    }
    
    public void vendingMachineCheck() {
        vendingMachineChecked = true;
    }
    
    public boolean checkVendingMachine() {
        return vendingMachineChecked;
    }
    
    public int monstersKilled() {
        return monstersKilled;
    }
    
    public boolean lastMonsterKilled() {
        return lastMonsterKilled;
    }
    
    public int monstersFled() {
        return monstersFled;
    }
    
    public void getWatcherItem() {
        hasWatcherItem = true;
    }
    
    public boolean hasWatcherItem() {
        return hasWatcherItem;
    }
    
    public void getBloodItem() {
        hasBloodItem = true;
        hasWeapon = true;
    }
    
    public boolean hasBloodItem() {
        return hasBloodItem;
    }
    
    public boolean hasWeapon() {
        return hasWeapon;
    }
    
    public void setNightmare(boolean nightmare) {
        isNightmare = nightmare;
    }
    
    public boolean isNightmare() {
        return isNightmare;
    }
    
    public void unlockChapter2() {
        unlockedChapter2 = true;
    }
    
    public boolean canChapter2() {
        return unlockedChapter2;
    }
    
    public boolean isHitless() {
        return playerHitless;
    }
    
    public void demoEnd() {
        demoCompleted = true;
    }
    
    public boolean isDemoDone() {
        return demoCompleted;
    }
    
    
    // For testing
    public void enableGenerator() {
        isGeneratorOff = false;
    }
    
    public boolean isGeneratorOn() {
        return !isGeneratorOff;
    }
    
    public int getStale() {
        return stale;
    }
    
    public int getRoomsLoaded() {
        return roomsLoaded;
    }
}