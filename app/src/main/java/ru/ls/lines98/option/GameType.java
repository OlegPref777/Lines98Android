package ru.ls.lines98.option;

import androidx.annotation.NonNull;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;

public enum GameType {
	LINE(1), SQUARE(2), BLOCK(3);

	private int value;

	GameType(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}

	@NonNull
	@Override
	public String toString() {
		if (this == LINE){
			return  MainActivity._this.getResources().getString(R.string.Lines);
		} else if (this == BLOCK){
			return  MainActivity._this.getResources().getString(R.string.Blocks);
		} else {
			return  MainActivity._this.getResources().getString(R.string.Squares);
		}
	}
}
