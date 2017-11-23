package uc3m.bomberman.entity;

import uc3m.bomberman.map.Coordinates;

public class Entity{
	private int id = 1;
	private Coordinates position;
	private String spritePath;
	private int hp;
	private int maxHp = 1;
	private boolean alive = true;
	//spritePath es la base del nombre de la imagen, es decir, "images/bomberman", por ejemplo
	public Entity(int id, String spritePath, int maxHp){
		
	}
}
