package ru.ls.lines98.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


import ru.ls.lines98.R;

public class HighScoreDialog {

	private static final String VERSION = "1.1.0";
	private static final String MY_GIT_ADDRESS = "https://github.com/OlegPref777";
	AlertDialog AboutDlg;
	LayoutInflater lInflater;
	WebView DisplayWV;
	Button OkBtn;

	public HighScoreDialog(Context context) {
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

//	public HighScoreDialog(Context context) {
//
//		JTable table = new JTable(new HighScoreTableModel());
//		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//		table.setDefaultRenderer(Object.class, centerRenderer);
//
//		JScrollPane northPane = new JScrollPane(table);
//		add(northPane, BorderLayout.CENTER);
//
//		JPanel southPane = new JPanel();
//		JButton okButton = new JButton("OK");
//		okButton.addActionListener((e) -> {
//			HighScoreDialog.this.setVisible(false);
//			HighScoreDialog.this.dispose();
//		});
//
//		southPane.add(okButton);
//		add(southPane, BorderLayout.SOUTH);
//
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		setTitle("High scores");
//		setSize(320, 300);
//		WindowUtil.centerOwner(this);
//		setResizable(false);
//	}
//
//	private class HighScoreTableModel extends AbstractTableModel {
//		private final String[] columnNames = { "Name", "Score", "Play time" };
//
//		private List<PlayerScore> scores = PlayerScoreHistory.getInstance().getTopScores();
//
//		@Override
//		public int getColumnCount() {
//			return columnNames.length;
//		}
//
//		@Override
//		public String getColumnName(int c) {
//			return columnNames[c];
//		}
//
//		@Override
//		public int getRowCount() {
//			return scores.size();
//		}
//
//		@Override
//		public Object getValueAt(int r, int c) {
//			switch (c) {
//			case 0:
//				return scores.get(r).getName();
//			case 1:
//				return scores.get(r).getScore();
//			case 2:
//				return scores.get(r).getPlayTime();
//			}
//			throw new InvalidParameterException();
//		}
//	}
}
