package net.coscolla.android.diasfestivos;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {
	public String comunidad, provincia, localidad;
	public String fecha;
	
	private Context ctx;
	
	public UserSettings(Context ctx)
	{
		this.ctx = ctx;
		loadSettings();
	}
	
	public void loadSettings()
	{
		SharedPreferences pref = ctx.getSharedPreferences("DiasFestivos", Context.MODE_PRIVATE);
		comunidad =  pref.getString("comunidad", null);
		provincia =  pref.getString("provincia", null);
		localidad =  pref.getString("localidad", null);
	}
	
	public void storeSettings()
	{
		SharedPreferences pref = ctx.getSharedPreferences("DiasFestivos", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("comunidad", comunidad);
		editor.putString("provincia", provincia);
		editor.putString("localidad", localidad);
		editor.commit();
	}
	
	public void setFecha(String fecha)
	{
		SharedPreferences pref = ctx.getSharedPreferences("DiasFestivos", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("fecha", fecha);
		editor.commit();
	}
	
	public String getFecha() 
	{
		SharedPreferences pref = ctx.getSharedPreferences("DiasFestivos", Context.MODE_PRIVATE);
		fecha =  pref.getString("fecha", "");
		return fecha;
	}
}
