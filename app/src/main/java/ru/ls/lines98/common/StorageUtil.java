package ru.ls.lines98.common;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class StorageUtil {

	public static <T extends Object> boolean save(T o, Context context, String fileName) {
		try {
			FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream stream = new ObjectOutputStream(fileOutputStream);
			stream.writeObject(o);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> Optional<T> load(Context context, String fileName) {
		try {
			FileInputStream fileInputStream = context.openFileInput(fileName);
			ObjectInputStream stream = new ObjectInputStream(fileInputStream);
			return Optional.of((T) stream.readObject());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Optional.empty();
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
