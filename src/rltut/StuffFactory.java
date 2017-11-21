package rltut;

import java.util.List;

import asciiPanel.AsciiPanel;

public class StuffFactory {
	private World world;
	
	public StuffFactory(World world) {
		this.world = world;
	}
	
	public Creature newPlayer(List<String> messages, FieldOfView fov) {
	    Creature player = new Creature(world, '@', AsciiPanel.brightWhite, "player", 100, 8, 5);
	    world.addAtEmptyLocation(player, 0);
	    new PlayerAi(player, messages, fov);
	    return player;
	}
	
	public Creature newFungus(int depth) {
		Creature fungus = new Creature(world, 'f', AsciiPanel.green, "fungus", 10, 0, 0);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	public Creature newBat(int depth) {
		Creature bat = new Creature(world, 'b', AsciiPanel.yellow, "bat", 15, 5, 0);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	public Item newRock(int depth) {
		Item rock = new Item(',', AsciiPanel.yellow, "rock");
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	public Item newVictoryItem(int depth) {
		Item victoryItem = new Item('*', AsciiPanel.brightWhite, "teddy bear");
		world.addAtEmptyLocation(victoryItem, depth);
		return victoryItem;
	}
	
	public Item newApple(int depth) {
		Item apple = new Item('%', AsciiPanel.brightRed, "apple");
		apple.modifyFoodValue(100);
		world.addAtEmptyLocation(apple, depth);
		return apple;
	}
	
	public Item newBread(int depth) {
		Item bread = new Item('%', AsciiPanel.yellow, "bread");
		bread.modifyFoodValue(200);
		world.addAtEmptyLocation(bread, depth);
		return bread;
	}

}
