package de.uulm.par.notes;

import org.joda.time.DateTime;

import de.uulm.par.R;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;


public class AddLocation extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		
		final TextView title = (TextView) findViewById(R.id.new_location_title);
		final TextView message = (TextView) findViewById(R.id.new_location_message);

		Button add = (Button) findViewById(R.id.btn_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlainNote addingNote = new PlainNote(
						title.getText().toString(), message.getText()
								.toString(), NoteType.LOCATION, new DateTime());
				addingNote.setLocation("Uni Ulm");
				Intent returnIntent = new Intent();
				returnIntent.putExtra("Note",addingNote);
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});
	}

}
