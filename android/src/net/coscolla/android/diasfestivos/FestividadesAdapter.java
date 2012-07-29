package net.coscolla.android.diasfestivos;

import java.util.List;

import net.coscolla.andrioid.diasfestivos.R;
import net.coscolla.android.diasfestivos.model.Festividades;
import net.coscolla.android.diasfestivos.model.Festividades.ViewModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FestividadesAdapter extends ArrayAdapter<Festividades.ViewModel> {

	private LayoutInflater inflater;

	
	public FestividadesAdapter(Context context, List<ViewModel> objects) {
		super(context, R.layout.list_item, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView ;
		if( convertView != null)
			rowView = convertView;
		else 
			rowView = inflater.inflate(R.layout.list_item, null, false);
		
		TextView dias_restantes = (TextView) rowView.findViewById(R.id.dias_restantes);
		TextView fecha          = (TextView) rowView.findViewById(R.id.fecha);
		TextView nombre 		= (TextView) rowView.findViewById(R.id.nombre_festividad);
		
		ViewModel model = this.getItem(position);
		if( model.diasRestantes <= 0 )
			dias_restantes.setText(String.valueOf("-"));
		else
			dias_restantes.setText(String.valueOf(model.diasRestantes));
		
		fecha.setText(model.fecha);
		nombre.setText(model.nombreFestividad);
		
		return rowView;
	}

}
