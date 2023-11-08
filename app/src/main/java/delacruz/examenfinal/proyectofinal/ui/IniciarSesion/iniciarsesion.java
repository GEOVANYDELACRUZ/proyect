package delacruz.examenfinal.proyectofinal.ui.IniciarSesion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import delacruz.examenfinal.proyectofinal.Entidad.Profesor;
import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.MainActivity;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.ui.Registrar.registrar;
import delacruz.examenfinal.proyectofinal.ui.recuperar.password;

public class iniciarsesion extends AppCompatActivity {
    private static final String ARCHIVO_PREF = "profesor";
    TextInputEditText edtCorreo,edtContraseña;
    Button btnIniSesion;
    TextView Registro,Pass;
    ArrayList<Profesor> lstProf;


    ProgressDialog progresoIS;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);

        edtCorreo = findViewById(R.id.edtISCorreo);
        edtContraseña = findViewById(R.id.edtISContraseña);
        btnIniSesion = findViewById(R.id.btnISIniciarsesion);
        Registro = findViewById(R.id.txtClicRegistro);
        Pass = findViewById(R.id.txtClicPass);
        lstProf = new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();



        btnIniSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarIS();

            }
        });
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(iniciarsesion.this,registrar.class);
                startActivity(i);
            }
        });
        Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(iniciarsesion.this, password.class);
                startActivity(i);
            }
        });
    }
//Procedimientos
    private void validarIS() {
        progresoIS = new ProgressDialog(this);
        progresoIS.setMessage("Cargando");
        progresoIS.show();
        String url = UtilDTG.RUTA+"iniciarSesion.php?" +
                "correo=" +edtCorreo.getText().toString().replace(" ","")+
                "&contra="+edtContraseña.getText().toString().replace(" ","");
        url=url.replace(" ","");
        Log.i("TT","ruta: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progresoIS.hide();
                Profesor profesor = null;
                JSONArray jsonArray = response.optJSONArray("tblProfesor");
                Integer auxID = -1; //contador
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        profesor = new Profesor();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);
                        auxID = jsonObject.getInt("IDProfesor");
                        profesor.setIDProfesor(Integer.valueOf(auxID.toString()));
                        profesor.setProfNombre(jsonObject.getString("profNombre"));
                        profesor.setProfApellidos(jsonObject.getString("profApellidos"));
                        profesor.setProfCorreo(jsonObject.getString("profCorreo"));
                        profesor.setProfContra(jsonObject.getString("profContra"));
                        lstProf.add(profesor);
//                      Guardar valores en el SHARED PREFERENCES
//                private static final String ARCHIVO_PREF = "profesor";
                        SharedPreferences preferences = getSharedPreferences(ARCHIVO_PREF, 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nombreCompleto", profesor.getProfApellidos() + ", " + profesor.getProfNombre());
                        editor.putString("nomb", profesor.getProfNombre());
                        editor.putString("apell", profesor.getProfApellidos());
                        editor.putString("IDProf", String.valueOf(profesor.getIDProfesor()));
                        editor.putString("Correo", profesor.getProfCorreo());
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
                    Toast.makeText(iniciarsesion.this, "Ingreso al catch\nError: " + exception, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progresoIS.hide();
                Log.i("TT","Error en CONSULTA: "+error);
                //Toast.makeText(iniciarsesion.this, "Error, imposible conectarse a la BD"+error, Toast.LENGTH_SHORT).show();
                Toast.makeText(iniciarsesion.this, "Error, imposible conectarse a la BD\n"+ "Credenciales no existentes" , Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}