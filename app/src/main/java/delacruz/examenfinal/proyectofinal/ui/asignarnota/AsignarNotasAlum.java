package delacruz.examenfinal.proyectofinal.ui.asignarnota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Locale;

import delacruz.examenfinal.proyectofinal.Entidad.Alumnos;
import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;

public class AsignarNotasAlum extends AppCompatActivity {
    Spinner spinAlumnos;
    TextInputEditText edtNota,edtDescrip;
    MaterialButton btnListo;
    ImageButton btnHablar;
    ImageButton btnHablar2;
    Button btnSalir;

    ArrayList<Alumnos> lstAlum=null;

    String codCurso;
    String codProfesor;
    String curso;
    String codAlumno;
    String criterio;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private static final String ARCHIVO_CURSO = "curso";
    private static final int REQ_CODE_SPECH_INPUT = 100;


    //Apoyo
    LinearLayout lytANA2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPECH_INPUT:{
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtDescrip.setText((result.get(0)));
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_notas_alum);

        spinAlumnos = findViewById(R.id.spiANAAlumnos);
        edtNota = findViewById(R.id.edtANANota);
        edtDescrip = findViewById(R.id.edtANADescripcion);

        btnListo = findViewById(R.id.btnANAListo);
        btnHablar = findViewById(R.id.btnANAHablar);
        btnSalir = findViewById(R.id.btnANASalir);
        lstAlum=new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

//                private static final String ARCHIVO_CURSO = "curso";
        SharedPreferences preferences = getSharedPreferences(ARCHIVO_CURSO, 0);
        codCurso = preferences.getString("IDCurso","nnn");
        codProfesor = preferences.getString("IDProf","nnn");
        criterio = preferences.getString("Criterio","nnn");
        curso = preferences.getString("Descripcion","nnn");
//        Toast.makeText(this, codCurso+" : "+criterio, Toast.LENGTH_SHORT).show();
        cargarAlumnos();

        //Apoyo
        lytANA2 = findViewById(R.id.lytANA2);

        spinAlumnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Alumnos alumnos=lstAlum.get(position);codAlumno=alumnos.getCodAlumno();
                Toast.makeText(view.getContext(), codCurso+" : "+criterio+"\nEstudiante :"+codAlumno, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Escuchando...");
                    try{
                        startActivityForResult(i,REQ_CODE_SPECH_INPUT);
                    }catch(Exception e){
                        Toast.makeText(AsignarNotasAlum.this, "Error al reconocer", Toast.LENGTH_SHORT).show();
                    }
                }

        });



        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarnota();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarAlumnos() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cargando alumnos...");
//        progreso.show();
        String url = UtilDTG.RUTA+"consultarCursosProfesor.php?" +
                "curso=" +codCurso+
                "&prof="+codProfesor;
        url=url.replace(" ","%20");
        Log.i("TT","url ALUMNO - CURSO: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Alumnos alumnos=null;
                JSONArray json = response.optJSONArray("tblCurso");
                Log.i("TT","Tamaño del jason : "+json.length());
                try {
                    for(int i=0; i<json.length();i++){
                        alumnos = new Alumnos();
                        JSONObject jsonObject=null;
                        jsonObject = json.getJSONObject(i);
                        alumnos.setPos(i+1);
                        alumnos.setCodAlumno(jsonObject.getString("DNI"));
                        alumnos.setAlumNombres(jsonObject.getString("alumNombres"));
                        alumnos.setAlumApPaterno(jsonObject.getString("alumAPaterno"));
                        alumnos.setAlumApMaterno(jsonObject.getString("alumAPMaterno"));
                        Log.i("TT","Alumno :"+alumnos.toString());
                        lstAlum.add(alumnos);
                    }
                    ArrayAdapter<Alumnos> adapter = new ArrayAdapter<Alumnos>(AsignarNotasAlum.this, android.R.layout.simple_spinner_item, lstAlum);
                    spinAlumnos.setAdapter(adapter);
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

    private void guardarnota() {
        progreso.setMessage("Registrando nota");
        progreso.show();
        String url = UtilDTG.RUTA+"insertarNota.php?" +
                "criterio="+criterio +
                "&valor="+edtNota.getText().toString() +
                "&descrip="+edtDescrip.getText().toString() +
                "&curso="+curso +
                "&dni="+codAlumno;
        url=url.replace(" ","%20");
        Log.i("TT","url NOTA: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                edtNota.setText("");
                edtDescrip.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.i("TT","ERROR AL AGREGAR NOTA ESTUDIANTE: "+error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}