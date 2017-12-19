package uc3m.bomberman.entity;

import uc3m.bomberman.main.Main;
import uc3m.bomberman.map.*;
//TODO javadoc
public class Bomb extends Entity{
	private final int TICK_TIME = 100;
	private int maxTicks = (int) (Main.BOMB_TIME/TICK_TIME);
	private long time;
	
	public Bomb(int id, Coordinates pos) {
		super(id, "bomb1.gif", -1, pos);
		time  = System.currentTimeMillis();
	}
	
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
		return false;
	}
	
	@Override
	public void takeDamage(int dmg){
	}
}