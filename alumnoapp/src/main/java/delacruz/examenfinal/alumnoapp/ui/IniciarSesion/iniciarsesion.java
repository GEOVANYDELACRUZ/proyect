package delacruz.examenfinal.alumnoapp.ui.IniciarSesion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import delacruz.examenfinal.alumnoapp.Entidades.Alumnos;
import delacruz.examenfinal.alumnoapp.Instancia.VolleySingleton;
import delacruz.examenfinal.alumnoapp.MainActivity;
import delacruz.examenfinal.alumnoapp.R;
import delacruz.examenfinal.alumnoapp.Util.UtilDTG;

public class iniciarsesion extends AppCompatActivity {
    private static final String ARCHIVO_PREF = "alumno";

    TextInputEditText edtUsuario,edtContra;
    Button btnISesion;

    ArrayList<Alumnos> lstAlumnos;

    ProgressDialog progresIS;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);

        edtUsuario = findViewById(R.id.edtISUsuario);
        edtContra = findViewById(R.id.edtISContrasena);

        btnISesion = findViewById(R.id.btnISIniciarSesion);
        lstAlumnos = new ArrayList<>();
        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();


        btnISesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
//                Intent i = new Intent(iniciarsesion.this, MainActivity.class);
//                startActivity(i);
            }
        });

    }

    private void validarDatos() {
        progresIS = new ProgressDialog(this);
        progresIS.setMessage("Iniciando Sesión");
        progresIS.show();
        String url = UtilDTG.RUTA+"iniciarSesionAlumnos.php?"+
                "dni=" + edtUsuario.getText().toString()+
                "&pass=" + edtContra.getText().toString().replace(" ","");
        url = url.replace(" ","");
        Log.e("URL: ",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progresIS.hide();
                Alumnos alumnos = null;
                JSONArray jsonArray = response.optJSONArray("tblAlumno");
                Integer auxID = -1;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        alumnos = new Alumnos();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);
                        auxID = jsonObject.getInt("DNI");
                        alumnos.setCodAlumno(auxID.toString());
                        alumnos.setAlumApPaterno(jsonObject.getString("alumAPaterno"));
                        alumnos.setAlumApPaterno(jsonObject.getString("alumAPMaterno"));
                        alumnos.setAlumNombres(jsonObject.getString("alumNombres"));
                        lstAlumnos.add(alumnos);
//                      Guardar valores en el SHARED PREFERENCES
//                        private static final String ARCHIVO_PREF = "alumno";
                        SharedPreferences preferences = getSharedPreferences(ARCHIVO_PREF, 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nombreCompleto", alumnos.getAlumApPaterno() + " " + alumnos.getAlumApMaterno() +
                                " " + alumnos.getAlumNombres());
                        editor.putString("DNI", alumnos.getCodAlumno());
                        editor.commit();
                    }


                    if (auxID == 0) {
                        Toast.makeText(iniciarsesion.this, "Esta cuenta no existe", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(iniciarsesion.this, "Cuenta verificada", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(iniciarsesion.this, MainActivity.class);
                        startActivity(i);
                    }

                } catch (Exception exception) {
                    Log.e("ErrorIS"," "+exception);
                    Toast.makeText(iniciarsesion.this, "Ingreso al catch\nError: " + exception, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              progresIS.hide();
                Log.i("TT","Error en CONSULTA: "+error);
                Toast.makeText(iniciarsesion.this, "Error, imposible conectarse a la BD"+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}