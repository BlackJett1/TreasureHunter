/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
import java. util. Random;
public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String[] treasure = {"a crown", "a trophy", "a gem", "dust"};
    private String townTreasure;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        int randomNum =  (int) (Math.random() * 4);
        this.shop = shop;
        this.terrain = getNewTerrain();
        townTreasure = treasure[randomNum];
        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
        hunter.setHuntedInTown(false);
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (TreasureHunter.allowItemBreak && checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = Colors.RED + "You couldn't find any trouble" + Colors.RESET;
        } else {
            printMessage = Colors.RED +"You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("sword")) {
                printMessage += Colors.RED +"*glances at your sword* hey man just take the money im not trying to die." + Colors.RESET;
                printMessage += "\nYou won the DUEL and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else if (Math.random() > noTroubleChance) {
                printMessage += Colors.RED +"Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW +  goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(-goldDiff);
            }
        }
    }
    public void digForGold() {
        Random randomNum = new Random();
        if (hunter.hasDugInTown()) {
            printMessage += "\nYou already dug for gold in this town.";
            return;
        }
        if (!hunter.hasItemInKit("shovel")) {
            printMessage += "\nYou can't dig for gold without a shovel.";
            return;
        }
        int num = randomNum.nextInt(100);
        if (num < 50) {
            int goldAmount = randomNum.nextInt(20);
            hunter.changeGold(goldAmount);
            printMessage += "\nYou dug up " + goldAmount + " gold!";
        } else {
            printMessage += "\nYou dug but only found dirt.";
        }
        hunter.setDugInTown(true);
    }

    public String infoString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET +".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .16) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .32) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .48) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .64) {
            return new Terrain("Desert", "Water");
        } else if (rnd < .8){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private static boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void huntForTreasure(){
        if (hunter.hasHuntedInTown()) {
            System.out.println("You have already searched this town");
            return;
        }
        if (hunter.hasTreasure(townTreasure)){
            System.out.println("You have already collected this treasure");
            hunter.setHuntedInTown(true);
            return;
        }
        System.out.println("You hunt for treasure");
        System.out.println(". . .");
        System.out.println("You found " + townTreasure + "!");
        if (!(townTreasure.equals("dust"))){
            hunter.addTreasure(townTreasure);
            hunter.setHuntedInTown(true);
        }
        if (!(Hunter.collectedTreasure[0] == null || Hunter.collectedTreasure[1] == null || Hunter.collectedTreasure[2] == null)){
            System.out.println("GOOD JOB YOU FOUND ALL THE TREASURES!!!");
            System.exit(0);
        }
    }
}