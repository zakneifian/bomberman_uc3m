package uc3m.bomberman.entity;

import uc3m.bomberman.main.Game;
import uc3m.bomberman.map.Coordinates;

public class AntiBomberman extends Enemy {

	public AntiBomberman(int id, Coordinates position) {
		super(id, "Antibomberman111.png", 1000, position, 3, 50);
	}
	
	@Override
	public void animateMovement(int spritePhase, String[] entityDir) {
		if (entityDir[1].equals(entityDir[0])) { //if the present movement equals the past one
			if (getSpritePhase() < getSpriteRgQty()) { //if the phase is lower than its range limit of movement
				setSpritePhase(getSpritePhase() + 1);  // sum one to the phase 
				setSprite(getSprite().substring(0, getSprite().length() - 5) + getSpritePhase()  + ".png"); // and change the sprite summing one to the last digit pf the path
			}
			else {
				setSpritePhase(1); //else, set the sprite phase to 0 and change the sprite to the first one, the 0
				setSprite(getSprite().substring(0, getSprite().length() - 5) + "1.png");
			}
		}
		else {
			setSpritePhase(1);
			if	    (entityDir[1].equals("up"))    {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "01.png");
			}
			else if (entityDir[1].equals("left"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "21.png");
			}
			else if (entityDir[1].equals("down"))  {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "11.png");
			}
			else if (entityDir[1].equals("right")) {
				setSprite(getSprite().substring(0, getSprite().length() - 6) + "31.png");
			}
		}
		
	}

	public boolean putBomb(){
		return true;
	}
	@Override
	public void moveEnemy(Game game) {
		String action = game.getPlayerAction();
		moveEnemyAction(game, action);
	}
	public void moveEnemyAction(Game game, String lastAction){
		if(lastAction.length() > 0 && this.isAlive() && game.getPlayer().isAlive()){ 
			//if movement
			if (lastAction.equals("up") || lastAction.equals("down") || lastAction.equals("left") || lastAction.equals("right")) {
				this.setEntityDir(lastAction);
				this.animateMovement(this.getSpritePhase(), this.getEntityDir());
			}
			switch(lastAction){
			case "up":
				this.moveTowards(Direction.UP, game.getMap());
				if(this.collides(game.getMap()))
					this.moveTowards(Direction.DOWN, game.getMap());
				break;
			case "down":
				this.moveTowards(Direction.DOWN, game.getMap());
				if(this.collides(game.getMap()))
					this.moveTowards(Direction.UP, game.getMap());
				break;
			case "left":
				this.moveTowards(Direction.LEFT, game.getMap());
				if(this.collides(game.getMap()))
					this.moveTowards(Direction.RIGHT, game.getMap());
				break;
			case "right":
				this.moveTowards(Direction.RIGHT, game.getMap());
				if(this.collides(game.getMap()))
					this.moveTowards(Direction.LEFT, game.getMap());
			case "space":
				if (this.putBomb()) {
				}
			}
		}
	}

	@Override
	public int getScoreOnDeath() {
		return 500;
	}
	
}