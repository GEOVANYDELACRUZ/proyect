package delacruz.examenfinal.proyectofinal.ui.cargarlista;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import delacruz.examenfinal.proyectofinal.Entidad.Alumnos;
import delacruz.examenfinal.proyectofinal.Instancia.VolleySingleton;
import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.Util.UtilDTG;
import delacruz.examenfinal.proyectofinal.databinding.FragmentCargarlistaBinding;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;


public class CargarLista extends Fragment {

    private static final String TAG = "FragmentCargarLista";


    MaterialButton btnCargarLis,btnGuardarLista;
    ListView lstAlumnos;
    TextInputEditText edtCurso;
    //Apoyo
    LinearLayout lyt1;
    String codigoProf;


    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    ArrayList<String> pathHistory;
    int count = 0;
    String lastDirectory;
    ArrayList<Alumnos> alumnosArrayList;


    ProgressDialog progreso;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;


    private static final String ARCHIVO_PREF = "profesor";

    private CargarListaViewModel cargarListaViewModel;
    private FragmentCargarlistaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCargarlistaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lstAlumnos = root.findViewById(R.id.listListaAlumnos);
        edtCurso = root.findViewById(R.id.edtCurso);
        btnCargarLis = root.findViewById(R.id.btnCargarLista);
        btnGuardarLista = root.findViewById(R.id.btnGuardarLista);

        //BLOQUE SHAREDPREFERENCES "DATOS DE PROFESOR"
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ARCHIVO_PREF,0);
//        private static final String ARCHIVO_PREF = "profesor";
        String nombre = sharedPreferences.getString("nombreCompleto","nnn");
        codigoProf = sharedPreferences.getString("IDProf","nnn");
        Toast.makeText(getContext(), "Bienvenido Profesor :"+nombre, Toast.LENGTH_SHORT).show();

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();
        //Apoyo
        lyt1 = root.findViewById(R.id.lytAsignarCurso);

        btnCargarLis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (lyt1.getVisibility() == View.GONE){
                    lyt1.setVisibility(View.VISIBLE);
                    verNotas();
                }
                ImportarLista();
                alumnosArrayList.clear();
                lstAlumnos.setAdapter(new ArrayAdapter<Alumnos>(getContext(), android.R.layout.simple_list_item_1, alumnosArrayList));

            }
        });

        btnGuardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder Guardar = new AlertDialog.Builder(getContext());
                Guardar.setTitle("GUARDAR LISTA DE ALUMNOS");
                Guardar.setMessage("¿Desea guardar la lista de alumnos con el curso asignado?");
                Guardar.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        guardarlista();
                        lyt1.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
                Guardar.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                Guardar.show();
            }
        });





        return root;
    }

    private void verNotas() {

    }


    private void ImportarLista() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.directorio_xls);
        ListView lvInternalStorage = (ListView) dialog.findViewById(R.id.lvInternalStorage);
        Button btnUpDirectory = (Button) dialog.findViewById(R.id.btnUpDirectory);
        Button btnSDCard = (Button) dialog.findViewById(R.id.btnViewSDCard);
        Button btnReturn = (Button) dialog.findViewById(R.id.btnReturn);
        alumnosArrayList = new ArrayList<>();
        if(!checkPermission())
            getPermis();
//        checkFilePermissions();
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                }else{
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage(dialog);
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });
        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage(dialog);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, FilePathStrings);
                lvInternalStorage.setAdapter(adapter);
            }
        });
        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.
                    readExcelData(lastDirectory, dialog);

                }else
                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage(dialog);
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });



        dialog.show();




    }

    private void checkInternalStorage(Dialog dialog) {

        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }
//            file = new File(pathHistory.get(count));
            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++)
            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }
//            Toast.makeText(getContext(), "Los datos fueron cargados\nPuede cerrar esta pantalla", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();


        }catch(NullPointerException e){
            Toast.makeText(getContext(), "Seleccione otra vez", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }

    private void readExcelData(String filePath,Dialog dialog) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>4){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
//                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
//                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
//            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb,rowsCount,dialog);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    public void parseStringBuilder(StringBuilder mStringBuilder,int aux, Dialog dialog){
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
//                for (int r = 0; r < aux; r++) {
                    String x = (columns[0]);
                    String y = (columns[1]);
                    String a = (columns[2]);
                    String b = (columns[3]);
//                    String cellInfo = "(x,y): (" + x + "," + y + "," + a + "," + b  + ")";
//                    Log.d(TAG, "ParseStringBuilder: Data from row PRUEBA: " + cellInfo);

                    //add the the uploadData ArrayList
//                    Dialog dialog
                    alumnosArrayList.add(new Alumnos(i+1,x, a, b,y));

//                }

                lstAlumnos.setAdapter(new ArrayAdapter<Alumnos>(getContext(), android.R.layout.simple_list_item_1, alumnosArrayList));
                dialog.dismiss();
//                Toast.makeText(getContext(), "Los alumnos se estan cargando...", Toast.LENGTH_SHORT).show();


            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }finally {

            }
        }

        printDataToLog();
    }

    private void printDataToLog() {
        Log.d(TAG, "printDataToLog: Printing data to log...");

        for(int i = 0; i< alumnosArrayList.size(); i++){
            String x = alumnosArrayList.get(i).getCodAlumno();
            String y = alumnosArrayList.get(i).getAlumNombres();
            String a = alumnosArrayList.get(i).getAlumApPaterno();
            String b = alumnosArrayList.get(i).getAlumApMaterno();
//            Log.d(TAG, "printDataToLog: (x,y): (" + x + "," + y + "," + a + "," + b+")");
//            Log.i("TT","Ingreso ALUMNO - " + i );
            registrarAlumno(x,y,a,b);
        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    Integer anInt = (int)numericValue;
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+anInt;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = getContext().checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += getContext().checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private void getPermis() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            //nuevas normas de seg. aumentado
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                Toast.makeText(getContext(), "Accediendo a permisos especiales", Toast.LENGTH_SHORT).show();
//                Intent PRUEBA = new Intent(Settings.ACTION_NIGHT_DISPLAY_SETTINGS); recuerda ctv
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getContext().getPackageName()})));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2000);

            }

        } else
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 333);
    }
    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void toastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }

//metodos funcionales
    private void guardarlista() {
        String curso = edtCurso.getText().toString();
        registrarCurso(curso);
        for(int i = 0; i< alumnosArrayList.size(); i++){
            String x = alumnosArrayList.get(i).getCodAlumno();
            for(int j=0;j<4;j++)
            registrarAlumnoCurso(x,curso);  //asignar un curso a un alumno
        }
    }

    private void registrarCurso(String curso) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Asignando curso a alumno ...");
        progreso.show();
        String url = UtilDTG.RUTA+"insertarCurso.php?" +
                "curso="+curso +
                "&prof="+ codigoProf;
        url=url.replace(" ","%20");
        Log.i("TT","url PROFESOR - CURSO: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Toast.makeText(getContext(), "Los alumnos fueron asignados a su curso", Toast.LENGTH_SHORT).show();
                Log.i("TT","INGRESO CORRECTO DE PROFESOR/CURSO: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.i("TT","ERROR AL INGRESAR PROFESOR/CURSO: "+error);

            }
        });
        progreso.hide();
        requestQueue.add(jsonObjectRequest);
    }
    private void registrarAlumnoCurso(String x, String curso) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Asignando curso a alumno ...");
        progreso.show();
        String url = UtilDTG.RUTA+"insertarCursoAlumno.php?"+
                "curso="+curso +
                "&alumno="+x+
                "&profesor="+codigoProf;
        url=url.replace(" ","%20");
        Log.i("TT","url ALUMNO - CURSO: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Toast.makeText(getContext(), "Los alumnos fueron asignados a su curso", Toast.LENGTH_SHORT).show();
                Log.i("TT","INGRESO CORRECTO DE ALUMNO/CURSO: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.e("TT","ERROR AL INGRESAR ALUMNO/CURSO "+x+": "+error);

            }
        });
        progreso.hide();
        requestQueue.add(jsonObjectRequest);
    }

    private void registrarAlumno(String x, String y, String a, String b) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Registering account ...");
        progreso.show();
        String url = UtilDTG.RUTA+"insertarAlumno.php?" +
                "dni=" +x+
                "&nombres=" +y+
                "&apaterno=" +a+
                "&amaterno="+b;
        url=url.replace(" ","%20");
        Log.i("TT","url ALUMNO: "+url);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progreso.hide();
                Toast.makeText(getContext(), "El alumno fue cargados", Toast.LENGTH_SHORT).show();
                Log.i("TI","INGRESO CORRECTO : "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.e("TT","ERROR AL INGRESAR: "+error);

            }
        });
        progreso.hide();
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}