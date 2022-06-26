package ru.ls.lines98.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import ru.ls.lines98.game.Dimension;

/**
 * Window utility
 */
public final class WindowUtil {

	/**
	 * Make static class
	 */
	private WindowUtil() {
	}

	/**
	 * Set window location to center owner
	 * 
	 * @param window
	 *            window what set to center owner
	 */
	public static void centerOwner(View window) {
		View onwner = (View) window.getParent();
		int x, y;

		// If owner is null, set dialog position to center screen
		if (onwner == null) {
			Context context = window.getContext();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			Dimension scrSize = new Dimension(size.x, size.y);
			x = (int) ((scrSize.getWidth() - window.getWidth()) / 2);
			y = (int) ((scrSize.getHeight() - window.getHeight()) / 2);
		} else {

			// Set dialog position to center owner
			x = (int) onwner.getX() + (onwner.getWidth() - window.getWidth()) / 2;
			y = (int) onwner.getY() + (onwner.getHeight() - window.getHeight()) / 2;
		}

		// Ensure dialog in screen
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		window.setX ((float)x);
		window.setY ((float)y);
	}

}
