package net.coscolla.android.diasfestivos;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.model.Festividades;
import net.coscolla.android.diasfestivos.model.FestividadesLoader;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetBroadcastReceicer extends AppWidgetProvider{

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

		FestividadesLoader loader = new FestividadesLoader();
		loader.load(context, new FestividadesLoader.Handler() {
			
			@Override
			public void ok(Festividades f) {
				updateWidget(context, appWidgetManager, appWidgetIds, f);
				
			}
			
			@Override
			public void error(Exception e) {
			}
		});

	}
	
	private void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Festividades f)
	{
		Festividades.ViewModel model = null;
		for( Festividades.ViewModel vm : f.getDiasFestivos())
		{
			if( vm.diasRestantes >= 0 )
			{
				model = vm;
				break;
			}
		}
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, WidgetBroadcastReceicer.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {


			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

			if( model == null){
				remoteViews.setTextViewText(R.id.countDown, "-");
				remoteViews.setTextViewText(R.id.dia_festivo, "");
			}else{
				remoteViews.setTextViewText(R.id.countDown, String.valueOf(model.diasRestantes) + " d’as");
				remoteViews.setTextViewText(R.id.dia_festivo, "para " + model.nombreFestividad);
			}
			
			// Register an onClickListener
			Intent intent = new Intent(context, MainActivity.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.countDown	, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
