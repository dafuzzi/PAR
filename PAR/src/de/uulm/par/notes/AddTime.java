package de.uulm.par.notes;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.uulm.par.R;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Fabian Schwab
 * 
 */
public class AddTime extends ActionBarActivity {

	private TimePickerFragment tpf;
	private DatePickerFragment dpf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_time);
		
		Button timeSet = (Button) findViewById(R.id.btn_setTime);
		timeSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tpf = new TimePickerFragment();
				tpf.show(getSupportFragmentManager(), "timePicker");
			}
		});
		
		Button dateSet = (Button) findViewById(R.id.btn_setDate);
		dateSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dpf = new DatePickerFragment();
				dpf.show(getSupportFragmentManager(), "datePicker");
			}
		});
		
		final TextView title = (TextView) findViewById(R.id.new_time_title);
		final TextView message = (TextView) findViewById(R.id.new_time_message);
		
		Button add = (Button) findViewById(R.id.btn_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlainNote addingNote = new PlainNote(
						title.getText().toString(), message.getText()
								.toString(), NoteType.DATETIME, new DateTime());
				
				DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
				DateTime alert = formatter.parseDateTime(dpf.getDate() + " "  +tpf.getTime());
				addingNote.setAlert(alert);
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("Note",addingNote);
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});
		
	}
	
	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		String time;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			time = hourOfDay + ":" + minute;
		}
		public String getTime(){
			return time;
		}
	}
	
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		String date;
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			month++;
			date = day + "." + month + "." + year;
		}
		public String getDate(){
			return date;
		}
	}
}
