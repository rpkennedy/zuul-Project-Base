import java.util.ArrayList; 
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    Room office, reception, entrance, infirmary;
    Room closet, breakroom, admission, morgue;
    Room security, facility, lobby, medical;
    Room research, server, conference, bathroom;
    Room director, lab, waste, quarters;
    ArrayList<Item> inventory = new ArrayList<Item>();
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {      
        // create the rooms
        office = new Room("in a rotted office that smells of dry blood");
        reception = new Room("in the reception to an office, everything's smashed");
        entrance = new Room("very close to danger, time to move");
        infirmary = new Room("in an infirmary, and some medical supplies \nseem intact. You take them for good measure");
        
        closet = new Room("in a cramped space with a body, but scavenge a \nnightstick. It's better than nothing");
        breakroom = new Room("in the staff break room. No coffee");
        admission = new Room("in the general admission, but the place looks hollow");
        morgue = new Room("in the... morgue? What is this place?");
        
        security = new Room("at a security checkpoint, what is this place hiding?");
        facility = new Room("at the Facility Wing junction");
        lobby = new Room("in the main lobby, there's some blood, but there's no going back");
        medical = new Room("at the Medical Wing junction");
        
        research = new Room("in the main research facility. What were they trying to do?");
        server = new Room("in a room lined with servers, whoever was here meant business");
        conference = new Room("in the conference room. Amongst the sprawled chairs are corpses in lab-coats");
        bathroom = new Room("in the bathroom. In the mirror, you see how broken and tired you are");
        
        director = new Room("in the Director's Office, and he's got some \nkeys on his desk. You take them without thinking");
        lab = new Room("in the main lab. You find someone's old hunting knife, \nprobably sentimental to them. You take it");
        waste = new Room("at the waste disposal area. The chute is locked");
        quarters = new Room("in the quarters of the scientists. You find an \nintact first-aid kit and bring it with you");
        
        // initialise room exits
        entrance.setExit("east", admission);
        
        admission.setExit("east", lobby);
        admission.setExit("west", entrance);
        
        lobby.setExit("west", admission);
        lobby.setExit("north", medical);
        lobby.setExit("south", facility);
        
        medical.setExit("south", lobby);
        medical.setExit("west", morgue);
        
        morgue.setExit("east", medical);
        morgue.setExit("west", infirmary);
        
        infirmary.setExit("east", morgue);
        
        facility.setExit("north", lobby);
        facility.setExit("west", reception);
        facility.setExit("south", security);
        
        reception.setExit("east", facility);
        reception.setExit("south", office);
        
        office.setExit("north", reception);
        office.setExit("east", closet);
        
        closet.setExit("west", office);
        
        security.setExit("north", facility);
        security.setExit("east", research);
        
        research.setExit("east", director);
        research.setExit("north", server);
        
        director.setExit("west", research);
        
        server.setExit("south", research);
        server.setExit("north", conference);
        server.setExit("east", lab);
        
        lab.setExit("west", server);
        
        conference.setExit("south", server);
        conference.setExit("east", waste);
        conference.setExit("north", bathroom);
        
        bathroom.setExit("south", conference);
        bathroom.setExit("east", quarters);
        
        quarters.setExit("west", bathroom);

        currentRoom = entrance;  // start game outside
    }
    
    public void checkForItems(){
        Item heal1, heal2, keys;
        Item weap1, weap2;
        
        if (currentRoom == infirmary){
            heal1 = new Item("Medic Bag", 0);
            inventory.add(heal1);
        }
        
        if (currentRoom == quarters){
            heal2 = new Item("First-Aid Kit", 0);
            inventory.add(heal2);
        }
            
        if (currentRoom == closet){
            weap1 = new Item("Nightstick", 1);
            inventory.add(weap1);
        }
            
        if (currentRoom == lab){
            weap2 = new Item("Hunting Knife", 1);
            inventory.add(weap2);
        }
            
        if (currentRoom == director){
            keys = new Item("Keys", 0);
            inventory.add(keys);
        }
    }
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Tired and broken, you stumble out of the \nwoods to find a facility before you \nThe creature hunting you screeches closeby");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case LOOK:
                System.out.println(currentRoom.getLongDescription());
                break;
                
            case INVENTORY:
                printItems();
                break;
        }
        return wantToQuit;
    }

    public void printItems(){
        if (inventory.size() == 0)
            System.out.println("You haven't found anything worth keeping yet");
        else
            for (int i = 0; i < inventory.size(); i++)
                System.out.println(inventory.get(i).getDescription());
    }
    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Wound the creature twice and it dies");
        System.out.println("Heal items let you survive encounters");
        System.out.println("[the exit requires keys]");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            checkForItems();
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
