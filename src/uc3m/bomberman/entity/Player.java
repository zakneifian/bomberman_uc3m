package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;
//TODO esta clase no está hecha. Solo tiene definido el constructor para poder probarla
public class Player extends MovableEntity{
	private int bombs = 1;
	private int maxBombs = 1;
	private int range = 1;
	private String name = "bomberman";
	private int score = 0;
	
	public Player(int id, String name){
		super(id, "bomberman111.png", 100, new Coordinates(10, 10)); //TODO las coordenadas iniciales tienen que ser en el sitio adecuado
		this.name = name;
	}

	@Override
	public void onCollision(Entity col) {
		// TODO Colisión con otras entidades		
	}
}
