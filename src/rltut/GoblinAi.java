package rltut;

import java.util.List;

public class GoblinAi extends CreatureAi {
	private Creature player;
	
	public GoblinAi(Creature creature, Creature player) {
		super(creature);
		this.player = player;
	}
	
	public void onUpdate() {
		
		if (canRangedWeaponAttack(player))
			creature.rangedWeaponAttack(player);
		else if (canThrowAt(player)) {
			creature.throwItem(getWeaponToThrow(), player.x, player.y, player.z);
		}
		else if (creature.canSee(player.x, player.y, player.z)) {
			hunt(player);
		}
		else if (canPickup())
			creature.pickup();
		else
			wander();
	}
	
	public void hunt(Creature target) {
		List<Point> points = new Path(creature, target.x, target.y).points();
		
		int mx = points.get(0).x - creature.x;
		int my = points.get(0).y - creature.y;
		
		creature.moveBy(mx, my, 0);
	}
	
	private boolean canRangedWeaponAttack(Creature other) {
		return creature.weapon() != null
				&& creature.weapon().rangedAttackValue() > 0
				&& getWeaponToThrow() != null;
	}
	
    private boolean canThrowAt(Creature other) {
        return creature.canSee(other.x, other.y, other.z)
          && getWeaponToThrow() != null;
    }
	
	private Item getWeaponToThrow() {
		Item toThrow = null;
		
		for (Item item : creature.inventory().getItems()) {
			if (item == null || creature.weapon() == item || creature.armor() == item)
				continue;
			
			if (toThrow == null || item.thrownAttackValue() > toThrow.attackValue())
				toThrow = item;
		}
		
		return toThrow;
	}
	
	
	private boolean canPickup() {
		return creature.item(creature.x, creature.y, creature.z) != null
				&& !creature.inventory().isFull();
	}
}
