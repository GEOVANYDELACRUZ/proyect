package delacruz.examenfinal.proyectofinal.ui.asignarnota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import delacruz.examenfinal.proyectofinal.Entidad.Curso;
import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.databinding.FragmentAsignarnotasBinding;

public class AsignarNotas extends Fragment {

    Spinner spiCursos;
    TextInputEditText edtCriterioCa;
    MaterialButton btnIrCalif;
    LinearLayout lytAN2;
    ArrayList<Curso> lstCurso=null;

    String codigoProf;
    String codigoCurso;
    String curDescrip;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private AsignarNotasViewModel galleryViewModel;
    private FragmentAsignarnotasBinding binding;
    private static final String ARCHIVO_PREF = "profesor";
    private static final String ARCHIVO_CURSO = "curso";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        galleryViewModel =
//                new ViewModelProvider(this).get(AsignarNotasViewModel.class);

        binding = FragmentAsignarnotasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//       // final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //textView.setText(s);
//            }
//        });
        spiCursos = root.findViewById(R.id.spiANCursos);
        edtCriterioCa = root.findViewById(R.id.edtANCriterio);
        btnIrCalif = root.findViewById(R.id.btnANIrCalificar);

        //BLOQUE SHAREDPREFERENCES "DATOS DE PROFESOR"
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ARCHIVO_PREF,0);
        //        private static final String ARCHIVO_PREF = "profesor";
        codigoProf = sharedPreferences.getString("IDProf","nnn");

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();

        lstCurso=new ArrayList<Curso>();
        verCursos();
        //Apoyo
        lytAN2 = root.findViewById(R.id.lytAN2);


//        String texto_Item[]= .toArray(new String[lst_textos.size()]);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,texto_Item);
//        spiCursos.setAdapter(adapter);
        spiCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curso curso = lstCurso.get(position);
                codigoCurso=curso.getCodigo();
                curDescrip=curso.getCurso();

//                Toast.makeText(getContext(), "Curso :"+curso.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnIrCalif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarcalif();
            }
        });



        return root;
    }

    private void verCursos() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando alumnos...");
        progreso.show();
        String url = UtilDTG.RUTA+"consultarProfesor.php?profesor="+codigoProf;
        url=url.replace(" ","%20");
        Log.i("TT","url PROFESOR - CURSO: "+url);
//        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Curso curso=null;
                JSONArray json = response.optJSONArray("tblCurso");
                Log.i("TT","Tamaño del jason : "+json.length());
                try {
                    for(int i=0; i<json.length();i++){
                        curso = new Curso();
                        JSONObject jsonObject=null;
                        jsonObject = json.getJSONObject(i);
                        curso.setCodigo(jsonObject.getString("IDCurso"));
                        curso.setCurso(jsonObject.getString("curNombre"));
                        Log.i("Cursos :",curso.toString());
                        lstCurso.add(curso);
                    }
                    ArrayAdapter<Curso> adapter = new ArrayAdapter<Curso>(getContext(), android.R.layout.simple_spinner_item, lstCurso);
                    spiCursos.setAdapter(adapter);
                }catch (Exception e){
                    Log.i("TT","Error al listar CURSOS : "+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.i("TT","ERROR AL LISTAR/CURSO: "+error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void guardarcalif() {
        //                      Guardar valores en el SHARED PREFERENCES
//                private static final String ARCHIVO_PREF = "curso";
        SharedPreferences preferences = getActivity().getSharedPreferences(ARCHIVO_CURSO, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("IDCurso", codigoCurso);
        editor.putString("IDProf", codigoProf);
        editor.putString("Criterio",edtCriterioCa.getText().toString());
        editor.putString("Descripcion",curDescrip);
        editor.commit();
        Intent i = new Intent(getContext(),AsignarNotasAlum.class);
        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}