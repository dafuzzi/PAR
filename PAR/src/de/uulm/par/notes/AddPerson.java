package de.uulm.par.notes;

import org.joda.time.DateTime;

import de.uulm.par.R;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

public class AddPerson extends ActionBarActivity {
	private String personMac;
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
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 100);
			}
		});
		
		Button add = (Button) findViewById(R.id.btn_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlainNote addingNote = new PlainNote(
						title.getText().toString(), message.getText()
								.toString(), NoteType.PERSON, new DateTime());
				addingNote.setLocation("Uni Ulm");
				Intent returnIntent = new Intent();
				returnIntent.putExtra("Note", addingNote);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}
	@Override public void onActivityResult(int reqCode, int resultCode, Intent data){ super.onActivityResult(reqCode, resultCode, data);

    switch(reqCode)
    {
       case (100):
         if (resultCode == Activity.RESULT_OK)
         {
             Uri contactData = data.getData();
             @SuppressWarnings("deprecation")
			Cursor c = managedQuery(contactData, null, null, null, null);
          if (c.moveToFirst())
          {
          String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

          String hasPhone =
          c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

          if (hasPhone.equalsIgnoreCase("1")) 
          {
          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
            phones.moveToFirst();
            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
            //setCn(cNumber);
          }
         }
       }
    }
   }

}
