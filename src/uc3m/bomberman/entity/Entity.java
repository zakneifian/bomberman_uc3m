package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;
/**
 * This class represents an entity in the game, with and id and a position. Entities do not necessarily move.
 * @author Daniel Alacide Nombela, Alejandro Martínez Riera
 * @see <code>{@link MovableEntity}</code>
 *
 */
public class Entity{
	private int id = 1;
	private String sprite;
	protected int maxHp = 1;	
	protected Coordinates position;
	protected int hp;
	protected boolean alive = true;
	/**
	 * Creates an entity, that must have an id, a sprite and a max health
	 * 
	 * By default, the entity will be placed in the square (1, 1)
	 * 
	 * @param id UNIQUE number identifying the Entity
	 * @param path Sprite path
	 * @param maxHp Maximum health
	 * @param position Initial position of the Entity
	 */
	public Entity(int id, String sprite, int maxHp, Coordinates position){
		this.id = id;
		this.sprite = sprite;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.position = position;
	}
	/**
	 * Checks collision with another entity
	 * @param col <code>{@link Entity}</code> to check colision
	 * @return True if the current entity collides with the parameter; false otherwise
	 */
	public boolean collides(Entity col){
		return col.getPosition().tenthsToUnits().x == this.position.tenthsToUnits().x && col.getPosition().tenthsToUnits().y == this.position.tenthsToUnits().y;
	}
	/**
	 * Checks the collisions with the map
	 * @param map <code>{@link Map}</code> to check the collisions with
	 * @return True if the current entity is INSIDE a non-walkable tile
	 */
	public boolean collides(Map map){
		Coordinates simplePos = position.tenthsToUnits();
		return !map.isWalkableAt(simplePos);
	}
	
	/**
	 * Damages the entity by <code>dmg</code> health points
	 * @param dmg
	 * @return true if the entity was killed, false otherwise
	 */
	public void takeDamage(int dmg){
		if(dmg > 0){
			hp -= dmg;
			if(hp <= 0){
				alive = false;
				hp = 0;
			}
		}
	}
	/**
	 * Set the entity dead
	 */
	public void kill(){
		alive = false;
	}
	
	/**
	 * Heals the entity by <code>heal</code> health points
	 * @param heal
	 */
	public void takeHeal(int heal){
		if(heal > 0){
			hp += heal;
			if(hp > maxHp){
				hp = maxHp;
			}
		}
	}
	/**
	 * Two entities are the same if their id is the same
	 * @param other
	 * @return
	 */
	public boolean equals(Entity other){
		return this.id == other.id;
	}
	
	//Setters
	protected void setSprite(String path){
		this.sprite = path;
	}
	//Getters
	public boolean isAlive(){
		return alive;
	}
	public String getSprite(){
		return sprite;
	}
	/**
	 * @return NEW <code>{@link Coordinates}</code> object. Changing the returned object will not change the entitie's position
	 */
	public Coordinates getPosition(){
		return new Coordinates(position.x, position.y);
	}
	public int getId(){
		return id;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public int getHp() {
		return hp;
	}
	//toString
	public String toString(){
		return ""+id;
	}

}
