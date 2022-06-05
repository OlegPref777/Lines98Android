package ru.ls.lines98;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.material.internal.FlowLayout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import ru.ls.lines98.game.common.WindowUtil;

/**
 * Puzzle about dialog
 */
public class AboutDialog {
	private static final String VERSION = "1.1.0";
	private static final String MY_GIT_ADDRESS = "https://github.com/OlegPref777";

	AlertDialog AboutDlg;
	LayoutInflater lInflater;
	WebView DisplayWV;
	Button OkBtn;

	public AboutDialog(Context context) {
		AboutDlg = new AlertDialog.Builder(context).create();
		AboutDlg.setTitle("Options");
		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lInflater.inflate(R.layout.dialog_about, AboutDlg.getListView());
		DisplayWV = view.findViewById(R.id.DisplayWV);

		String unencodedHtml = "<html><body><table>" + "<tr><td colspan=2 align=center><font size=5 color=Red>Game Lines</font></td></tr>"
				+ "<tr><td>Author:</td><td>Trac Quang Hoa</td><td></td></tr>" + "<tr><td>Version:</td><td>"
				+ VERSION + "</td><td></td></tr>" + "</table>" + "<a href='" + MY_GIT_ADDRESS + "'><i>" + MY_GIT_ADDRESS + "</i></a></body></html>";
		String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
		DisplayWV.loadData(encodedHtml, "text/html", "base64");
		OkBtn = view.findViewById(R.id.OkBtn);
		OkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AboutDlg.cancel();
			}
		});
		AboutDlg.setView(view);
	}

	public void ShowDialog(){
		AboutDlg.show();
	}
}
