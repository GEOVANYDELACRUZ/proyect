package delacruz.examenfinal.proyectofinal.ui.editar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.MainActivity;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.databinding.ActivityDatosBinding;

public class datos extends AppCompatActivity {
    String  cod;
    private ActivityDatosBinding binding;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private static final String ARCHIVO_PREF = "profesor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_datos);
        binding = ActivityDatosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO_PREF,0);
//        private static final String ARCHIVO_PREF = "profesor";
        String nombre = sharedPreferences.getString("nomb","nnn");
        String apellido = sharedPreferences.getString("apell","nnn");
        cod = sharedPreferences.getString("IDProf","nnn");

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        binding.edteditaccname.setHint(nombre);
        binding.edteditacclastname.setHint(apellido);

        binding.buttonEditaccCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        binding.buttonEditaccSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.edteditaccname.getText().toString().isEmpty()
                        ||binding.edteditacclastname.getText().toString().isEmpty())
                    Toast.makeText(datos.this, getString(R.string.error_Null), Toast.LENGTH_SHORT).show();
                else {
                    dialogAcept();
                }
            }
        });
    }

    private void dialogAcept() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.question_ActUser))
                .setPositiveButton(getString(R.string.question_Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        regUser();
                    }})
                .setNegativeButton(getString(R.string.question_No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    private void regUser() {
        progreso = new ProgressDialog(datos.this);
        progreso.setMessage(getString(R.string.load_Register));
        progreso.show();
        String url = UtilDTG.RUTA+"actualizarProfesor.php" +
                "?cod="+cod +
                "&nombres="+binding.edteditaccname.getText().toString()+
                "&apellidos="+binding.edteditacclastname.getText().toString();
        url=url.replace(" ","%20");
        Log.d("Url : ",url.toString());

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                String msj = response.optString("msj");
                if (msj.equals("0")) msj=getString(R.string.error_msj3);
                if (msj.equals("1")){
                    goHome();
                    msj=getString(R.string.msj1);

                    SharedPreferences log = getSharedPreferences(ARCHIVO_PREF,0);
                    SharedPreferences.Editor editor = log.edit();
                    editor.putString("nombreCompleto", binding.edteditacclastname.getText() + ", " + binding.edteditaccname.getText());
                    editor.putString("nomb", binding.edteditaccname.getText().toString());
                    editor.putString("apell", binding.edteditacclastname.getText().toString());
                    editor.commit();
                }

                Toast.makeText(datos.this, msj, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(datos.this, getString(R.string.error_General), Toast.LENGTH_SHORT).show();
                Log.e(" ERROR: ", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void goHome() {
        Intent i = new Intent(datos.this, MainActivity.class);
        startActivity(i);
    }
}