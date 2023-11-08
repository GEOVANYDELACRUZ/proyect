package delacruz.examenfinal.proyectofinal.ui.editar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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
import delacruz.examenfinal.proyectofinal.databinding.ActivityPassBinding;

public class pass extends AppCompatActivity {
    private String cod,aux,aux1;
    private ActivityPassBinding binding;
    private static final String ARCHIVO_PREF = "profesor";

    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_pass);

        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO_PREF,0);
//        private static final String ARCHIVO_PREF = "profesor";
        String nombre = sharedPreferences.getString("nombreCompleto","nnn");
        String correo = sharedPreferences.getString("Correo","nnn");
        cod = sharedPreferences.getString("IDProf","nnn");

        binding.txtChangepassUser.setText(nombre);
        binding.txtChangepassMail.setText(correo);

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        binding.checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.edtchangepass1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.edtchangepass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    binding.edtchangepass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.edtchangepass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        binding.buttonChangepassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        binding.buttonChangepassSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aux = binding.edtchangepass1.getText().toString().replace(" ","");
                aux1 = binding.edtchangepass2.getText().toString().replace(" ","");

                if(aux.isEmpty()||aux1.isEmpty())
                    Toast.makeText(pass.this, getString(R.string.error_Null), Toast.LENGTH_SHORT).show();
                else {
//                    if (verifyPassword(aux)) {
                    if (aux.equals(aux1)) {
                        dialogAcept();
                    } else
                        Toast.makeText(pass.this, getString(R.string.error_Pass), Toast.LENGTH_SHORT).show();
//                    } else {
//                        binding.txtSegPass.setVisibility(View.VISIBLE);
//                        Toast.makeText(changePassword.this, getString(R.string.error_segPass), Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }

    private void goHome() {
        Intent i = new Intent(pass.this, MainActivity.class);
        startActivity(i);
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
        progreso = new ProgressDialog(pass.this);
        progreso.setMessage(getString(R.string.load_Register));
        progreso.show();
        String url = UtilDTG.RUTA+"actualizarProfesorPass.php" +
                "?cod=" +cod+
                "&pass=" + aux;
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
                }

                Toast.makeText(pass.this, msj, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(pass.this, getString(R.string.error_General), Toast.LENGTH_SHORT).show();
                Log.e(" ERROR: ", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}