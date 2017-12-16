package uc3m.bomberman.entity;

import uc3m.bomberman.map.Coordinates;

public class Slime extends Enemy {

	public Slime(int id, Coordinates position) {
		super(id, "enemy211.png", 100, position, 3, 10);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollision(Entity col) {
		// TODO Auto-generated method stub
		
	}
	
}
