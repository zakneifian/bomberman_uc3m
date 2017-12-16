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
	
}
