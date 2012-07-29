package net.coscolla.android.diasfestivos.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Festividades {
	private List<ViewModel> diasFestivos; 
	
	public enum TipoFestividad
	{
		Autonomica,
		Nacional,
		Local
	}
	
	public Festividades(String json)
	{
		this.diasFestivos= new LinkedList<Festividades.ViewModel>();
		try {
			JSONArray diasFestivos = new JSONArray(json);
			
			for(int i=0; i<diasFestivos.length(); i++)
			{
				JSONObject obj = diasFestivos.getJSONObject(i);
				ViewModel model = new ViewModel();
				model.nombreFestividad = obj.getString("nombre");
				model.tipoFestividad   = convertTipoFestividad(obj.getString("type"));
				model.diasRestantes = GetDiffDays(obj);
				model.fecha = " " + obj.getString("dia_semana")+ " " + obj.getString("day") + " " + convertMonthName(obj.getString("month")) + " " + obj.getString("ano") ;
				this.diasFestivos.add(model);
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public List<ViewModel> getDiasFestivos()
	{
		return this.diasFestivos;
	}
	
	private TipoFestividad convertTipoFestividad(String str)
	{
		if( str.equals("diaFiestaNacional"))
			return TipoFestividad.Nacional;
		else if( str.equals("diaFiestaLocal"))
			return TipoFestividad.Local;
		else if( str.equals("diaFiestaAutonomica"))
			return TipoFestividad.Autonomica;
		
		throw new RuntimeException("Invalid tipo festividad");
	}
	private String convertMonthName(String str)
	{
		int month = Integer.parseInt(str);
		return Constants.MONTHS[month-1];
		
	}
	private int GetDiffDays( JSONObject json) throws JSONException
	{
		int year = Integer.parseInt(json.getString("ano"));
		int month = Integer.parseInt(json.getString("month"));
		int day = Integer.parseInt(json.getString("day"));
		
		
		Date d = new Date(year - 1900, month-1, day);
		Date now = new Date();
		int days = getSignedDiffInDays(now, d);
		if( days < -1)
			days = -2;
		return days;
	}
	
	
	
	private final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

	private static long getDateToLong(Date date) {
	    return Date.UTC(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
	}

	public static int getSignedDiffInDays(Date beginDate, Date endDate) {
	    long beginMS = getDateToLong(beginDate);
	    long endMS = getDateToLong(endDate);
	    long diff = (endMS - beginMS) / (MILLISECS_PER_DAY);
	    return (int)diff;
	}

	public static int getUnsignedDiffInDays(Date beginDate, Date endDate) {
	    return Math.abs(getSignedDiffInDays(beginDate, endDate));
	}
	
	public class ViewModel
	{
		public TipoFestividad tipoFestividad;
		public String nombreFestividad;
		public int diasRestantes;
		public String fecha;
		
	}
}
