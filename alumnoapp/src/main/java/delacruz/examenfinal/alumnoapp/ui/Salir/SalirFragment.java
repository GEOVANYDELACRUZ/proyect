package delacruz.examenfinal.alumnoapp.ui.Salir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import delacruz.examenfinal.alumnoapp.databinding.FragmentSalirBinding;
import delacruz.examenfinal.alumnoapp.ui.IniciarSesion.iniciarsesion;

public class SalirFragment extends Fragment {

    private SalirViewModel slideshowViewModel;
    private FragmentSalirBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        slideshowViewModel =
//                new ViewModelProvider(this).get(SalirViewModel.class);
//
//        binding = FragmentSalirBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        Intent i = new Intent(getContext(),iniciarsesion.class);
        startActivity(i);

//        return root;
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}