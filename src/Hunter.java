/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original preogram -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private int gold;
    private boolean hasDugInCurrentTown = false;
    public static String[] collectedTreasure = new String[3];
    public static boolean hasHuntedInCurrentTown = false;


    public void setDugInTown(boolean status) {
        this.hasDugInCurrentTown = status;
    }

    public void setHuntedInTown(boolean status) {
        this.hasHuntedInCurrentTown = status;
    }

    public boolean hasDugInTown() {
        return this.hasDugInCurrentTown;
    }
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[8];
        gold = startingGold;
    }

    public boolean hasHuntedInTown() {
        return this.hasHuntedInCurrentTown;
    }



    public String getHunterName() {
        return hunterName;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            System.out.println("As your money reached below 0, you became broke and sadly that's the end for you, goodbye :3");
            System.exit(0);
        }

    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem == -1 || gold < costOfItem || hasItemInKit(item)) {
            return false;
        }

        if ((TreasureHunter.hunter).hasItemInKit("sword")) {
            addItem(item);
        } else {
            gold -= costOfItem;
            addItem(item);
        }
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    public boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }
    public boolean addTreasure(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInCollectedTreasure();
            collectedTreasure[idx] = item;
            return true;
        }
        return false;
    }


    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    public boolean hasTreasure(String item) {
        for (String tmpItem : collectedTreasure) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    public String getTreasure() {
        String printableKit = "";
        String space = " ";

        for (String item : collectedTreasure) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    public void populateKitForTestMode() {
        addItem("water");
        addItem("rope");
        addItem("machete");
        addItem("horse");
        addItem("boat");
        addItem("boots");
        addItem("shovel");
    }
    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + Colors.YELLOW + gold + Colors.RESET + " gold";
        if (!kitIsEmpty()) {
            str += " and " + Colors.PURPLE + getInventory() + Colors.RESET;
        }
        if (!treasureIsEmpty()) {
            str += "\nTreasures found: " + Colors.GREEN + getTreasure() + Colors.RESET;
        } else {
            // If there are no treasures found, display 'none'
            str += "\nTreasures found: none";
        }
        return str;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }
    private boolean treasureIsEmpty() {
        for (String string : collectedTreasure) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int emptyPositionInCollectedTreasure() {
        for (int i = 0; i < collectedTreasure.length; i++) {
            if (collectedTreasure[i] == null) {
                return i;
            }
        }
        return -1;
    }
}