package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;
//TODO esta clase no esta¡ hecha. Solo tiene definido el constructor para poder probarla
public class Player extends MovableEntity{
	private int bombs = 1;
	private int maxBombs = 1;
	private int range = 2;

	private String name = "bomberman";
	private int score = 0;
	
	private int movingPhase = 0;

	public Player(int id, String name){
		super(id, "bomberman111.png", 100, new Coordinates(10, 10)); //TODO las coordenadas iniciales tienen que ser en el sitio adecuado
		this.name = name;
	}
	
	public void resetPlayerPos() {
		position = new Coordinates(10, 10);
	}
	@Override
	public void onCollision(Entity col) {
		// TODO Colision con otras entidades		
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getBombs() {
		return bombs;
	}

	public int getMaxBombs() {
		return maxBombs;
	}

	public int getRange() {
		return range;
	}

	public int getScore() {
		return score;
	}
	public String getName(){
		return name;
	}
}
