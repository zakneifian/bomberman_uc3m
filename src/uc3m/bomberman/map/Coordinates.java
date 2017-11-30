package uc3m.bomberman.map;
public class Coordinates{
	public int x;
	public int y;
	public Coordinates(){}
	public Coordinates(int x, int y){
		this.x = x > 0 ? x : 0;
		this.y = y > 0 ? y : 0;
	}
	public Coordinates tenthsToUnits(){
		return new Coordinates(x%10, y%10);
	}
	public Coordinates unitsToTenths(){
		return new Coordinates(x*10, y*10);
	}
}
