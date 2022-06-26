package ru.ls.lines98.game.option;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.R.layout;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;


public class OptionDialog{
	GameInfo gameInfo = GameInfo.getCurrentInstance().clone();
	RadioGroup GameTypeRG;
	RadioButton LineRB, SquareRB, BlockRB;
	Button OkBtn, CancelBtn;
	CheckBox DestroySoundCB, MovingSoundCB, JumpingSoundCB;
	Spinner NextBallDisplayCB;


	public AlertDialog OptionsDlg;
	public int Background = 0xFF000000;
	LayoutInflater lInflater;


	View.OnClickListener GameTypeRB_OnClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			int GameTypeId = GameTypeRG.getCheckedRadioButtonId();
			if (GameTypeId == R.id.LineRB){
				gameInfo.setDefaultGameType(GameType.LINE);
			}else if (GameTypeId == R.id.SquareRB){
				gameInfo.setDefaultGameType(GameType.SQUARE);
			}else if (GameTypeId == R.id.BlockRB){
				gameInfo.setDefaultGameType(GameType.BLOCK);
			}
		}
	};


	public OptionDialog(Context context) {
		OptionsDlg = new AlertDialog.Builder(context).create();
		OptionsDlg.setTitle(MainActivity._this.getResources().getString(R.string.Options));

		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lInflater.inflate(R.layout.dialog_options, OptionsDlg.getListView());
		GameTypeRG = view.findViewById(R.id.GameTypeRG);
		LineRB = view.findViewById(R.id.LineRB);
		BlockRB = view.findViewById(R.id.BlockRB);
		SquareRB = view.findViewById(R.id.SquareRB);
		if (gameInfo.getDefaultGameType() == GameType.LINE){
			LineRB.setChecked(true);
		}
		if (gameInfo.getDefaultGameType() == GameType.BLOCK){
			BlockRB.setChecked(true);
		}
		if (gameInfo.getDefaultGameType() == GameType.SQUARE){
			SquareRB.setChecked(true);
		}
		LineRB.setOnClickListener(GameTypeRB_OnClick);
		BlockRB.setOnClickListener(GameTypeRB_OnClick);
		SquareRB.setOnClickListener(GameTypeRB_OnClick);
		NextBallDisplayCB = (Spinner) view.findViewById(R.id.NextBallDisplayCB);
		NextBallDisplayCB.setAdapter(new ArrayAdapter<>(context, layout.simple_spinner_dropdown_item, NextBallDisplayType.values()));
		NextBallDisplayCB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				gameInfo.setNextBallDisplayType((NextBallDisplayType) NextBallDisplayCB.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		NextBallDisplayCB.setSelection(gameInfo.getNextBallDisplayType().getIndex());

		view.setBackgroundColor(Background);
		DestroySoundCB = (CheckBox)view.findViewById(R.id.DestroySoundCB);
		DestroySoundCB.setChecked(gameInfo.isDestroySound());
		DestroySoundCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				gameInfo.setDestroySound(DestroySoundCB.isChecked());
			}
		});

		MovingSoundCB = (CheckBox)view.findViewById(R.id.MovingSoundCB);
		MovingSoundCB.setChecked(gameInfo.isMovementSound());
		MovingSoundCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				gameInfo.setMovementSound(MovingSoundCB.isChecked());
			}
		});
		JumpingSoundCB = (CheckBox)view.findViewById(R.id.JumpingSoundCB);
		JumpingSoundCB.setChecked(gameInfo.isBallJumpingSound());
		JumpingSoundCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				gameInfo.setBallJumpingSound(JumpingSoundCB.isChecked());
			}
		});
		OkBtn = view.findViewById(R.id.OkBtn);
		OkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				OptionsDlg.cancel();
				GameInfo.setCurrentInstance(gameInfo);
			}
		});
		CancelBtn = view.findViewById(R.id.CancelBtn);
		CancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				OptionsDlg.cancel();
			}
		});

		OptionsDlg.setView(view);
	}

	public void ShowDialog(){
		OptionsDlg.show();
	}
}
