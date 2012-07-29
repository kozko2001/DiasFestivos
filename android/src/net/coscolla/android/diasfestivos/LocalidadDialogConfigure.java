package net.coscolla.android.diasfestivos;

import java.util.LinkedList;
import java.util.List;

import net.coscolla.android.diasfestivos.model.Localidades;
import net.coscolla.android.diasfestivos.model.Localidades.ViewModel;
import net.coscolla.android.modules.Errorable;
import net.coscolla.android.modules.Http;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

public class LocalidadDialogConfigure {

	private Localidades localidades;
	private Context     ctx;
	private UserSettings settings;
	private LocalidadDialogHandler handler;
	
	public LocalidadDialogConfigure(Context ctx)
	{
		this.ctx = ctx;
		this.settings = new UserSettings(ctx);
	}
	
	public void showDialog(LocalidadDialogHandler handler)
	{
		this.handler = handler;
	    AsyncTask<Void, Void, Errorable<String>> t = new AsyncTask<Void, Void, Errorable<String>>() {
			
			@Override
			protected Errorable<String> doInBackground(Void... params) {
				return Http.Get( Constants.DIAS_FESTIVOS_ROOT_URL + "comunidades.json");
			}
			
			@Override
			protected void onPostExecute(Errorable<String> json) {
				if( json.getError() )
					Toast.makeText(ctx, "No hay connexi—n a internet", Toast.LENGTH_LONG).show();

				localidades = new Localidades(json.getData());
				showLocalidades();
			}
		};
		t.execute();
	}
	
    private void showLocalidades()
    {
    	if(settings.comunidad == null)
    		showLocalidades(localidades.getComunidades());
    	else if( settings.provincia == null)
    		showLocalidades(localidades.getProvincias(settings.comunidad));
    	else if( settings.localidad == null )
    		showLocalidades(localidades.getLocalidades(settings.comunidad, settings.provincia));
    	
    }
    

	private void showLocalidades(final List<ViewModel> vm )
	{
		String[] items = convertLocalidadViewModel(vm);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Escoge una comunidad");
		
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        String code = vm.get(item).code;
		        localidadClicked(code);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	

	private void localidadClicked(String code)
	{
		if( settings.comunidad == null)
			settings.comunidad = code;
		else if( settings.provincia == null)
			settings.provincia = code;
		else if( settings.localidad == null)
		{
			settings.localidad = code;
			settings.storeSettings();
			handler.complete();
			return;
		}
		showLocalidades();
	}
	
	private String[] convertLocalidadViewModel(List<ViewModel> l)
	{
		LinkedList<String> r = new LinkedList<String>();
		for( ViewModel vm : l)
			r.add(vm.name);
		return r.toArray(new String[]{});
	}
	
	public interface LocalidadDialogHandler
	{
		void complete();
	}
	
}
