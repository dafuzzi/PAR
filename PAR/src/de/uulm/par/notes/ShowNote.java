package de.uulm.par.notes;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.uulm.par.R;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowNote extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_note);
		Bundle extras = getIntent().getExtras();
		PlainNote note = null;
		if (extras != null) {
			note = (PlainNote) extras.getSerializable("Note");
		}
		if (note != null) {
			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("EEEE, MMMM d. YYYY HH:mm");
			TextView title = (TextView) findViewById(R.id.note_title);
			TextView created = (TextView) findViewById(R.id.note_created);
			TextView message = (TextView) findViewById(R.id.note_message);
			TextView info = (TextView) findViewById(R.id.note_info);
			ImageView imageView = (ImageView) findViewById(R.id.note_ic);

			title.setText(note.getTitle());
			created.setText("created on " + formatter.print(note.getAlert()));
			message.setText(note.getMessage());

			switch (note.getType()) {
			case PERSON:
				info.setText("When " + note.getPerson() + " is near you.");
				imageView.setImageResource(R.drawable.ic_person);
				break;
			case LOCATION:
				info.setText("When you are at " + note.getLocation());
				imageView.setImageResource(R.drawable.ic_location);
				break;
			case DATETIME:
				info.setText(formatter.print(note.getAlert()));
				imageView.setImageResource(R.drawable.ic_time);
				break;
			default:
				info.setText("");
				imageView.setImageResource(R.drawable.ic_note);
				break;
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}
}
