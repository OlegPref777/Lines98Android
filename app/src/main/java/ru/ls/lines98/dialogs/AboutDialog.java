package ru.ls.lines98.dialogs;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


import ru.ls.lines98.BuildConfig;
import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;

/**
 * Puzzle about dialog
 */
public class AboutDialog {
	private static final String MY_GIT_ADDRESS = "https://github.com/OlegPref777";

	AlertDialog AboutDlg;
	LayoutInflater lInflater;
	WebView DisplayWV;
	Button OkBtn;

	public AboutDialog(Context context) {
		AboutDlg = new AlertDialog.Builder(context).create();
		AboutDlg.setTitle(MainActivity._this.getResources().getString(R.string.About));
		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lInflater.inflate(R.layout.dialog_about, AboutDlg.getListView());
		DisplayWV = view.findViewById(R.id.DisplayWV);
		String Author = MainActivity._this.getResources().getString(R.string.Author);
		String VersionString = MainActivity._this.getResources().getString(R.string.VersionString);
		String AuthorString = MainActivity._this.getResources().getString(R.string.AuthorString);

		String unencodedHtml = "<html><body><table>" + "<tr><td colspan=2 align=center><font size=5 color=Red>Game Lines</font></td></tr>"
				+ "<tr><td>"+ AuthorString + ":</td><td>" + Author + "</td><td></td></tr>" + "<tr><td>"+ VersionString +":</td><td>"
				+ BuildConfig.VERSION_NAME + "</td><td></td></tr>" + "</table>" + "<a href='" + MY_GIT_ADDRESS + "'><i>" + MY_GIT_ADDRESS + "</i></a></body></html>";
		String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
		DisplayWV.loadData(encodedHtml, "text/html", "base64");
		OkBtn = view.findViewById(R.id.OkBtn);
		OkBtn.setOnClickListener(view1 -> AboutDlg.cancel());
		AboutDlg.setView(view);
	}

	public void ShowDialog(){
		AboutDlg.show();
	}
}
