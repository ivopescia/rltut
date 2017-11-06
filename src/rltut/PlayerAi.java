package rltut;

import java.util.List;

public class PlayerAi extends CreatureAi {
	
	private List<String> messages;
	public FieldOfView fov;
	
	public PlayerAi(Creature creature, List<String> messages, FieldOfView fov) {
		super(creature);
		this.messages = messages;
		this.fov = fov;
	}
	
	public void onEnter(int x, int y, int z, Tile tile){
	    if (tile.isGround()){
	        creature.x = x;
	        creature.y = y;
	    } else if (tile.isDiggable()) {
	        creature.dig(x, y, z);
	    }
	}
	
	public void onNotify(String message) {
		messages.add(message);
	}
	
	public boolean canSee(int wx, int wy, int wz) {
		return fov.isVisible(wz, wy, wz);
	}
	
}
