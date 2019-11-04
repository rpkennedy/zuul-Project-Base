
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
    private int weapon; //1 for yes, 0 for no. used for calculating monster death
    /**
     * Constructor for objects of class Item
     * 
     * @param description The name of the item element
     * @param weapon The binary value of whether it is weaponized
     */
    public Item(String description, int weapon)
    {
        // initialise instance variables
        this.description = description;
        this.weapon = weapon;
        has = true;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String getDescription()
    {
        return description;
    }
}
