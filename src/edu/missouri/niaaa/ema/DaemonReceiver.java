package edu.missouri.niaaa.ema;

import java.util.Calendar;

import edu.missouri.niaaa.ema.location.LocationUtilities;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class DaemonReceiver extends BroadcastReceiver {

	final static String TAG = "Daemon Receiver";
	long tolerace = 60*1000;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Utilities.Log(TAG, "on receiver daemon");
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		int fun = intent.getIntExtra(Utilities.BD_ACTION_DAEMON_FUNC, 0); 
		
		if(fun == 0){//set alarm
			Utilities.Log(TAG, "on receiver daemon 0");
			//Noon
			Intent itTrigger1 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger1.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 1);//int
			PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20), piTrigger1);
			
			//Midnight
			Intent itTrigger2 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger2.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 2);//int
			PendingIntent piTrigger2 = PendingIntent.getBroadcast(context, 2, itTrigger2, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(23, 59), piTrigger2);
			
			//Three oclock
			Intent itTrigger3 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger3.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 3);//int
			PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(3, 0), piTrigger3);
			
			//Nine oclock reminder charge phone and bluetooth device 
			Intent itTrigger4 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger4.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 4);//int
			PendingIntent piTrigger4 = PendingIntent.getBroadcast(context, 4, itTrigger4, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(21, 0), piTrigger4);
			
		}
		else if(fun == 1){//Noon
			Utilities.Log(TAG, "on receiver daemon 1");
			
    		//today at noon
			Utilities.morningComplete(context, true);

			Toast.makeText(context, "Noon daemon trigger random popups for you.", Toast.LENGTH_LONG).show();
			
			//Noon
			Intent itTrigger1 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger1.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 1);//int
			PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20), piTrigger1);
		}
		else if(fun == -1){//cancel noon
			Utilities.Log(TAG, "on receiver daemon -1");
			
			Intent itTrigger1 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger1.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 1);//int
			PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20)+Utilities.getDayLong(), piTrigger1);
		}
		else if(fun == 2){//Midnight
			Utilities.Log(TAG, "on receiver daemon 2");
			
			//close sensor
			
			
			//cancel all survey (follow-ups are allowed base on new requirement)
			Utilities.cancelSchedule(context);
			
			//reset sp
//			Utilities.getSP(context, Utilities.SP_RANDOM_TIME).edit().clear().commit();
//			Utilities.getSP(context, Utilities.SP_SURVEY).edit().clear().commit();
			
			
			Toast.makeText(context, "MIND close sensor and cancel all the survey", Toast.LENGTH_LONG).show();
			
			//Midnight
			Intent itTrigger2 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger2.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 2);//int
			PendingIntent piTrigger2 = PendingIntent.getBroadcast(context, 2, itTrigger2, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(23, 59), piTrigger2);
		}
		else if(fun == 3){//three oclock
			Utilities.Log(TAG, "on receiver daemon 3");
			
			//close location
			context.sendBroadcast(new Intent(LocationUtilities.ACTION_STOP_LOCATION));
			
			//next day at 3
			//default
			Calendar d = Utilities.getDefaultMorningCal(context);
			long defTime = d.getTimeInMillis();
			int hour = d.get(Calendar.HOUR_OF_DAY);
			int minute = d.get(Calendar.MINUTE);
			
			//current
			Calendar c = Calendar.getInstance();
			
			//morning
			Calendar m = Calendar.getInstance();
			m.setTimeInMillis(Utilities.getSP(context, Utilities.SP_BED_TIME).getLong(Utilities.SP_KEY_BED_TIME_LONG, -1));
			
			if(c.before(m)){
				//set m as morning
				hour = m.get(Calendar.HOUR_OF_DAY);
				minute = m.get(Calendar.MINUTE);
			}
			
			Utilities.bedtimeComplete(context, hour, minute);
			
			//cancel followup
			Utilities.cancelTrigger(context);
			
			//reset sp
//			Utilities.getSP(context, Utilities.SP_RANDOM_TIME).edit().clear().commit();
//			Utilities.getSP(context, Utilities.SP_SURVEY).edit().clear().commit();
			
			Toast.makeText(context, "THRE cancel survey for you", Toast.LENGTH_LONG).show();
			
			//Three oclock
			Intent itTrigger3 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger3.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 3);//int
			PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(3, 0), piTrigger3);
			
			//reset all, send 0 broadcast??
			
			
		}
		else if(fun == -3){//cancel three oclock //useless for now
			Utilities.Log(TAG, "on receiver daemon -3");
			
//			Intent itTrigger3 = new Intent(Utilities.BD_ACTION_DAEMON);
//			itTrigger3.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 3);//int
//			PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);
//			
//			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(3, 0)+Utilities.getDayLong(), piTrigger3);
		}
		else if(fun == 4){
			 /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			  alertDialogBuilder.setTitle("Charge Reminder");
		      alertDialogBuilder.setMessage("Please remember to charge your Phone and Other Devices before you go to bed.");
		      alertDialogBuilder.setCancelable(true);
		      alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
			   });
			    
		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();*/
			
			//Toast.makeText(context, "This is where the alertDialog will show", Toast.LENGTH_LONG).show();
			
			
			
			
			Intent i = new Intent(context.getApplicationContext(), ChargeReminder.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			
			
			
			//Nine oclock reminder charge phone and bluetooth device 
			Intent itTrigger4 = new Intent(Utilities.BD_ACTION_DAEMON);
			itTrigger4.putExtra(Utilities.BD_ACTION_DAEMON_FUNC, 4);//int
			PendingIntent piTrigger4 = PendingIntent.getBroadcast(context, 4, itTrigger4, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(21, 0), piTrigger4);
			
			Log.d("Nine oclock reminder", "Reseting the nine oclock reminder for the next day");
			Toast.makeText(context, "Reseting the 9pm reminder for tomorrow", Toast.LENGTH_LONG).show();
			
			
		}
		else{
			
		}
		
	}
	
	private long getProperTime(int hour, int minute){
		Calendar c = Calendar.getInstance();
		Calendar s = Calendar.getInstance();
		s.set(Calendar.HOUR_OF_DAY, hour);
		s.set(Calendar.MINUTE, minute);
		s.set(Calendar.SECOND, 0);
		s.set(Calendar.MILLISECOND, 0);
		if(c.after(s)){
			return s.getTimeInMillis() + Utilities.getDayLong();
		}
		return s.getTimeInMillis();
	}

}
