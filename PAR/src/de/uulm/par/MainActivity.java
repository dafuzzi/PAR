package de.uulm.par;

import java.util.LinkedList;

import org.joda.time.DateTime;

import de.uulm.par.notes.AddNote;
import de.uulm.par.notes.AddTime;
import de.uulm.par.notes.NoteType;
import de.uulm.par.notes.PlainNote;
import de.uulm.par.notes.ShowNote;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	private ListView list;
	private LinkedList<PlainNote> notes = new LinkedList<PlainNote>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// DUMMY DATA ---- START ----
		PlainNote t1 = new PlainNote("Meeting with Johannes",
				"Talking about the documentation", NoteType.DATETIME,
				new DateTime());
		t1.setAlert(new DateTime());

		PlainNote l1 = new PlainNote("Lunch", "check foodchart for lunch",
				NoteType.LOCATION, new DateTime());
		l1.setLocation("Ulm University");

		PlainNote p1 = new PlainNote("Masterthesis",
				"Ask Johannes about open thesis", NoteType.PERSON,
				new DateTime());
		p1.setPerson("Johannes");

		PlainNote s1 = new PlainNote("Fix bugs in project",
				"just kidding... there are no bugs ;)", NoteType.SIMPLE,
				new DateTime());

		PlainNote p2 = new PlainNote("Seminar", "Talk about the acm seminar",
				NoteType.PERSON, new DateTime());
		p2.setPerson("Fabian Maier");

		notes.add(t1);
		notes.add(l1);
		notes.add(p1);
		notes.add(s1);
		notes.add(p2);

		// DUMMY DATA ---- END ----
		
//		CustomList adapter = new CustomList(MainActivity.this,
//				titleBuilder(notes), imageBuilder(notes), notes);
//		list = (ListView) findViewById(R.id.list);
//		list.setAdapter(adapter);
//		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent;
//				intent = new Intent(getApplicationContext(), ShowNote.class);
//				intent.putExtra("Note", notes.get(position));
//				startActivityForResult(intent, 1);
//			}
//		});
	}

	protected void onResume(){
		super.onResume();
		CustomList adapter = new CustomList(MainActivity.this,
				titleBuilder(notes), imageBuilder(notes), notes);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				intent = new Intent(getApplicationContext(), ShowNote.class);
				intent.putExtra("Note", notes.get(position));
				startActivity(intent);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			 data.getExtras();
			 PlainNote note = null;
			 if (data != null) {
			 note = (PlainNote) data.getSerializableExtra("Note");
			 }
			 if (note != null) {
				 notes.add(note);
			 }
		}

	}

	/**
	 * @param notes
	 * @return
	 */
	private Integer[] imageBuilder(LinkedList<PlainNote> notes) {
		Integer[] res = new Integer[notes.size()];
		for (int i = 0; i < notes.size(); i++) {
			switch (notes.get(i).getType()) {
			case LOCATION:
				res[i] = R.drawable.ic_location;
				break;
			case DATETIME:
				res[i] = R.drawable.ic_time;
				break;
			case PERSON:
				res[i] = R.drawable.ic_person;
				break;
			default:
				res[i] = R.drawable.ic_note;
				break;
			}
		}
		return res;
	}

	/**
	 * @param notes
	 * @return
	 */
	private String[] titleBuilder(LinkedList<PlainNote> notes) {
		String[] res = new String[notes.size()];
		for (int i = 0; i < notes.size(); i++) {
			res[i] = notes.get(i).getTitle();
		}
		return res;
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
		if (id == R.id.action_add_note) {
			Intent intent = new Intent(this, AddNote.class);
			startActivityForResult(intent, 1);
			return true;
		}
		if (id == R.id.action_add_time) {
			Intent intent = new Intent(this, AddTime.class);
			startActivityForResult(intent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
