package delacruz.examenfinal.proyectofinal.ui.recuperar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.RandomTextGenerator;
import delacruz.examenfinal.proyectofinal.Util.SendMail;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.databinding.ActivityPasswordBinding;
import delacruz.examenfinal.proyectofinal.ui.IniciarSesion.iniciarsesion;

public class password extends AppCompatActivity {
    private ActivityPasswordBinding binding;

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest, jsonObjectRequest_2 ;

    String mensaje = null;
    String correo_ingresado=null;
    String nuevaClave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        binding.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        binding.btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goHome();
                recID();
            }
        });
    }

    private void recID() {
        correo_ingresado = binding.edtCorreo.getText().toString();
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.seraching));
        progreso.show();
        if (!correo_ingresado.isEmpty()){
            //consultar existencia de correo
            String url = UtilDTG.RUTA+"consultarCorreo.php?correo="+correo_ingresado;
            url=url.replace(" ","%20");
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Integer resultado = 0;
                    JSONArray jsonArray = response.optJSONArray("tblProf");
                    try {
                        for (int i=0;i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            resultado = jsonObject.getInt("idProf");
                        }
                    }catch (Exception e){
                        Toast.makeText(password.this, "Error, onResponse-accespPass: "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Buscar_Usuario(resultado);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(password.this, "Error, onErrorResponse: "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
        else {
            progreso.hide();
            Toast.makeText(this, "Los campos no pueden quedar vacios", Toast.LENGTH_SHORT).show();
        }
    }
    private void Buscar_Usuario(Integer id_Usuario) {
        if (id_Usuario==0){
            mensaje = "No se encontro un usuario relacionado a este correo";
            AlertDialog("ALERTA",mensaje,0);
        }
        else {
            Actualizar_Clave(id_Usuario);
        }
    }
    private void AlertDialog(String titulo, String mensaje, Integer acc){
        progreso.hide();
        AlertDialog.Builder Guardar = new AlertDialog.Builder(password.this);
        Guardar.setTitle(titulo);
        Guardar.setMessage(mensaje);
        Guardar.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(acc==1)
                    goHome();
            }
        });
//        Guardar.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int which){
//                dialog.cancel();
//            }
//        });
        Guardar.show();
    }

    private void Actualizar_Clave(Integer id_usuario){

        nuevaClave = RandomTextGenerator.generateRandomText();
        String url = UtilDTG.RUTA+"actualizarProfesorPass.php?cod="+id_usuario+"&pass="+nuevaClave;
        url=url.replace(" ","%20");
        Log.e("URL: ",url);
        jsonObjectRequest_2=new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Integer respuesta = 0;
                        try {
                            respuesta= response.optInt("msj");
                        }catch (Exception e){
                            Toast.makeText(password.this,
                                    "Error, onResponse-actualizarPass: "+ e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.e("BD: ",String.valueOf(response.optInt("msj")));
                        Log.e("Respuesta: ",respuesta.toString());
                        if(respuesta==1)
                            envCorreo();
                        else
                            AlertDialog("Error","No se pudo actualizar la contraseña",0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(password.this,
                                "Error, onErrorResponse: "+error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest_2);
    }

    private void envCorreo() {
        SendMail sm = new SendMail(this,
                correo_ingresado,
                "Su cuenta fue reestablecida correctamente",
                "Su contraseña para acceder ahora es: "+nuevaClave);
        sm.execute();

        mensaje="Se actualizo correctamente su contraseña";
        AlertDialog("Contraseña restablecida", mensaje,1);
    }

    private void goHome() {
        Intent i = new Intent(password.this, iniciarsesion.class);
        startActivity(i);
    }
}