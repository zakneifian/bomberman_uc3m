package uc3m.bomberman.entity;
public class Player extends Entity{
	private int bombs;
	private int maxBombs = 1;
	private int range = 1;
	private String name;
	private int score;
	
	public Player(String name){
		this.name = name;
	}
}
