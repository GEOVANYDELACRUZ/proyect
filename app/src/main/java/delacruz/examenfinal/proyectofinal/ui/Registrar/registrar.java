package delacruz.examenfinal.proyectofinal.ui.Registrar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONObject;

import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.ui.IniciarSesion.iniciarsesion;

public class registrar extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    TextInputEditText edtNombre,edtApellidos,edtCorreo,edtContra;
    Button btnRegistrar;
    TextView txtCancelar;


    ProgressDialog progresoReg;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        edtNombre = findViewById(R.id.edtReNombres);
        edtApellidos = findViewById(R.id.edtReApellidos);
        edtCorreo = findViewById(R.id.edtReCorreo);
        edtContra = findViewById(R.id.edtReContrasena);

        txtCancelar = findViewById(R.id.txtReCancelar);

        btnRegistrar = findViewById(R.id.btnReRegistrar);

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAceptar();
            }
        });

        txtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               irIniciarSesion();
            }
        });

    }

    private void irIniciarSesion() {
        Intent i = new Intent(registrar.this,iniciarsesion.class);
        startActivity(i);
    }


    private void dialogAceptar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea esta cuenta?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        registrarnuevo();
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    private void registrarnuevo() {
        progresoReg = new ProgressDialog(this);
        progresoReg.setMessage("Registrando cuenta");
        progresoReg.show();
        String url = UtilDTG.RUTA+"insertarProfesor.php?" +
                "Nombres=" +edtNombre.getText().toString()+
                "&Apellidos=" + edtApellidos.getText().toString()+
                "&Cuenta=" + edtCorreo.getText().toString()+
                "&Pass=" + edtContra.getText().toString();
        url=url.replace(" ","%20");
//        Toast.makeText(this, "Ruta : "+url, Toast.LENGTH_SHORT).show();
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
        Log.i("TT","Ruta"+url);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progresoReg.hide();
        Toast.makeText(this, "La cuenta fue registrada exitosamente", Toast.LENGTH_SHORT).show();
        irIniciarSesion();

        Log.i("TT","Error Ruta"+error);
    }

    @Override
    public void onResponse(JSONObject response) {
        progresoReg.hide();
        Toast.makeText(this, "La cuenta fue registrada exitosamente", Toast.LENGTH_SHORT).show();
        irIniciarSesion();
    }
}