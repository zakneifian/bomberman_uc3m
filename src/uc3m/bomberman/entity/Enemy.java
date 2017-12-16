package uc3m.bomberman.entity;

import uc3m.bomberman.map.Coordinates;

public abstract class Enemy extends MovableEntity{
	int damagetoPlayer;
	public Enemy(int id, String path, int maxHp, Coordinates position, int spriteRgQty, int damageToPlayer, int intelligence) {
		super(id, path, maxHp, position, spriteRgQty);
		this.damagetoPlayer = damageToPlayer;
	}
	public int getDamagetoPlayer() {
		return damagetoPlayer;
	}
	
	@Override
	public void animateMovement(int spritePhase, String[] entityDir) {
		if (entityDir[1].equals(entityDir[0])) { //if the present movement equals the past one
			if (getSpritePhase() < getSpriteRgQty()) { //if the phase is lower than its range limit of movement
				setSpritePhase(getSpritePhase() + 1);  // sum one to the phase 
				setSprite(getSprite().substring(0, getSprite().length() - 5) + getSpritePhase()  + ".png"); // and change the sprite summing one to the last digit pf the path
			}
			else {
				setSpritePhase(1); //else, set the sprite phase to 0 and change the sprite to the first one, the 0
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
		}
		else {
			setSpritePhase(1);
			if	    (entityDir[1].equals("up"))    {
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
			else if (entityDir[1].equals("left"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "11.png");
			}
			else if (entityDir[1].equals("down"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
			else if (entityDir[1].equals("right")) {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "21.png");
			}
		}
		
	}
	
}
