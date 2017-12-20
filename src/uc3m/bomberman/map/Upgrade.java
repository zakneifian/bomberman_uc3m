package uc3m.bomberman.map;
/**
 * This class extends the <code>{@link Tile}</code> for the sole purpose
 * of being more specific concerning the Upgrades in the game
 *
 */
public class Upgrade extends Tile{
	private final char[] UPG_TYPE = {'B', 'F', 'S', 'R', 'K', 'G', 'D'};
	private char type;
	
	/**
	 * Full <code>{@link Ugrade}</code> constructor
	 * @param type
	 */
	public Upgrade(String type) {
		super("green");
		setUpgradeType(type);
	}
	
	/**
	 * Sets the type of an <code>{@link Upgrade}</code>
	 * @param type
	 */
	private void setUpgradeType(String type){
		int i = UPG_TYPE.length;
		switch(type){
		case "bomb":
			i--;
		case "fire":
			i--;
		case "special":
		case "sfire":
		case "special fire":
			i--;
		case "remote":
			i--;
		case "skate":
			i--;
		case "geta":
			i--;
		case "door":
			i--;
			this.type = UPG_TYPE[i];
		}
	}
	
	/**
	 * @return the type of the <code>{@link Upgrade}</code>
	 */
	public String getUpgradeType(){
		switch(type){
		case 'B':
			return "bomb";
		case 'F':
			return "fire";
		case 'S':
			return "special";
		case 'R':
			return "remote";
		case 'K':
			return "skate";
		case 'G':
			return "geta";
		case 'D':
			return "door";
		}
		return "null";
	}
	
	/* (non-Javadoc)
	 * @see uc3m.bomberman.map.Tile#getSprite()
	 */
	@Override
	public String getSprite(){
		switch(type){
		case 'B':
			return "Bombupsprite.png";
		case 'F':
			return "Fireupsprite.png";
		case 'S':
			return "Fullfiresprite.png";
		case 'R':
			return "Remote_Control_2.png";
		case 'K':
			return "Skatesprite.png";
		case 'G':
			return "Getasprite.png";
		case 'D':
			return "DoorClosed.png";
		}
		return "null";
	}
	
}