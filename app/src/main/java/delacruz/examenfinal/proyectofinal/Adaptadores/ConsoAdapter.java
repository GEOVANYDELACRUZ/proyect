package delacruz.examenfinal.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import delacruz.examenfinal.proyectofinal.Entidad.Notas;
import delacruz.examenfinal.proyectofinal.R;

public class ConsoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notas> arrayListNotas;

    public ConsoAdapter() {
    }

    public ConsoAdapter(Context context, ArrayList<Notas> arrayListNotas) {
        this.context = context;
        this.arrayListNotas = arrayListNotas;
    }

    @Override
    public int getCount() {
        return arrayListNotas.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListNotas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.itemconso,null);
        }
        TextView txtDNI = convertView.findViewById(R.id.txtCONSDNI);
        TextView txtCriterio = convertView.findViewById(R.id.txtCONSCriterio);
        TextView txtNota = convertView.findViewById(R.id.txtCONSNota);
        TextView txtAlumnoNom = convertView.findViewById(R.id.txtCONSAlumnoNomb);
        TextView txtAlumnoAp = convertView.findViewById(R.id.txtCONSAlumnoApelli);

        txtDNI.setText(arrayListNotas.get(position).getDNI());
        txtCriterio.setText(arrayListNotas.get(position).getAlumCriterio());
        txtNota.setText(arrayListNotas.get(position).getAlumNota());
        txtAlumnoNom.setText(arrayListNotas.get(position).getAlumNombre());
        txtAlumnoAp.setText(arrayListNotas.get(position).getApPaterno());

        txtNota.setTextSize(20);
        return convertView;
    }
}
