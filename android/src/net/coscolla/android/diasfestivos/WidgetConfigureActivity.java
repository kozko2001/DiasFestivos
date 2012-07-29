package net.coscolla.android.diasfestivos;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.LocalidadDialogConfigure.LocalidadDialogHandler;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class WidgetConfigureActivity extends Activity{

	private int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	     setContentView(R.layout.main_activity); // blank activity
	     
	     Intent intent = getIntent();
	     Bundle extras = intent.getExtras();
	     if (extras != null) 
	    	 mAppWidgetId = extras.getInt(
	                 AppWidgetManager.EXTRA_APPWIDGET_ID,
	                 AppWidgetManager.INVALID_APPWIDGET_ID);
	  
	     if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) 
	         finish();
	     
	     
	     UserSettings settings = new UserSettings(this);
	     if( settings.comunidad != null && settings.provincia != null && settings.localidad != null)
	    	 ok();
	     else
	    	 new LocalidadDialogConfigure(this).showDialog(new LocalidadDialogHandler() {
				
				@Override
				public void complete() {
					ok();
				}
			});
	     
	}
	
	private void ok()
	{
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
	
}
