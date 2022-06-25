package ru.ls.lines98.game.option;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;

public enum NextBallDisplayType {

	ShowBoth(MainActivity._this.getResources().getString(R.string.show_both)),
	ShowOnField(MainActivity._this.getResources().getString(R.string.show_on_field)),
	ShowOnTop(MainActivity._this.getResources().getString(R.string.show_on_top)),
	NotShow(MainActivity._this.getResources().getString(R.string.dont_show));

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
