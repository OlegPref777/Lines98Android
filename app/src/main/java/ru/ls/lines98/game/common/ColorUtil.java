package ru.ls.lines98.game.common;

import android.graphics.Color;
import java.util.Random;

public final class ColorUtil {
	private final static int[] lineColorArray = new int[] { Color.RED,
			Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA,
			Color.rgb(160, 0, 0) };

	public static int getRandomColor() {
		Random random = new Random();
		return lineColorArray[random.nextInt(lineColorArray.length)];
	}
}
