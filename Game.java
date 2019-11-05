import java.util.*;
/**
 *  You ever been chased through the woods by a creature unknown
 *  to mankind? Me neither. Better learn quick
 * 
 * @author  Ryan Kennedy
 * @version 11.04.2019
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
    ArrayList<Room> monster = new ArrayList<Room>();
    int counter = 0;
    boolean wounded = false;
    Item heal1 = new Item("Medic Bag");
    Item heal2 = new Item("First-Aid Kit");
    Item weap1 = new Item("Nightstick");
    Item weap2 = new Item("Hunting Knife");
    Item keys = new Item("Keys");
    
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
        
        research = new Room("in the main research facility. The door locked behind you,\nyet the creature can get in the vents");
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

        monster.add(waste);
        monster.add(waste);
        monster.add(waste);    //monster is at end for first three turns
        currentRoom = entrance;  // start game outside
    }
    
    /**
     * Called every game update. Checks if current room is one with designated
     * spawn. If so, adds item to inventory
     */
    public void checkForItems(){
        
        
        if (currentRoom == infirmary){
            inventory.add(heal1);
            heal1.pickup();
        }
        
        if (currentRoom == quarters){
            inventory.add(heal2);
            heal2.pickup();
        }
            
        if (currentRoom == closet){
            inventory.add(weap1);
            weap1.pickup();
        }
            
        if (currentRoom == lab){
            inventory.add(weap2);
            weap2.pickup();
        }
            
        if (currentRoom == director){
            inventory.add(keys);
            keys.pickup();
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

    /**
     * Print what items you have if any to terminal
     */
    public void printItems(){
        if (inventory.size() == 0)
            System.out.println("You haven't found anything worth keeping yet");
        else
            for (int i = 0; i < inventory.size(); i++)
                System.out.println(inventory.get(i).getDescription());
    }
    // implementations of user commands:

    /**
     * Here I printed some stupid, cryptic message and a list of the 
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
            monster.add(currentRoom);
            currentRoom = nextRoom;
            checkForItems();
            checkLeave();
            counter++;
            if (currentRoom == monster.get(counter) || currentRoom == monster.get(counter-1))
                encounter();
            else
                System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Check every game update whether the player is in waste disposal with
     * the key or not. If so, game-win scenario plays
     */
    public void checkLeave(){
       if (keys.has() == true && currentRoom == waste)
       System.out.println("With the keys to the locked chute, you open\nit up and climb through. You've survived the ordeal\nPlease QUIT");
    }
    
    /**
     * The method for determining what happens when the player and creature meet
     */
    public void encounter(){
        if (weap1.has() == false && weap2.has() == false)
            if(heal1.has() == false && heal2.has() == false)
            System.out.println("With nothing to defend or heal yourself with, \nthe creature overcomes you\nPlease QUIT");
            else if (heal1.has() == true){
            System.out.println("The creature corners you and you escape with injuries\nusing medical supplies to heal");
            heal1.drop();}
            else if (heal2.has() == true){
            System.out.println("The creature corners you and you escape with injuries\nusing medical supplies to heal");
            heal2.drop();}    
        else if (wounded == true)
            System.out.println("Coming across the wounded creature, you finish it with your weapon\nFinally able to move on from this tragedy, you escape\nPlease QUIT");
            
        else if (weap1.has() == true){
            System.out.println("Using what you had picked up, you stun the creature \nwith an attack and slip by");
            weap1.drop();
            wounded = true;
            }
        else if (weap2.has() == true){
            System.out.println("Using what you had picked up, you stun the creature \nwith an attack and slip by");
            weap2.drop();
            wounded = true;
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
