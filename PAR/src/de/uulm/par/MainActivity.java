package de.uulm.par;

import java.util.LinkedList;

import de.uulm.par.notes.AddLocation;
import de.uulm.par.notes.AddNote;
import de.uulm.par.notes.AddPerson;
import de.uulm.par.notes.AddTime;
import de.uulm.par.notes.NoteType;
import de.uulm.par.notes.PlainNote;
import de.uulm.par.notes.ShowNote;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author Fabian Schwab
 * 
 */
public class MainActivity extends ActionBarActivity {

	private static final int SHOW = 10;
	private static final int ADD = 11;

	private ListView list;
	private LinkedList<PlainNote> notes = new LinkedList<PlainNote>();
	private PlainNote lastNote;
	
	private Notification resultReceiver = new Notification(null);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		CustomList adapter = new CustomList(MainActivity.this, titleBuilder(notes), imageBuilder(notes), notes);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				intent = new Intent(getApplicationContext(), ShowNote.class);
				intent.putExtra("Note", notes.get(position));
				lastNote = notes.get(position);
				startActivityForResult(intent, SHOW);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_note) {
			Intent intent = new Intent(this, AddNote.class);
			startActivityForResult(intent, ADD);
			return true;
		}
		if (id == R.id.action_add_time) {
			Intent intent = new Intent(this, AddTime.class);
			startActivityForResult(intent, ADD);
			return true;
		}
		if (id == R.id.action_add_location) {
			Intent intent = new Intent(this, AddLocation.class);
			startActivityForResult(intent, ADD);
			return true;
		}
		if (id == R.id.action_add_person) {
			Intent intent = new Intent(this, AddPerson.class);
			startActivityForResult(intent, ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			data.getExtras();
			PlainNote note = null;
			if (data != null) {
				note = (PlainNote) data.getSerializableExtra("Note");
			}
			if (note != null) {
				if (requestCode == ADD) {
					notes.add(note);
					if(note.getType()==NoteType.PERSON){
						Intent intent = new Intent("de.uulm.miss.MISService");
						//intent.setComponent(new ComponentName("de.uulm.miss", "de.uulm.miss.misservice"));
						intent.putExtra("receiver", resultReceiver);
						intent.putExtra("operation", "add");
						intent.putExtra("client_name", note.getPerson().getName());
						intent.putExtra("client_mac", note.getPerson().getMac());
						startService(intent);
					}
				} else if (requestCode == SHOW) {
					if (data.hasExtra("Delete")) {
						notes.remove(lastNote);
					}
				}
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
}
