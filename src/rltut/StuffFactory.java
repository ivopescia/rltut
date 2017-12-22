package rltut;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import asciiPanel.AsciiPanel;

public class StuffFactory {
	private World world;
	
	private Map<String, Color> potionColors;
	private List<String> potionAppearances;
	
	public StuffFactory(World world) {
		this.world = world;
		
		setUpPotionAppearances();
	}
	
    private void setUpPotionAppearances(){
        potionColors = new HashMap<String, Color>();
        potionColors.put("red potion", AsciiPanel.brightRed);
        potionColors.put("yellow potion", AsciiPanel.brightYellow);
        potionColors.put("green potion", AsciiPanel.brightGreen);
        potionColors.put("cyan potion", AsciiPanel.brightCyan);
        potionColors.put("blue potion", AsciiPanel.brightBlue);
        potionColors.put("magenta potion", AsciiPanel.brightMagenta);
        potionColors.put("dark potion", AsciiPanel.brightBlack);
        potionColors.put("grey potion", AsciiPanel.white);
        potionColors.put("light potion", AsciiPanel.brightWhite);

        potionAppearances = new ArrayList<String>(potionColors.keySet());
        Collections.shuffle(potionAppearances);
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
	
	public Creature newZombie(int depth, Creature player) {
		Creature zombie = new Creature(world, 'z', AsciiPanel.white, "zombie", 35, 8, 3);
		world.addAtEmptyLocation(zombie, depth);
		new ZombieAi(zombie, player);
		return zombie;
	}
	
	public Creature newGoblin(int depth, Creature player) {
		Creature goblin = new Creature(world, 'g', AsciiPanel.brightGreen, "goblin", 42, 6, 2);
		goblin.equip(randomWeapon(depth));
		goblin.equip(randomArmor(depth));
		world.addAtEmptyLocation(goblin, depth);
		new GoblinAi(goblin, player);
		return goblin;
	}
	
	public Item newRock(int depth) {
		Item rock = new Item(',', AsciiPanel.yellow, "rock", null);
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	public Item newVictoryItem(int depth) {
		Item victoryItem = new Item('*', AsciiPanel.brightWhite, "teddy bear", null);
		world.addAtEmptyLocation(victoryItem, depth);
		return victoryItem;
	}
	
	public Item newApple(int depth) {
		Item apple = new Item('%', AsciiPanel.brightRed, "apple", null);
		apple.modifyFoodValue(100);
		world.addAtEmptyLocation(apple, depth);
		return apple;
	}
	
	public Item newBread(int depth) {
		Item bread = new Item('%', AsciiPanel.yellow, "bread", null);
		bread.modifyFoodValue(200);
		world.addAtEmptyLocation(bread, depth);
		return bread;
	}
	
	public Item newDagger(int depth) {
		Item dagger = new Item(')', AsciiPanel.white, "dagger", null);
		dagger.modifyAttackValue(5);
		world.addAtEmptyLocation(dagger, depth);
		return dagger;
	}
	
	public Item newSword(int depth) {
		Item sword = new Item(')', AsciiPanel.brightWhite, "sword", null);
		sword.modifyAttackValue(10);
		world.addAtEmptyLocation(sword, depth);
		return sword;
	}

	public Item newStaff(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "staff", null);
		item.modifyAttackValue(5);
		item.modifyDefenseValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newLightArmor(int depth){
		Item item = new Item('[', AsciiPanel.green, "tunic", null);
		item.modifyDefenseValue(2);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	  
	public Item newMediumArmor(int depth){
		Item item = new Item('[', AsciiPanel.white, "chainmail", null);
		item.modifyDefenseValue(4);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newHeavyArmor(int depth){
		Item item = new Item('[', AsciiPanel.brightWhite, "platemail", null);
		item.modifyDefenseValue(6);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newEdibleWeapon(int depth){
	    Item item = new Item(')', AsciiPanel.yellow, "baguette", null);
	    item.modifyAttackValue(3);
	    item.modifyFoodValue(50);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	 }
	
	public Item newBow(int depth){
        Item item = new Item(')', AsciiPanel.yellow, "bow", null);
        item.modifyAttackValue(1);
        item.modfiyRangedAttackValue(5);
        world.addAtEmptyLocation(item, depth);
        return item;
    }
	
	public Item newPotionOfHealth(int depth) {
		String appearance = potionAppearances.get(0);
		Item item = new Item('!', AsciiPanel.white, "health potion", appearance);
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature) {
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(15);
				creature.doAction("look healthier");
				creature.learnName(item);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfPoison(int depth) {
		String appearance = potionAppearances.get(1);
		Item item = new Item('!', AsciiPanel.white, "poison potion", appearance);
		item.setQuaffEffect(new Effect(20) {
			public void start(Creature creature) {
				creature.doAction("look sick");
			}
			
			public void update(Creature creature) {
				super.update(creature);
				creature.modifyHp(-1);
				creature.learnName(item);
			}
		});
		
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfWarrior(int depth){
		String appearance = potionAppearances.get(2);
	    Item item = new Item('!', AsciiPanel.white, "warrior's potion", appearance);
	    item.setQuaffEffect(new Effect(20){
	        public void start(Creature creature){
	            creature.modifyAttackValue(5);
	            creature.modifyDefenseValue(5);
	            creature.doAction("look stronger");
	            creature.learnName(item);
	        }
	        public void end(Creature creature){
	            creature.modifyAttackValue(-5);
	            creature.modifyDefenseValue(-5);
	            creature.doAction("look less strong");
	        }
	    });
	                
	    world.addAtEmptyLocation(item, depth);
	    return item;
	}
	
	
	public Item randomWeapon(int depth){
		switch ((int)(Math.random() * 4)){
		case 0: return newDagger(depth);
		case 1: return newSword(depth);
		case 2: return newBow(depth);
		default: return newStaff(depth);
		}
	}

	public Item randomArmor(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newLightArmor(depth);
		case 1: return newMediumArmor(depth);
		default: return newHeavyArmor(depth);
		}
	}
	
	public Item randomPotion(int depth){
        switch ((int)(Math.random() * 3)){
        case 0: return newPotionOfHealth(depth);
        case 1: return newPotionOfPoison(depth);
        default: return newPotionOfWarrior(depth);
        }
	}
	
	public Item randomSpellBook(int depth){
        switch ((int)(Math.random() * 3)){
        case 0: return newWhiteMagesSpellbook(depth);
        case 1: return newBlueMagesSpellbook(depth);
        default: return newWhiteMagesSpellbook(depth);
        }
	}
	
	public Item newWhiteMagesSpellbook(int depth) {
		Item item = new Item ('+', AsciiPanel.brightWhite, "white mage's spellbook", null);
		item.addWrittenSpell("minor heal", 4, new Effect(1) {
			public void start(Creature creature) {
				if (creature.hp() == creature.maxHp())
					return;
				
				creature.modifyHp(20);
				creature.doAction("look healthier");
			}
		});
		
        item.addWrittenSpell("major heal", 8, new Effect(1){
            public void start(Creature creature){
                if (creature.hp() == creature.maxHp())
                    return;
                
                creature.modifyHp(50);
                creature.doAction("look healthier");
            }
        });
        
        item.addWrittenSpell("slow heal", 12, new Effect(50){
            public void update(Creature creature){
                super.update(creature);
                creature.modifyHp(2);
            }
        });
        
        item.addWrittenSpell("inner strength", 16, new Effect(50){
            public void start(Creature creature){
                creature.modifyAttackValue(2);
                creature.modifyDefenseValue(2);
                creature.modifyVisionRadius(1);
                creature.modifyRegenHpPer1000(10);
                creature.modifyRegenManaPer1000(-10);
                creature.doAction("seem to glow with inner strength");
            }
            public void update(Creature creature){
                super.update(creature);
                if (Math.random() < 0.25)
                    creature.modifyHp(1);
            }
            public void end(Creature creature){
                creature.modifyAttackValue(-2);
                creature.modifyDefenseValue(-2);
                creature.modifyVisionRadius(-1);
                creature.modifyRegenHpPer1000(-10);
                creature.modifyRegenManaPer1000(10);
            }
        });
        
        world.addAtEmptyLocation(item, depth);
        return item;
	}
	
	public Item newBlueMagesSpellbook(int depth) {
        Item item = new Item('+', AsciiPanel.brightBlue, "blue mage's spellbook", null);

        item.addWrittenSpell("blood to mana", 1, new Effect(1){
            public void start(Creature creature){
                int amount = Math.min(creature.hp() - 1, creature.maxMana() - creature.mana());
                creature.modifyHp(-amount);
                creature.modifyMana(amount);
            }
        });
        
        item.addWrittenSpell("blink", 6, new Effect(1){
            public void start(Creature creature){
                creature.doAction("fade out");
                
                int mx = 0;
                int my = 0;
                
                do
                {
                    mx = (int)(Math.random() * 11) - 5;
                    my = (int)(Math.random() * 11) - 5;
                }
                while (!creature.canEnter(creature.x+mx, creature.y+my, creature.z)
                        && creature.canSee(creature.x+mx, creature.y+my, creature.z));
                
                creature.moveBy(mx, my, 0);
                
                creature.doAction("fade in");
            }
        });
        
        item.addWrittenSpell("summon bats", 11, new Effect(1){
            public void start(Creature creature){
                for (int ox = -1; ox < 2; ox++){
                    for (int oy = -1; oy < 2; oy++){
                        int nx = creature.x + ox;
                        int ny = creature.y + oy;
                        if (ox == 0 && oy == 0 
                                || creature.creature(nx, ny, creature.z) != null)
                            continue;
                        
                        Creature bat = newBat(0);
                        
                        if (!bat.canEnter(nx, ny, creature.z)){
                            world.remove(bat);
                            continue;
                        }
                        
                        bat.x = nx;
                        bat.y = ny;
                        bat.z = creature.z;
                        
                        creature.summon(bat);
                    }
                }
            }
        });
        
        item.addWrittenSpell("detect creatures", 16, new Effect(75){
            public void start(Creature creature){
                creature.doAction("look far off into the distance");
                creature.modifyDetectCreatures(1);
            }
            public void end(Creature creature){
                creature.modifyDetectCreatures(-1);
            }
        });
        world.addAtEmptyLocation(item, depth);
        return item;
    }
}
