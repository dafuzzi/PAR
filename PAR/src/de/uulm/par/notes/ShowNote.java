package de.uulm.par.notes;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.uulm.par.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Fabian Schwab
 *
 */
public class ShowNote extends ActionBarActivity {
	PlainNote note;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_note);
		Bundle extras = getIntent().getExtras();
		note = null;
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
			created.setText("created on " + formatter.print(note.getCreated()));
			message.setText(note.getMessage());

			switch (note.getType()) {
			case PERSON:
				info.setText("When " + note.getPerson().getName() + " is near you.");
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
			Button del = (Button)findViewById(R.id.btn_delete);
			del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("Note",note);
					returnIntent.putExtra("Delete", true);
					setResult(RESULT_OK,returnIntent);
					finish();
					
				}
			});
		}

	}
}
