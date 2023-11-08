package delacruz.examenfinal.proyectofinal.ui.configuraciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import delacruz.examenfinal.proyectofinal.R;
import delacruz.examenfinal.proyectofinal.databinding.ConfiguracionesFragmentBinding;
import delacruz.examenfinal.proyectofinal.ui.editar.datos;
import delacruz.examenfinal.proyectofinal.ui.editar.pass;

public class Configuraciones extends Fragment {

    private ConfiguracionesViewModel mViewModel;
    private ConfiguracionesFragmentBinding binding;
//    private Binding

    public static Configuraciones newInstance() {
        return new Configuraciones();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ConfiguracionesFragmentBinding.inflate(inflater,container,false);
        binding.RlyEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goDatos();
            }
        });
        binding.RlyChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPass();
            }
        });
        return binding.getRoot();
//        return inflater.inflate(R.layout.configuraciones_fragment, container, false);
    }

    private void goPass() {
        Intent i = new Intent(getContext(), pass.class);
        startActivity(i);
    }

    private void goDatos() {
        Intent i = new Intent(getContext(), datos.class);
        startActivity(i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConfiguracionesViewModel.class);
        // TODO: Use the ViewModel
    }

}