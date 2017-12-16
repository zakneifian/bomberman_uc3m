package uc3m.bomberman.entity;

import uc3m.bomberman.map.Coordinates;

public class Balloon extends Enemy {

	public Balloon(int id, Coordinates position) {
		super(id, "enemy111.png", 50, position, 3, 10);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollision(Entity col) {
		// TODO Auto-generated method stub
		
	}
	
}