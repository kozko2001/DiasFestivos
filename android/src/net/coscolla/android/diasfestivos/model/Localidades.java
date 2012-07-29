package net.coscolla.android.diasfestivos.model;

import java.util.LinkedList;
import java.util.List;

import net.coscolla.android.diasfestivos.model.Localidades.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Localidades {

	private JSONArray comunidades;
	
	public Localidades(String json)
	{
		try {
			comunidades = new JSONArray(json);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ViewModel> getComunidades()
	{
		return convertToViewModel(comunidades);
	}
	

	public List<ViewModel> getProvincias(String comunidad) {
		for( int i = 0 ; i< comunidades.length(); i++)
		{
			try {
				JSONObject comunidad_json = comunidades.getJSONObject(i);
				if ( comunidad_json.getString("code").equals(comunidad))
				{
					JSONArray prov = comunidad_json.getJSONArray("prov");
					return convertToViewModel(prov);
				}
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		
		throw new RuntimeException("getProvincias no comunidad found...");
	}
	
	
	public List<ViewModel> getLocalidades(String comunidad, String provincia) {
		for( int i = 0 ; i< comunidades.length(); i++)
		{
			try {
				JSONObject comunidad_json = comunidades.getJSONObject(i);
				if ( comunidad_json.getString("code").equals(comunidad))
				{
					JSONArray prov = comunidad_json.getJSONArray("prov");
					for( int j = 0 ; j< prov.length(); j++)
					{
						JSONObject prov_json = prov.getJSONObject(j);
						if ( prov_json.getString("code").equals(provincia))
						{
							JSONArray localidad = prov_json.getJSONArray("local");
							return convertToViewModel(localidad);
						}
					}
					return convertToViewModel(prov);
				}
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		
		throw new RuntimeException("getProvincias no comunidad found...");
	}
	
	private List<ViewModel> convertToViewModel(JSONArray json)
	{
		LinkedList<ViewModel> v = new LinkedList<Localidades.ViewModel>();
		
		for( int i = 0 ; i< json.length(); i++)
		{
			try {
				JSONObject comunidad_json = json.getJSONObject(i);
				v.add(new ViewModel(comunidad_json.getString("name"), comunidad_json.getString("code")));
			} catch (JSONException e) {
			}
		}
		return v;
	}

	
	public class ViewModel
	{
		public String name;
		public String code;
		
		public ViewModel(String name, String code)
		{
			this.code = code;
			this.name = name;
		}
	}



}
