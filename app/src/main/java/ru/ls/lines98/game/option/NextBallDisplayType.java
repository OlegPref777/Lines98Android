package ru.ls.lines98.game.option;

public enum NextBallDisplayType {
	ShowBoth("Show both"),
	ShowOnField("Show on field"),
	ShowOnTop("Show on top"),
	NotShow("Do not show");

	private String description;

	NextBallDisplayType(String description) {
		this.description = description;
	}

	public int getIndex(){
		for (int i = 0; i < values().length; i++){
			if (this == values()[i]){
				return i;
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return description;
	}
}
