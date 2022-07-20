package ru.ls.lines98.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;
import ru.ls.lines98.playerscore.DBHelper;
import ru.ls.lines98.playerscore.PlayerScore;
import ru.ls.lines98.playerscore.PlayerScoreAdapter;
import ru.ls.lines98.playerscore.ScoreHistoryDAO;


public class HighScoreDialog {

	private static final String VERSION = "1.1.0";
	private static final String MY_GIT_ADDRESS = "https://github.com/OlegPref777";
	AlertDialog HighScoreDlg;
	LayoutInflater lInflater;
	ListView RecordList;
	Button OkBtn, ClearAllBtn;

	public HighScoreDialog(Context context) {
		HighScoreDlg = new AlertDialog.Builder(context).create();
		HighScoreDlg.setTitle(MainActivity._this.getResources().getString(R.string.HighScores));
		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lInflater.inflate(R.layout.dialog_high_score, HighScoreDlg.getListView());
		RecordList = view.findViewById(R.id.RecordList);
		ScoreHistoryDAO scoreHistoryDAO = new ScoreHistoryDAO(context);
		List<PlayerScore> scores = scoreHistoryDAO.getAll();
		RecordList.setAdapter(new PlayerScoreAdapter(context, scores));

		OkBtn = view.findViewById(R.id.OkBtn);

		OkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				HighScoreDlg.cancel();
			}
		});
		ClearAllBtn = view.findViewById(R.id.ClearAllBtn);
		ClearAllBtn.setOnClickListener(view1 -> {
			new AlertDialog.Builder(context)
					.setTitle(context.getResources().getString(R.string.clear_records) + "?")
					.setMessage(context.getResources().getString(R.string.clear_records) + "?")
					.setNegativeButton(context.getResources().getString(R.string.No), null)
					.setPositiveButton(context.getResources().getString(R.string.Yes), (arg0, arg1) -> scoreHistoryDAO.ClearRecords()).create().show();

		});
		HighScoreDlg.setView(view);
	}

	public void ShowDialog(){
		HighScoreDlg.show();
	}
}
