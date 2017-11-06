package rltut;

import java.awt.Color;
import java.text.Format;

public class Creature {
	private World world;
	
	public int x;
	public int y;
	public int z;
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }
	
	private int maxHp;
	public int maxHp() { return maxHp; }
	
	private int hp;
	public int hp() { return hp; }
	
	private int attackValue;
	public int attackValue() { return attackValue; }
	
	public int defenseValue;
	public int defenseValue() { return defenseValue; }
	
	private int visionRadius;
	public int visionRadius() { return visionRadius; }	
	
	public Creature (World world, char glyph, Color color, int maxHp, int attack, int defense) {
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
	}
	
	private CreatureAi ai;
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	
	public void dig(int wx, int wy, int wz) {
	    world.dig(wx, wy, wz);
	}
	
	public void moveBy(int mx, int my, int mz){
        Tile tile = world.tile(x+mx, y+my, z+mz);
        
        if (mz == -1){
            if (tile == Tile.STAIRS_DOWN) {
                doAction("walk up the stairs to level %d", z+mz+1);
            } else {
                doAction("try to go up but are stopped by the cave ceiling", null);
                return;
            }
        } else if (mz == 1){
            if (tile == Tile.STAIRS_UP) {
                doAction("walk down the stairs to level %d", z+mz+1);
            } else {
                doAction("try to go down but are stopped by the cave floor", null);
                return;
            }
        }
		
		Creature other = world.creature(x+mx,  y+my, z+mz);
        if (other == null)
            ai.onEnter(x+mx, y+my, z+mz, tile);
        else
            attack(other);
	}
	
	public void attack(Creature other) {
		int amount = Math.max(0,  attackValue() - other.defenseValue());
		
		amount = (int)(Math.random() * amount) + 1;
		
		other.modifyHp(-amount);
		doAction(String.format("attack the '%s' for %d damage", other.glyph, amount), null);
	}
	
	public void modifyHp(int amount) {
		hp += amount;
		
		if (hp < 1) {
			doAction("Die", null);
			world.remove(this);
		}
	}

	public void update() {
		ai.onUpdate();
	}
	
	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}
	
	public void notify(String message, Object params) {
		ai.onNotify(String.format(message,  params));
	}
	
	public void doAction(String message, Object ... params){
	    int r = 9;
	    for (int ox = -r; ox < r+1; ox++){
	        for (int oy = -r; oy < r+1; oy++){
	            if (ox*ox + oy*oy > r*r)
	                continue;
	         
	            Creature other = world.creature(x+ox, y+oy, z);
	         
	            if (other == null)
	                continue;
	         
	            if (other == this)
	                other.notify("You " + message + ".", params);
	            else if (other.canSee(x, y, z))
	                other.notify(String.format("The %s %s.", glyph, makeSecondPerson(message)), params);
	         }
	    }
	}
	
	private String makeSecondPerson(String text){
	    String[] words = text.split(" ");
	    words[0] = words[0] + "s";
	    
	    StringBuilder builder = new StringBuilder();
	    for (String word : words){
	        builder.append(" ");
	        builder.append(word);
	    }
	    
	    return builder.toString().trim();
	}
	
	public boolean canSee(int wx, int wy, int wz) {
		return ai.canSee(wx, wy, wz);
	}
	
	public Tile tile(int wx, int wy, int wz) {
		return world.tile(wx,  wy,  wz);
	}
}
