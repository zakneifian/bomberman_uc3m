package uc3m.bomberman.entity;

import uc3m.bomberman.main.Main;
import uc3m.bomberman.map.*;

/**
 * This class extends <code>{@link Entity}</code>
 * and holds methods regarding the Bomb object such as the tick animation
 * and values such as ticks for explosion, etc
 *
 */
public class Bomb extends Entity{
	private final int TICK_TIME = 100;
	private int maxTicks = (int) (Main.BOMB_TIME/TICK_TIME);
	private long time;
	private boolean exploded = false;
	
	/**
	 * Full constructor
	 * @param id
	 * @param pos
	 */
	public Bomb(int id, Coordinates pos) {
		super(id, "bomb1.gif", -1, pos);
		time  = System.currentTimeMillis();
	}
	
	/**
	 * Changes the bomb sprite for animation purposes
	 * @return true if time for ticks is over
	 */
	public boolean tick(){
		if(System.currentTimeMillis() - time > TICK_TIME){
			if(this.getSprite().equals("bomb1.gif")){
				this.setSprite("bomb2.gif");
			}else{
				this.setSprite("bomb1.gif");
			}
			time = System.currentTimeMillis();
			if(--maxTicks <= 0){
				return true;
			}
		}
		return exploded;
	}
	
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.Entity#takeDamage(int)
	 */
	@Override
	public void takeDamage(int dmg){
		exploded = true;
	}
}