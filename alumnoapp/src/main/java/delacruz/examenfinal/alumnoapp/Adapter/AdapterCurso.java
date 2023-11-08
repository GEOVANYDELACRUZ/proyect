package delacruz.examenfinal.alumnoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import delacruz.examenfinal.alumnoapp.Entidades.Notas;
import delacruz.examenfinal.alumnoapp.R;

public class AdapterCurso extends BaseAdapter {
    private Context context;
    private ArrayList<Notas> arrayList;

    public AdapterCurso() {
    }

    public AdapterCurso(Context context, ArrayList<Notas> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.item,null);
        }
        TextView txvValor = convertView.findViewById(R.id.txtValor);
        TextView txvCrit = convertView.findViewById(R.id.txtCriterio);
        TextView txvDesc = convertView.findViewById(R.id.txtDescr);

        txvValor.setText(arrayList.get(position).getValor());
        txvCrit.setText(arrayList.get(position).getCriterio());
        txvDesc.setText(arrayList.get(position).getDescripcion());

        txvValor.setTextSize(20);
        return convertView;
    }
}
