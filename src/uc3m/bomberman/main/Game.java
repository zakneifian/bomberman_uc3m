package uc3m.bomberman.main;

import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Game{
	private Map[] map = new Map[15];
	private Entity[] entities;
	private Player player;
	private int level = 0;
	private int enemyCount;
	
	public Game(int dim, String playerName){
		for(int ii = 0; ii < map.length; ii++){
			map[ii] = new Map(dim);
		}
		player = new Player(Main.nextId(), playerName);
		entities = new Entity[1];
		entities[0] = player;
	}
	public Map getMap(){
		return map[level];
	}
	/**
	 * retorna si se ha podido añadir la entidad o no (si la id ya existe o no)
	 * @param entity
	 * @return
	 */
	public boolean nextMap(){
		//TODO implementar
		return true;
	}
	public boolean addEntity(Entity entity){
		for(int ii = 0; ii < entities.length; ii++){
			if(entities[ii].getId() == entity.getId())
				return false;
		}		
		//Crea una nueva array con la nueva entidad
		Entity[] arr = new Entity[entities.length+1];
		System.arraycopy(entities, 0, arr, 0, entities.length);
		arr[arr.length-1] = entity;
		entities = arr;
		return true;
	}
	/**
	 * retorna si se ha podido eliminar la entidad o no
	 * @param id
	 * @return
	 */
	public boolean removeEntity(int id){
		for(int ii = 0; ii < entities.length; ii++){
			if(entities[ii].getId() == id){
				Entity[] arr = new Entity[entities.length-1];
				System.arraycopy(entities, 0, arr, 0, ii);
				System.arraycopy(entities, ii+1, arr, ii, arr.length-ii);
				entities = arr;
				return true;
			}
		}
		return false;
	}
	public Entity[] getEntities(){
		return entities;
	}
	public Player getPlayer(){
		return player;
	}
}
