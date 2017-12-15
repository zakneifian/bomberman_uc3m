package uc3m.bomberman.map;
/**
 * This record stores two integers representing coordinates
 * @author danoloan10
 *
 */
public class Coordinates{
	public int x;
	public int y;
	public Coordinates(){}
	public Coordinates(int x, int y){
		this.x = x > 0 ? x : 0;
		this.y = y > 0 ? y : 0;
	}
	public Coordinates tenthsToUnits(){
		return new Coordinates(((x+4)/10), (y+8)/10);
	}
	public Coordinates unitsToTenths(){
		return new Coordinates(x*10, y*10);
	}
	public boolean equals(Coordinates coords){
		return this.x == coords.x && this.y == coords.y;
	}
}
