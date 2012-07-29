package net.coscolla.android.diasfestivos.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import net.coscolla.android.diasfestivos.Constants;
import net.coscolla.android.diasfestivos.UserSettings;
import net.coscolla.android.modules.Errorable;
import net.coscolla.android.modules.ExternalStorage;
import net.coscolla.android.modules.Http;

public class FestividadesLoader {

	private Handler handler;

	public void load(final Context ctx, final Handler handler)
	{
		UserSettings settings = new UserSettings(ctx);
		this.handler = handler;
		
		final String localidad_file = settings.localidad + ".json";
		AsyncTask<Void, Void, Errorable<String>> t = new AsyncTask<Void, Void, Errorable<String>>() {
			ProgressDialog dialog;
			
			@Override
			protected void onPreExecute() {
				if (ctx instanceof Activity) 
					dialog = ProgressDialog.show(ctx, "", "Cargando datos...", true);
			}
			@Override
			protected Errorable<String> doInBackground(Void... params) {
				return Http.Get( Constants.DIAS_FESTIVOS_ROOT_URL + localidad_file);
			}
			
			@Override
			protected void onPostExecute(Errorable<String> result) {
				if( dialog != null)
					dialog.dismiss();
				if(!result.getError()){
					ExternalStorage.write(result.getData(),localidad_file, ctx);
					loadAdapter(result);
				}
				
			}
		};
		Errorable<String> e = ExternalStorage.readString(localidad_file, ctx);
		if( e.getError())
			t.execute();
		else
			loadAdapter(e);
	}
	
	private void loadAdapter(Errorable<String> json)
	{
		if( json.getError())
			handler.error(json.getException());
		else
			handler.ok(new Festividades(json.getData()) );
	}
	
	public interface Handler
	{
		void ok(Festividades f);
		void error(Exception e);
	}
}
