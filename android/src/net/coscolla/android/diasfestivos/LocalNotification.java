package net.coscolla.android.diasfestivos;

import java.util.Calendar;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.model.Festividades;
import net.coscolla.android.diasfestivos.model.Festividades.ViewModel;
import net.coscolla.android.modules.Errorable;
import net.coscolla.android.modules.ExternalStorage;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class LocalNotification extends BroadcastReceiver {

	private UserSettings settings;
	
	public static void setAlarm(Context ctx)
	{
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);


		Intent broadcast_intent = new Intent(ctx, LocalNotification.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0,  broadcast_intent, 0);


		AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		settings = new UserSettings(context);
		if( settings.localidad != null )
		{
			Errorable<String> e = ExternalStorage.readString(settings.localidad + ".json", context);
			if( !e.getError())
			{
				Festividades f = new Festividades(e.getData());
				for( Festividades.ViewModel vm : f.getDiasFestivos() )
				{
					if(vm.diasRestantes == 0 || vm.diasRestantes == -1 && !settings.fecha.equals(vm.fecha))
						createNotification(context, vm);
				}
			}
		}
		// Set alarm for next time ;)
		setAlarm(context);
	}

	private void createNotification(Context context, ViewModel model) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
				
		  Notification notification = new NotificationCompat.Builder(context)
		    .setContentTitle ("Á" + (model.diasRestantes == 0 ? "Hoy es " : "Ma–ana sera") + " fiesta!")
		    .setContentText(model.nombreFestividad)
		    .setSmallIcon(R.drawable.ic_launcher)
		    .setContentIntent(pending)
		    .getNotification();
		  
		  
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 
		  NotificationManager notificationManager = 	  (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		  notificationManager.notify(0, notification);
		  
		  settings.setFecha(model.fecha);
		
	}
}
