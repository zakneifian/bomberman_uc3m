package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;

public class Player extends MovableEntity{
	private int bombs = 1;
	private int maxBombs = 1;
	private int range = 2;
	private String name = "bomberman";
	private int score = 0;
	private int damage = 10;
	private boolean remote = false;

	private boolean enemiesAlive = true;
	private boolean openedDoorOnTouch = false;
	
	public Player(int id, String name){
		super(id, "bomberman111.png", 100, new Coordinates(10, 10), 5); //5 es la cantidad de sprites que tiene por direccion de movimiento
		this.name = name;												//ej, el slime/balloon seria 3.
	}
	
	public void resetPlayerPos() {
		position = new Coordinates(10, 10);
	}
	@Override
	public void onCollision(Entity col) {
		if(col instanceof Enemy && alive){
			col.takeDamage(this.getDamage());
		}
	}

	public void upgrade(String type){
		switch(type){
		case "bomb":
			maxBombs++;
			bombs++;
			break;
		case "fire":
			if(++range > 5) range--;
			break;
		case "special":
			range = 5;
			break;
		case "remote":
			remote = true;
			break;
		case "skate":
			if(++speed > 10) speed--;
			break;
		case "geta":
			speed = 1;
			break;
		case "door":
			if (!enemiesAlive) {
				openedDoorOnTouch = true;
			}
			
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean putBomb(){
		if(bombs <= 0)
			return false;
		bombs--;
		return true;
	}
	public void bombExploded(){
		bombs++;
		if(bombs > maxBombs)
			bombs = maxBombs;
	}
	public boolean isRemote(){
		return remote;
	}
	public int getBombs() {
		return bombs;
	}
	public int getDamage(){
		return damage;
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

	public boolean getEnemiesAlive() {
		return enemiesAlive;
	}

	public void setEnemiesAlive(boolean enemiesAlive) {
		this.enemiesAlive = enemiesAlive;
	}
	
	public boolean getOpenedDoorOnTouch() {
		return openedDoorOnTouch;
	}

	public void setOpenedDoorOnTouch(boolean openedDoorOnTouch) {
		this.openedDoorOnTouch = openedDoorOnTouch;
	}
}
