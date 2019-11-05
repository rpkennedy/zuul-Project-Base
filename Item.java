
/**
 * An item in a room that can be picked up by the player. Is either a healing
 * item or a weapon. Heals allow you to survive monster encounters, while weapons
 * allow you to do the same while injuring the monster. All items are one time
 * uses.
 *
 * @author Ryan Kennedy
 * @version 11.04.2019
 */
public class Item
{
    private String description;
    private boolean has;
    /**
     * Constructor for objects of class Item
     * 
     * @param description The name of the item element
     */
    public Item(String description)
    {
        // initialise instance variables
        this.description = description;
        has = false;
    }

    /**
     * Set 'has' boolean to true on pickup
     */
    public void pickup(){
        this.has = true;
    }
    /**
     * Set 'has' boolean to false on use/drop
     */
    public void drop(){
        this.has = false;
    }
    /**
     * Check if item is in inventory or not
     */
    public boolean has(){
        return has;
    }
    /**
     * Returns the item description
     * @return  description  Item name
     */
    public String getDescription()
    {
        return description;
    }
}
