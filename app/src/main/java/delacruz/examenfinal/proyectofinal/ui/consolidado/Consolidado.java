package delacruz.examenfinal.proyectofinal.ui.consolidado;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import delacruz.examenfinal.proyectofinal.Adaptadores.ConsoAdapter;
import delacruz.examenfinal.proyectofinal.Entidad.Curso;
import delacruz.examenfinal.proyectofinal.Entidad.Notas;
import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.databinding.FragmentConsolidadoBinding;


public class Consolidado extends Fragment {
    Spinner spinCursos;
    TextView txtCantEst;
    MaterialButton btnGuardarLis;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    ArrayList<Notas> lstDetNotas;
    ArrayList<Curso> lstCurso;

    String codigoProf,codCurso;

    ListView listConso;

    private ConsolidadoViewModel slideshowViewModel;
    private static final String ARCHIVO_PREF = "profesor";
    private FragmentConsolidadoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(ConsolidadoViewModel.class);

        binding = FragmentConsolidadoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //BLOQUE SHAREDPREFERENCES "DATOS DE PROFESOR"
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ARCHIVO_PREF,0);
        //        private static final String ARCHIVO_PREF = "profesor";
        codigoProf = sharedPreferences.getString("IDProf","nnn");

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();

//        //final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//       //        textView.setText(s);
//            }
//        });
        spinCursos = root.findViewById(R.id.spiCCursos);
        txtCantEst = root.findViewById(R.id.txtCNumEstudiantes);
        listConso = root.findViewById(R.id.listCONSO);



        lstCurso=new ArrayList<>();
        verCursos();

        lstDetNotas = new ArrayList<>();
        spinCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curso curso=lstCurso.get(position);
                codCurso= curso.getCodigo();
                verConso();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return root;
    }

    private void verConso() {
        lstDetNotas.clear();
        ConsoAdapter consoAdapter = new ConsoAdapter(getContext(), lstDetNotas);
        listConso.setAdapter(consoAdapter);
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando consolidado...");
        progreso.show();
        String url = UtilDTG.RUTA+"consultarCursoAlumno.php?codNota="+codCurso;
        url=url.replace(" ","%20");
        Log.e("TT","url ALUMNO - NOTAS: "+url);
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
                        notas.setDNI(jsonObject.getString("DNI"));
                        notas.setAlumCriterio(jsonObject.getString("notCriterio"));
                        notas.setApPaterno(jsonObject.getString("alumAPaterno"));
                        notas.setAlumNombre(jsonObject.getString("alumNombres"));
                        notas.setAlumNota(jsonObject.getString("notValor"));
                        lstDetNotas.add(notas);
                    }
                    ConsoAdapter adapterCurso = new ConsoAdapter(getContext(), lstDetNotas);
                    listConso.setAdapter(adapterCurso);
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

//    public void exportarCSV() {
//        File carpeta = new File(Environment.getExternalStorageDirectory() + "/ExportarSQLiteCSV");
//        String archivoAgenda = carpeta.toString() + "/" + "Usuarios.csv";
//
//        boolean isCreate = false;
//        if(!carpeta.exists()) {
//            isCreate = carpeta.mkdir();
//        }
//
//        try {
//            FileWriter fileWriter = new FileWriter(archivoAgenda);
//
////            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
//
////            SQLiteDatabase db = admin.getWritableDatabase();
//
////            Cursor fila = db.rawQuery("select * from usuarios", null);
//
//            if(fila != null && fila.getCount() != 0) {
//                fila.moveToFirst();
//                do {
//
//                    fileWriter.append(fila.getString(0));
//                    fileWriter.append(",");
//                    fileWriter.append(fila.getString(1));
//                    fileWriter.append(",");
//                    fileWriter.append(fila.getString(2));
//                    fileWriter.append("\n");
//
//                } while(fila.moveToNext());
//            } else {
//                Toast.makeText(getContext(), "No hay registros.", Toast.LENGTH_LONG).show();
//            }
//
//            db.close();
//            fileWriter.close();
//            Toast.makeText(getContext(), "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();
//
//        } catch (Exception e) { }
//    }


    private void verCursos() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando alumnos...");
        progreso.show();
        String url = UtilDTG.RUTA+"consultarProfesor.php?profesor="+codigoProf;
        url=url.replace(" ","%20");
        Log.e("TT","url PROFESOR - CONSO: "+url);
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
                        Toast.makeText(getContext(), "Curso : "+curso.getCurso(), Toast.LENGTH_SHORT).show();
                        lstCurso.add(curso);
                    }
                    ArrayAdapter<Curso> adapter = new ArrayAdapter<Curso>(getContext(), android.R.layout.simple_spinner_item, lstCurso);
                    spinCursos.setAdapter(adapter);
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