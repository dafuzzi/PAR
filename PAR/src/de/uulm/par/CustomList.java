package de.uulm.par;

import java.util.LinkedList;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.uulm.par.notes.PlainNote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Fabian Schwab
 * 
 */
@SuppressLint("ViewHolder") public class CustomList extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] title;
	private final Integer[] imageId;
	private final LinkedList<PlainNote> details;
	
	/**
	 * @param context
	 * @param title
	 * @param imageId
	 * @param details
	 */
	public CustomList(Activity context, String[] title, Integer[] imageId,
			LinkedList<PlainNote> details) {
		super(context, R.layout.list_single, title);
		this.context = context;
		this.title = title;
		this.imageId = imageId;
		this.details = details;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single, null, true);

		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		TextView txtDetail = (TextView) rowView.findViewById(R.id.detail);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM d. YYYY HH:mm");
		
		txtTitle.setText(title[position]);
		imageView.setImageResource(imageId[position]);

		switch (details.get(position).getType()) {
		case PERSON:
			txtDetail.setText("When " + details.get(position).getPerson() + " is near you.");
			break;
		case LOCATION:
			txtDetail.setText("When you are at " + details.get(position).getLocation());
			break;
		case DATETIME:
			txtDetail.setText(formatter.print(details.get(position).getAlert()));
			break;

		default:
			txtDetail.setText("");
			break;
		}

		return rowView;
	}
}