package de.uulm.par;

import java.util.LinkedList;

import de.uulm.par.notes.AddLocation;
import de.uulm.par.notes.AddNote;
import de.uulm.par.notes.AddPerson;
import de.uulm.par.notes.AddTime;
import de.uulm.par.notes.NoteType;
import de.uulm.par.notes.PlainNote;
import de.uulm.par.notes.ShowNote;
import de.uulm.par.sql.NotesDataSource;
import de.uulm.par.sql.CustomList;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author Fabian Schwab
 * 
 */
public class MainActivity extends ActionBarActivity implements ServiceConnection {
	private static final String LOGTAG = "PAR";

	public static final int MSG_REGISTER_APPLICATION = 1;
	public static final int MSG_UNREGISTER_APPLICATION = 2;
	public static final int MSG_ADD_CLIENT = 3;
	public static final int MSG_ADD_STATION = 4;
	public static final int MSG_REMOVE_CLIENT = 5;
	public static final int MSG_REMOVE_STATION = 6;
	public static final int MSG_FOUND_DEVICE = 7;

	private static final int SHOW = 10;
	private static final int ADD = 11;

	private ListView list;
	private LinkedList<PlainNote> notes = new LinkedList<PlainNote>();
	private PlainNote lastNote;

	private final Messenger mMessenger = new Messenger(new IncomingMessageHandler(this));
	private ServiceConnection mConnection = this;
	private Messenger mServiceMessenger = null;
	boolean mIsBound;

	private NotesDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new NotesDataSource(this);
		Log.d(LOGTAG,"DB open on Create");
		datasource.open();
		notes = datasource.getAllNotes();
		setContentView(R.layout.activity_main);
		doBindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open(); 
		Log.d(LOGTAG,"DB open on Resume");
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
	protected void onDestroy() {
		super.onDestroy();
		Log.d(LOGTAG,"DB close on Destroy");
		datasource.close();
//		try {
//			doUnbindService();
//		} catch (Throwable t) {
//			Log.d(LOGTAG, "Failed to unbind from the service: ", t);
//		}
	}
	@Override
	protected void onPause(){
		super.onPause();
		datasource.close();
		Log.d(LOGTAG,"DB close on Pause");
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mServiceMessenger = new Messenger(service);
		Log.d(LOGTAG, "onServiceConnected");
		try {
			Message msg = Message.obtain(null, MSG_REGISTER_APPLICATION);
			msg.replyTo = mMessenger;
			mServiceMessenger.send(msg);
		} catch (RemoteException e) {
			// In this case the service has crashed before we could even do
			// anything with it
		}

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// This is called when the connection with the service has been
		// unexpectedly disconnected - process crashed.
		mServiceMessenger = null;
		Log.d(LOGTAG, "onServiceDisconnected");
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
		Log.d(LOGTAG,"DB open on Result");
		datasource.open();
		if (resultCode == RESULT_OK) {
			data.getExtras();
			PlainNote note = null;
			if (data != null) {
				note = (PlainNote) data.getSerializableExtra("Note");
			}
			if (note != null) {
				if (requestCode == ADD) {
					notes.add(note);
					datasource.insertNote(note);
					if (note.getType() == NoteType.PERSON) {
						Bundle b = new Bundle();
						b.putString("MAC", note.getPerson().getMac());
						b.putString("Name", note.getPerson().getName());
						sendMessageToService(b, MSG_ADD_CLIENT);
					}
				} else if (requestCode == SHOW) {
					if (data.hasExtra("Delete")) {
						if (lastNote.getType() == NoteType.PERSON) {
							Bundle b = new Bundle();
							b.putString("MAC", lastNote.getPerson().getMac());
							b.putString("Name", lastNote.getPerson().getName());
							sendMessageToService(b, MSG_REMOVE_CLIENT);
						}
						datasource.deleteNote(lastNote);
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

	/**
	 * Handle incoming messages from MyService
	 */
	private class IncomingMessageHandler extends Handler {
		MainActivity main;

		public IncomingMessageHandler(MainActivity parent) {
			main = parent;
		}

		@Override
		public void handleMessage(Message msg) {
			Log.d(LOGTAG, "IncomingHandler: " + msg.what);
			switch (msg.what) {
			case MSG_FOUND_DEVICE:
				Bundle b = new Bundle();
				b.putString("MAC", (String) msg.getData().get("MAC"));
				b.putString("Name", (String) msg.getData().get("Name"));
				sendMessageToService(b, MSG_REMOVE_CLIENT);
				main.doNotification((String) msg.getData().get("MAC"));
				break;
			default:
				super.handleMessage(msg);
			}
		}

	}

	/**
	 * @param mac
	 */
	private void doNotification(String mac) {
		String person = null;
		Intent resultIntent = new Intent(this, MainActivity.class);
		for (PlainNote n : notes) {
			if (n.getType() == NoteType.PERSON && n.getPerson().getMac() == mac) {
				resultIntent = new Intent(this, ShowNote.class);
				resultIntent.putExtra("Note", n);
				person = n.getPerson().getName();
				break;
			}
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_note).setContentTitle("PAR")
				.setContentText(person + " is near you and linked with a note.");
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		// Sets an ID for the notification
		int mNotificationId = 001;

		// Sound
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);

		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}

	/**
	 * 
	 */
	private void doBindService() {
		bindService(new Intent("de.uulm.miss.MISService"), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
		Log.d(LOGTAG, "Binding.");
	}

	/**
	 * 
	 */
	private void doUnbindService() {
		if (mIsBound) {
			// If we have received the service, and hence registered with it,
			// then now is the time to unregister.
			if (mServiceMessenger != null) {
				try {
					Message msg = Message.obtain(null, MSG_UNREGISTER_APPLICATION);
					msg.replyTo = mMessenger;
					mServiceMessenger.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service has
					// crashed.
				}
			}
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
			Log.d(LOGTAG, "Unbinding.");
		}
	}

	/**
	 * @param data
	 * @param action
	 */
	private void sendMessageToService(Bundle data, int action) {
		if (mIsBound) {
			if (mServiceMessenger != null) {
				try {
					Message msg = Message.obtain(null, action);
					msg.setData(data);
					msg.replyTo = mMessenger;
					mServiceMessenger.send(msg);
				} catch (RemoteException e) {
				}
			}
		}
	}

}
