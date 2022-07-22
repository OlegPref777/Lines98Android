package ru.ls.lines98.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.ls.lines98.R;

public class SaveAdapter extends ArrayAdapter<SaveGame> {
    Context ctx;
    LayoutInflater lInflater;
    private int SelectedIndex = -1;
    List<SaveGame> MySaves;

    public SaveAdapter(@NonNull Context context, @NonNull List<SaveGame> saveGames) {
        super(context, R.layout.row_save, saveGames);
        MySaves = saveGames;
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.row_save, parent, false);
        }

        SaveGame saveGame = getItem(position);
        TextView DateTV = view.findViewById(R.id.DateTV);
        TextView GameTypeTV = view.findViewById(R.id.GameTypeTV);
        TextView PlayTimeTV = view.findViewById(R.id.PlayTimeTV);
        TextView ScoreTV = view.findViewById(R.id.ScoreTV);
        CheckBox IsAutoSave = view.findViewById(R.id.AutoSave);
        int TableForeSelected = ctx.getResources().getColor(R.color.white);
        int TableForeUnSelected = ctx.getResources().getColor(R.color.black);
        if (position == SelectedIndex) {
            view.setBackground(ctx.getDrawable(R.drawable.rect_rounded_sel));
            DateTV.setTextColor(TableForeSelected);
            GameTypeTV.setTextColor(TableForeSelected);
            PlayTimeTV.setTextColor(TableForeSelected);
            ScoreTV.setTextColor(TableForeSelected);
            IsAutoSave.setTextColor(TableForeSelected);

        }else{
            view.setBackground(ctx.getDrawable(R.drawable.rect_rounded));
            DateTV.setTextColor(TableForeUnSelected);
            GameTypeTV.setTextColor(TableForeUnSelected);
            PlayTimeTV.setTextColor(TableForeUnSelected);
            ScoreTV.setTextColor(TableForeUnSelected);
            IsAutoSave.setTextColor(TableForeUnSelected);

        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        DateTV.setText(format.format(saveGame.getSaveDate()));
        GameTypeTV.setText(saveGame.getGameType().toString());
        PlayTimeTV.setText(String.valueOf(saveGame.getPlayTimeSeconds()));
        ScoreTV.setText(String.valueOf(saveGame.getScore()));
        IsAutoSave.setSelected(saveGame.isAutoSave());
        return view;
    }

    public void setSelected(int i) {
        SelectedIndex = i;
        notifyDataSetChanged();
    }
    public SaveGame getSelectedItem(){
        if ((SelectedIndex >= 0) && (SelectedIndex < MySaves.size())){
            return getItem(SelectedIndex);
        }
        return null;
    }
    public void UnSelectAll(){
        SelectedIndex = -1;
        notifyDataSetChanged();
    }

}
