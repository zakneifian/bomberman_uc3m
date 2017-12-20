package uc3m.bomberman.map;

import uc3m.bomberman.main.Main;
/**
 * This class handles the Explosion sprites for the <code>{@link Tile}</code>s
 *
 */
public class Explosion extends Tile {
	private final char[] EXP_TYPE = {'C', 'N', 'E', 'W', 'S', 'H', 'V'};
	private char type;

	private final int TICK_TIME = 50;
	private int maxTicks = (int) (Main.EXP_TIME/TICK_TIME);
	private long time;
		
	private final int MAX_PHASE = 4;
	private int currentPhase = 0;
	
	/**
	 * Full constructor to set explosions
	 * @param type
	 */
	public Explosion(String type) {
		super("green");
		setExplosionType(type);
		time = System.currentTimeMillis();
	}
	
	/**
	 * Sets the type of the tile for the sprite of the explosion depending on the direction and position
	 * @param type
	 */
	private void setExplosionType(String type){
		switch(type.toLowerCase()){
		case "centre":
		case "center":
		case "c":
			this.type = EXP_TYPE[0]; //'c'
			break;
		case "north":
		case "n":
			this.type = EXP_TYPE[1]; //'n'
			break;
		case "east":
		case "e":
			this.type = EXP_TYPE[2]; //'e'
			break;
		case "west":
		case "w":
			this.type = EXP_TYPE[3]; //'w'
			break;
		case "south":
		case "s":
			this.type = EXP_TYPE[4]; //'s'
			break;
		case "horizontal":
		case "h":
			this.type = EXP_TYPE[5]; //'h'
			break;
		case "vertical":
		case "v":
			this.type = EXP_TYPE[6]; //'v'
			break;
		}
	}
	
	/**
	 * @return true if it's still in the MAX_PHASE, this concerns animation
	 */
	public boolean tick(){
		if(System.currentTimeMillis() - time > TICK_TIME){
			currentPhase++;
			time = System.currentTimeMillis();
			if(--maxTicks <= 0){
				maxTicks = 0;
				return true;
			}
		}
		if(currentPhase >= MAX_PHASE)
			currentPhase -= 2;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see uc3m.bomberman.map.Tile#getSprite()
	 */
	@Override
	public String getSprite(){
		return "explosion_"+type+(currentPhase+1)+".gif";
	}

}
