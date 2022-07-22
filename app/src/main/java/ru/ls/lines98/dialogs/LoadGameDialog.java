package ru.ls.lines98.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;
import ru.ls.lines98.database.SaveAdapter;
import ru.ls.lines98.database.SaveGame;
import ru.ls.lines98.database.SaveGameDAO;
import ru.ls.lines98.game.GamePanel;


public class LoadGameDialog {

	private static final String VERSION = "1.1.0";
	private static final String MY_GIT_ADDRESS = "https://github.com/OlegPref777";
	AlertDialog LoadGameDialog;
	LayoutInflater lInflater;
	ListView SaveList;
	Button OkBtn, DeleteSaveBtn;
	private GamePanel MyGamePanel;
	private List<SaveGame> savedGames;

	public LoadGameDialog(Context context, GamePanel gamePanel) {
		MyGamePanel = gamePanel;
		LoadGameDialog = new AlertDialog.Builder(context).create();
		LoadGameDialog.setTitle(MainActivity._this.getResources().getString(R.string.SavedGames));
		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lInflater.inflate(R.layout.dialog_load_game, LoadGameDialog.getListView());
		SaveList = view.findViewById(R.id.SaveList);
		SaveGameDAO saveGameDAO = new SaveGameDAO(context);
		savedGames = saveGameDAO.getAll();
		SaveAdapter saveAdapter = new SaveAdapter(context, savedGames);

		SaveList.setAdapter(saveAdapter);
		SaveList.setOnItemClickListener((adapterView, view1, i, l) -> saveAdapter.setSelected(i));

		OkBtn = view.findViewById(R.id.OkBtn);

		OkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SaveGame mySaveGame = saveAdapter.getSelectedItem();
				if (mySaveGame != null){
					MyGamePanel.getGameBoard().loadGame(mySaveGame);
					LoadGameDialog.cancel();
				}
			}
		});
		DeleteSaveBtn = view.findViewById(R.id.DeleteSave);
		DeleteSaveBtn.setOnClickListener(view1 -> {
			new AlertDialog.Builder(context)
					.setTitle(context.getResources().getString(R.string.delete_save) + "?")
					.setMessage(context.getResources().getString(R.string.delete_save) + "?")
					.setNegativeButton(context.getResources().getString(R.string.No), null)
					.setPositiveButton(context.getResources().getString(R.string.Yes), (arg0, arg1) -> {
						SaveGame mySaveGame = saveAdapter.getSelectedItem();
						if (mySaveGame != null){
							if (saveGameDAO.deleteOne(mySaveGame)){
								savedGames.remove(mySaveGame);
								saveAdapter.notifyDataSetChanged();
							}
						}
					}).create().show();;
		});
		LoadGameDialog.setView(view);
	}

	public void ShowDialog(){
		LoadGameDialog.show();
	}
}
