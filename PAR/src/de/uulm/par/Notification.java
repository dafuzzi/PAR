package de.uulm.par;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * @author Fabian Schwab
 * 
 */
public class Notification extends ResultReceiver {

	/**
	 * @param handler
	 */
	public Notification(Handler handler) {
		super(handler);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d("NOTIFY", "received");
	}

}
