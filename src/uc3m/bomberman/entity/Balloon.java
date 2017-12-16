package uc3m.bomberman.entity;

import uc3m.bomberman.main.Game;
import uc3m.bomberman.map.Coordinates;

public class Balloon extends Enemy {
	
	private int dir;
	
	private final int TICK_TIME = 250;
	private long time;
	
	public Balloon(int id, Coordinates position) {
		super(id, "enemy111.png", 50, position, 3, 10);
		speed = 1;
		time = System.currentTimeMillis();
	}

	@Override
	public void moveEnemy(Game game) {
		if(time > TICK_TIME){
			if (dir == 0) {
				this.moveTowards(Direction.UP, game.getMap());
				this.setEntityDir("up");
				if(this.collides(game.getMap())){
					this.moveTowards(Direction.DOWN, game.getMap());
					dir = (int) Math.floor(Math.random()*4);
				}
			}
			else if (dir == 1) {
				this.moveTowards(Direction.DOWN, game.getMap());
				this.setEntityDir("down");
				if(this.collides(game.getMap())){
					this.moveTowards(Direction.UP, game.getMap());
					dir = (int) Math.floor(Math.random()*4);
				}
			}
			else if (dir == 2) {
				this.moveTowards(Direction.LEFT, game.getMap());
				this.setEntityDir("left");
				if(this.collides(game.getMap())){
					this.moveTowards(Direction.RIGHT, game.getMap());
					dir = (int) Math.floor(Math.random()*4);
				}
			}
			else if (dir == 3) {
				this.moveTowards(Direction.RIGHT, game.getMap());
				this.setEntityDir("right");
				if(this.collides(game.getMap())){
					this.moveTowards(Direction.LEFT, game.getMap());
					dir = (int) Math.floor(Math.random()*4);
				}
			}
		
			this.animateMovement(this.getSpritePhase(), this.getEntityDir());	
			time = System.currentTimeMillis();
		}
	}
	@Override
	public int getScoreOnDeath() {
		return 100;
	}
	
}