package ru.ls.lines98.playerscore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ls.lines98.R;

public class PlayerScoreAdapter extends ArrayAdapter<PlayerScore> {
    Context ctx;
    LayoutInflater lInflater;

    public PlayerScoreAdapter(@NonNull Context context, @NonNull List<PlayerScore> scores) {
        super(context, R.layout.row_score, scores);
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.row_score, parent, false);
        }

        PlayerScore score = getItem(position);
        TextView DateTV = view.findViewById(R.id.DateTV);
        TextView GameTypeTV = view.findViewById(R.id.GameTypeTV);
        TextView PlayTimeTV = view.findViewById(R.id.PlayTimeTV);
        TextView ScoreTV = view.findViewById(R.id.ScoreTV);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        DateTV.setText(format.format(score.getRecordDate()));
        GameTypeTV.setText(score.getGameType().toString());
        PlayTimeTV.setText(String.valueOf(score.getPlayTimeSeconds()));
        ScoreTV.setText(String.valueOf(score.getScore()));
        return view;
    }
}
