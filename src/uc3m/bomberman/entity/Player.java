package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;
/**
 * This class overrides some unimplemented
 * methods of <code>{@link MovableEntity}</code>
 * and creates the object for Player. It also holds
 * all the parameters and methods regarding to the player.
 *
 */
public class Player extends MovableEntity{
	private int bombs = 1;
	private int maxBombs = 1;
	private int range = 2;
	private String name = "bomberman";
	private int score = 0;
	private int damage = 10;
	private boolean remote = false;

	private boolean enemiesAlive = true;
	private boolean doorOnTouch = false;
	private boolean god = false;
	private boolean clip = true;
	
	/**
	 * Full constructor for the player
	 * @param id
	 * @param name
	 */
	public Player(int id, String name){
		super(id, "bomberman111.png", 100, new Coordinates(10, 10), 5); //5 es la cantidad de sprites que tiene por direccion de movimiento
		this.name = name;												//ej, el slime/balloon seria 3.
		this.speed = 2;
	}
	
	/**
	 * sets the Player position to the initial position in the up-leftmost position of the map
	 */
	public void resetPlayerPos() {
		position = new Coordinates(10, 10);
	}
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.MovableEntity#onCollision(uc3m.bomberman.entity.Entity)
	 */
	@Override
	public void onCollision(Entity col) {
	}
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.Entity#takeDamage(int)
	 */
	@Override
	public void takeDamage(int dmg){
		if(!god) super.takeDamage(dmg);
	}
	/* (non-Javadoc)
	 * @see uc3m.bomberman.entity.Entity#collides(uc3m.bomberman.map.Map)
	 */
	@Override
	public boolean collides(Map map){
		if(clip) return super.collides(map);
		return false;
	}
	/**
	 * toggles god cheat command (makes the player invulnerable
	 */
	public void toggleGod(){
		god = !god;
	}
	/**
	 * toggles noclip cheat command (makes the player able to ghost walk)
	 */
	public void toggleClip(){
		clip = !clip;
	}
	/**
	 * Handles what happens when the player touches an <code>{@link Upgrade}</code>
	 * @param type
	 */
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
			doorOnTouch = true;
		}
	}
	
	/**
	 * sets the name of the Player
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * adds score to the player
	 * @param score
	 */
	public void addScore(int score) {
		this.score += score;
	}
	/**
	 * @return true if Player is able to put a bomb (the Player needs to hold at least one bomb to put it)
	 */
	public boolean putBomb(){
		if(bombs <= 0)
			return false;
		bombs--;
		return true;
	}
	/**
	 *  when bombs explode, increase the current number of bombs that the player has
	 *  this number won't surpass the maximum amount of bombs
	 */
	public void bombExploded(){
		bombs++;
		if(bombs > maxBombs)
			bombs = maxBombs;
	}
	/**
	 * @return true if player has remote upgrade
	 */
	public boolean isRemote(){
		return remote;
	}
	/**
	 * @return the actual number of bombs that the player currently has
	 */
	public int getBombs() {
		return bombs;
	}
	/**
	 * @return the inflicted damage to the player
	 */
	public int getDamage(){
		return damage;
	}
	/**
	 * @return the max. amount of bombs that the player can hold at the moment
	 */
	public int getMaxBombs() {
		return maxBombs;
	}

	/**
	 * @return the range of the explosion of the bomb that the player sets
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @return the score of the player
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @return the name of the player
	 */
	public String getName(){
		return name;
	}

	/**
	 * @return true if there are enemies alive in the map
	 */
	public boolean getEnemiesAlive() {
		return enemiesAlive;
	}

	/**
	 * stores in a boolean var if there are enemies alive
	 * @param enemiesAlive
	 */
	public void setEnemiesAlive(boolean enemiesAlive) {
		this.enemiesAlive = enemiesAlive;
	}
	
	/**
	 * @return true if player is touching the door
	 */
	public boolean getDoorOnTouch() {
		return doorOnTouch;
	}

	/**
	 * sets true if Player is touching the door
	 * @param doorOnTouch
	 */
	public void setDoorOnTouch(boolean doorOnTouch) {
		this.doorOnTouch = doorOnTouch;
	}
}
