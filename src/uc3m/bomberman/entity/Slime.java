package uc3m.bomberman.entity;

import uc3m.bomberman.main.Game;
import uc3m.bomberman.map.Coordinates;

public class Slime extends Enemy {

	public Slime(int id, Coordinates position) {
		super(id, "enemy211.png", 100, position, 3, 4);
		this.speed = 1;
	}

	@Override
	public void moveEnemy(Game game){
		if (this.position.x > game.getPlayer().getPosition().x) { //if slime is to the right of player
			this.moveTowards(Direction.LEFT, game.getMap());
			this.setEntityDir("left");
			if(this.collides(game.getMap())) this.moveTowards(Direction.RIGHT, game.getMap());			
		}else if (this.position.x < game.getPlayer().getPosition().x) { //if slime is to the left of player
			this.moveTowards(Direction.RIGHT, game.getMap());
			this.setEntityDir("right");
			if(this.collides(game.getMap())) this.moveTowards(Direction.LEFT, game.getMap());					
		}else if (this.position.y > game.getPlayer().getPosition().y) { //if slime is above slime
			this.moveTowards(Direction.UP, game.getMap());
			this.setEntityDir("down");
			if(this.collides(game.getMap())) this.moveTowards(Direction.DOWN, game.getMap());			
		}else if (this.position.y < game.getPlayer().getPosition().y) { //if slime is below slime
			this.moveTowards(Direction.DOWN, game.getMap());
			this.setEntityDir("up");
			if(this.collides(game.getMap())) this.moveTowards(Direction.UP, game.getMap());			
		}
		this.animateMovement(this.getSpritePhase(), this.getEntityDir());		
	}

	@Override
	public int getScoreOnDeath() {
		return 250;
	}
	
}
