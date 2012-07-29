package net.coscolla.android.diasfestivos;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.LocalidadDialogConfigure.LocalidadDialogHandler;
import net.coscolla.android.diasfestivos.model.Festividades;
import net.coscolla.android.diasfestivos.model.FestividadesLoader;
import net.coscolla.android.diasfestivos.model.Localidades;
import net.coscolla.android.diasfestivos.model.Localidades.ViewModel;
import net.coscolla.android.modules.Errorable;
import net.coscolla.android.modules.ExternalStorage;
import net.coscolla.android.modules.Http;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends SherlockListActivity {

	private Localidades localidades;
	private Festividades dias_festivos;
	private ArrayAdapter<net.coscolla.android.diasfestivos.model.Festividades.ViewModel> adapter;
	private UserSettings settings;
	private LocalidadDialogConfigure dialogConfigure;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_activity);
        
        settings = new UserSettings(this);
        dialogConfigure = new LocalidadDialogConfigure(this);
        
        setAlarmManager();
        LocalNotification n = new LocalNotification();
        n.onReceive(this, null);

		if( settings.comunidad == null || settings.provincia == null || settings.localidad == null)
			dialogConfigure.showDialog(new LocalidadDialogHandler() {
				
				@Override
				public void complete() {
					loadFestividades();
				}
			});
		else
			loadFestividades();
    }
	
    

	

	private void loadFestividades() 
	{
		FestividadesLoader loader = new FestividadesLoader();
		loader.load(this, new FestividadesLoader.Handler() {
			
			@Override
			public void ok(Festividades f) {
				loadAdapter(f);
			}
			
			@Override
			public void error(Exception e) {
				
			}
		});
	}
	
	private void loadAdapter(Festividades f)
	{
		dias_festivos = f;
		
		setAlarmManager();
		
		adapter = new FestividadesAdapter(MainActivity.this, dias_festivos.getDiasFestivos());
		getListView().setAdapter(adapter);
	}
	
	private void setAlarmManager() {
		LocalNotification.setAlarm(this);
	}


}
