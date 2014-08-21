package de.uulm.par;

import java.util.LinkedList;

import org.joda.time.DateTime;

import de.uulm.par.notes.AddNote;
import de.uulm.par.notes.NoteType;
import de.uulm.par.notes.PlainNote;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	ListView list;
	LinkedList<PlainNote> notes = new LinkedList<PlainNote>();

	String[] web = { "Time based", "Location based", "Simple note", "Person based", "Location based",
			"Simple note", "Person based" };
	Integer[] imageId = { R.drawable.ic_time, R.drawable.ic_location,
			R.drawable.ic_note, R.drawable.ic_person,
			R.drawable.ic_location, R.drawable.ic_note,
			R.drawable.ic_person};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//DUMMY DATA ---- START ----
		PlainNote t1 = new PlainNote("Meeting with Johannes", "Talking about the documentation", NoteType.DATETIME, new DateTime());
		t1.setAlert(new DateTime());
		
		PlainNote l1 = new PlainNote("Lunch", "check foodchart for lunch", NoteType.LOCATION, new DateTime());
		l1.setLocation("Ulm University");
		
		PlainNote p1 = new PlainNote("Masterthesis", "Ask Johannes about open thesis", NoteType.PERSON, new DateTime());
		p1.setPerson("Johannes");
		
		PlainNote s1 = new PlainNote("Fix bugs in project", "just kidding... there are no bugs ;)", NoteType.SIMPLE, new DateTime());
		
		notes.add(t1);
		notes.add(l1);
		notes.add(p1);
		notes.add(s1);
		
		//DUMMY DATA ---- END ----
		
		CustomList adapter = new CustomList(MainActivity.this, web, imageId);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(MainActivity.this,
						"You Clicked at " + web[+position], Toast.LENGTH_SHORT)
						.show();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add_reminder) {
			Intent intent = new Intent(this, AddNote.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
