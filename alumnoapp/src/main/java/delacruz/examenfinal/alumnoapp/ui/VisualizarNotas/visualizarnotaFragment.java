package delacruz.examenfinal.alumnoapp.ui.VisualizarNotas;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import delacruz.examenfinal.alumnoapp.Adapter.AdapterCurso;
import delacruz.examenfinal.alumnoapp.Entidades.Cursos;
import delacruz.examenfinal.alumnoapp.Entidades.Notas;
import delacruz.examenfinal.alumnoapp.Instancia.VolleySingleton;
import delacruz.examenfinal.alumnoapp.R;
import delacruz.examenfinal.alumnoapp.Util.UtilDTG;
import delacruz.examenfinal.alumnoapp.databinding.FragmentVisualizarnotaBinding;

public class visualizarnotaFragment extends Fragment {
    Spinner _spiCursoA;
    ListView _recLisNotas;

    String dni;//
    ArrayList<Notas> lstNotas;
    ArrayList<Cursos> lstCursos;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private visualizarnotaViewModel homeViewModel;
    private FragmentVisualizarnotaBinding binding;
    private static final String ARCHIVO_PREF = "alumno";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
        //        new ViewModelProvider(this).get(visualizarnotaViewModel.class);

        binding = FragmentVisualizarnotaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();
        lstNotas =new ArrayList<>();
        lstCursos =new ArrayList<>();
        _spiCursoA=root.findViewById(R.id.spiCursoA);
        _recLisNotas=root.findViewById(R.id.recLisNotas);


        SharedPreferences preferences = getActivity().getSharedPreferences(ARCHIVO_PREF, 0);
        dni=preferences.getString("DNI","76905898");
        verCursos();
        _spiCursoA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursos cursos=lstCursos.get(position);
                verNotas(cursos.getCodNota());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

//
    private void verCursos() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando alumnos...");
        progreso.show();
        String url = UtilDTG.RUTA+"consultarCursos.php?dni="+dni;
        url=url.replace(" ","%20");
        Log.i("TT","url PROFESOR - CURSO: "+url);
//        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Cursos cursos =null;
                JSONArray json = response.optJSONArray("tblCurso");
                try {
                    for(int i=0; i<json.length();i++){
                        cursos = new Cursos();
                        JSONObject jsonObject=null;
                        jsonObject = json.getJSONObject(i);
                        cursos.setCodCurso(jsonObject.getString("IDCurso"));
                        cursos.setCodNota(jsonObject.getString("IDAlumC"));
                        cursos.setCurNombre(jsonObject.getString("curNombre"));
                        lstCursos.add(cursos);
                    }
                    _spiCursoA.setAdapter(new ArrayAdapter<Cursos>(getContext(), android.R.layout.simple_spinner_item, lstCursos));
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
    private void verNotas(String codNotas) {
        lstNotas.clear();
        AdapterCurso adapterCurso = new AdapterCurso(getContext(), lstNotas);
        _recLisNotas.setAdapter(adapterCurso);
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando alumnos...");
        progreso.show();
        String url = UtilDTG.RUTA+"consultarCursoAlumno.php?codNota="+codNotas;
        url=url.replace(" ","%20");
        Log.i("TT","url PROFESOR - CURSO: "+url);
//        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Notas notas =null;
                JSONArray json = response.optJSONArray("tblCurso");
                Log.e("TT","Tamaño del jason : "+json.length());
                try {
                    for(int i=0; i<json.length();i++){
                        notas = new Notas();
                        JSONObject jsonObject=null;
                        jsonObject = json.getJSONObject(i);
                        notas.setCodigo(jsonObject.getString("IDCurso"));
                        notas.setCurso(jsonObject.getString("curNombre"));
                        notas.setSubcodigo(jsonObject.getString("IDNota"));
                        notas.setCriterio(jsonObject.getString("notCriterio"));
                        notas.setValor(jsonObject.getString("notValor"));
                        notas.setDescripcion(jsonObject.getString("notDescrip"));
                        Toast.makeText(getContext(), "Curso : "+ notas.getCurso(), Toast.LENGTH_SHORT).show();
                        lstNotas.add(notas);
                    }
                    AdapterCurso adapterCurso = new AdapterCurso(getContext(), lstNotas);
                    _recLisNotas.setAdapter(adapterCurso);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}