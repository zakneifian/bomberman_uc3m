package uc3m.bomberman.entity;

import uc3m.bomberman.map.*;

public class Entity{
	private int id = 1;
	private String sprite;
	private int maxHp = 1;
	
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
		this.position = position; //TODO poner por defecto en la primera casilla libre
	}
	/**
	 * Checks collision with another entity
	 * @param col <code>{@link Entity}</code> to check colision
	 * @return True if the current entity collides with the parameter; false otherwise
	 */
	public boolean collides(Entity col){
		return col.getPosition().x == this.position.x && col.getPosition().y == this.position.y;
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
	
	//Damage
	public void takeDamage(int dmg){
		if(dmg > 0){
			hp -= dmg;
			if(hp <= 0){
				alive = false;
				hp = 0;
			}
		}
	}
	public void takeHeal(int heal){
		if(heal > 0){
			hp += heal;
			if(hp > maxHp){
				hp = maxHp;
			}
		}
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
	public Coordinates getPosition(){
		return new Coordinates(position.x, position.y);
	}
	public int getId(){
		return id;
	}
}
