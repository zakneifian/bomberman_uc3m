package uc3m.bomberman.main;

import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Game{
	private Map[] map = new Map[15];
	private Entity[] entities;
	private Player player;
	private int level = 0;
	
	public Game(int dim, String playerName){
		//Generate upgrades of each level		
		for(int ii = 0; ii < map.length; ii++){
			map[ii] = new Map(dim, ii);
		}
		player = new Player(Main.nextId(), playerName);
		entities = new Entity[1];
		entities[0] = player;
	}
	public Map getMap(){
		return map[level];
	}
	public boolean nextMap(){
		if(++level >= map.length){
			level--;
			return false;
		}
		return true;
	}
	/**
	 * retorna si se ha podido añadir la entidad o no (si la id ya existe o no)
	 * @param entity
	 * @return
	 */
	public boolean addEntity(Entity entity){
		if(!entity.isAlive())
			return false;
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
				entities[ii].kill();
				Entity[] arr = new Entity[entities.length-1];
				System.arraycopy(entities, 0, arr, 0, ii);
				System.arraycopy(entities, ii+1, arr, ii, arr.length-ii);
				entities = arr;
				return true;
			}
		}
		return false;
	}
	
	public void explodeAt(Coordinates bombPos){
		int explosionLength = getPlayer().getRange();
//		String orientation = "";
//		String place = "";
		
		Coordinates square = bombPos.tenthsToUnits(); //sin esto no parsea bien la coordenada y da error out of bounds
		//primero el centro 
		getMap().setTypeAt(square, "explosion");
		// set explosion_C4.gif
		boolean wallN = false, wallW = false, wallS = false, wallE = false;
		Coordinates[][] arrExplosive = new Coordinates[4][explosionLength]; // N, W, S, E
		for (int ii = 1; ii < explosionLength; ii++) {
			//Setting all the possible coordinates in all directions (but center)
			arrExplosive[0][ii] = new Coordinates(square.x, square.y + ii); //North
			arrExplosive[1][ii] = new Coordinates(square.x - ii, square.y); //West
			arrExplosive[2][ii] = new Coordinates(square.x, square.y - ii); //South
			arrExplosive[3][ii] = new Coordinates(square.x + ii, square.y); //East
			//Taking into account the limits of the walls
			if (!wallN && getMap().getTypeAt(arrExplosive[0][ii]).equals("wall")) wallN = true;
			if (!wallW && getMap().getTypeAt(arrExplosive[1][ii]).equals("wall")) wallW = true;
			if (!wallS && getMap().getTypeAt(arrExplosive[2][ii]).equals("wall")) wallS = true;
			if (!wallE && getMap().getTypeAt(arrExplosive[3][ii]).equals("wall")) wallE = true;
			//Setting sprites
			getMap().setExplosionAt(square, "c");
			if      (!wallN) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[0][ii], "v");//set explosion_V4.gif
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[0][ii], "s");//set explosion_N4.gif	
			}
			if (!wallW) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[1][ii], "h");//set explosion_H4.gif
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[1][ii], "w");//set explosion_W4.gif	
			}
			if (!wallS) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[2][ii], "v");//set explosion_V4.gif
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[2][ii], "n");//set explosion_S4.gif	
			}
			if (!wallE) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[3][ii], "h");//set explosion_H4.gif
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[3][ii], "e");//set explosion_E4.gif	
			}
		}
		
	}

//	public String ExplosionSprite(String orientation, String place) {
//		// Orientation: "N", "W", "E", "S"
//		// Place: "Center", "Middle", "Edge"
//		String toReturn = "";
//		if (place.equals("Center")) {
//			toReturn = "explosion_C4.gif";
//		}
//		else if (place.equals("Middle")) {
//			if (orientation.equals("N") || orientation.equals("S")) {
//				toReturn = "explosion_V4.gif";
//			}
//			else if (orientation.equals("E") || orientation.equals("W")) {
//				toReturn = "explosion_H4.gif";
//			}
//		}
//		else if (place.equals("Edge")) {
//			toReturn = "explosion_" + orientation + "4.gif";
//		}
//		return toReturn;
//	}

	public Entity[] getEntities(){
		return entities;
	}
	public Player getPlayer(){
		return player;
	}
	public int getLevel(){
		return level;
	}
}
