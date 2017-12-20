package uc3m.bomberman.map;

import uc3m.bomberman.entity.Enemy;

/**
 * This record stores two integers representing coordinates
 * @author danoloan10
 *
 */
public class Coordinates{
	public int x;
	public int y;
	/**
	 * Empty constructor
	 * @param tenths
	 */
	public Coordinates(boolean tenths){}
	/**
	 * Full constructor
	 */
	public Coordinates(int x, int y){
		this.x = x > 0 ? x : 0;
		this.y = y > 0 ? y : 0;
	}
	/**
	 * @return new object with the units from tenths of tile to whole squares
	 */
	public Coordinates tenthsToUnits(){
		return new Coordinates(((x+4)/10), (y+8)/10);
	}
	/**
	 * @return new object with the units from whole squares to tenths of tile 
	 */
	public Coordinates unitsToTenths(){
		return new Coordinates(x*10, y*10);
	}

	/**
	 * @param coords
	 * @return true if the <code>{@link Coordinates}</code> are equal
	 */
	public boolean equals(Coordinates coords){
		return this.x == coords.x && this.y == coords.y;
	}
}
