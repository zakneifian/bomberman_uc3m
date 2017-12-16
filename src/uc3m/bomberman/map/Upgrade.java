package uc3m.bomberman.map;

public class Upgrade extends Tile{
	private final char[] UPG_TYPE = {'B', 'F', 'S', 'R', 'K', 'G', 'D'};
	private char type;
	
	public Upgrade(String type) {
		super("green");
		setUpgradeType(type);
		// TODO UPGRADE Esta clase más o menos como la de Explosion: que almacene el tipo de upgrade y su sprite. Que los efectos los gestione Player.
	}
	
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
		return "wall";
	}
	
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
		return "wall";
	}
	
}