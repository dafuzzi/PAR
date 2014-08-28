package de.uulm.par.notes;

import org.joda.time.DateTime;

import de.uulm.par.R;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class AddPerson extends ActionBarActivity {
	private String personMac;
	@SuppressLint("InlinedApi")
	private static final String[] PROJECTION = { Contacts.CONTENT_ITEM_TYPE

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_person);

		final TextView title = (TextView) findViewById(R.id.new_location_title);
		final TextView message = (TextView) findViewById(R.id.new_location_message);

		Button mac = (Button) findViewById(R.id.btn_addPerson);
		mac.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 100);
			}
		});

		Button add = (Button) findViewById(R.id.btn_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlainNote addingNote = new PlainNote(title.getText().toString(), message.getText().toString(), NoteType.PERSON, new DateTime());
				addingNote.setLocation("Uni Ulm");
				Intent returnIntent = new Intent();
				returnIntent.putExtra("Note", addingNote);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (100):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();

				Cursor cursor = getContentResolver().query(contactData, new String[] { ContactsContract.Contacts.DISPLAY_NAME }, null, null, null); // Cursor
				String mime = "";
				int dataIdx;
				int mimeIdx;
				int nameIdx;

				String name, mac;
				name = "";
				mac = "";

				if (cursor.moveToFirst()) {
					nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					name = cursor.getString(nameIdx);

					String[] projection = { ContactsContract.Data.DISPLAY_NAME, ContactsContract.Contacts.Data.DATA1, ContactsContract.Contacts.Data.MIMETYPE };

					cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, ContactsContract.Data.DISPLAY_NAME + " = ?", new String[] { name }, null);

					if (cursor.moveToFirst()) {
						mimeIdx = cursor.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE);
						dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Data.DATA1);
						do {
							// TODO match on mac address pattern but for now IM works fine
							mime = cursor.getString(mimeIdx);
							if (ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
								mac = cursor.getString(dataIdx);
							}
						} while (cursor.moveToNext());
					}
					//Log.d("Contact", name + " " + mac + " " + mime);
				}
			}
		}
	}

}
