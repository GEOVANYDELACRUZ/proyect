package delacruz.examenfinal.proyectofinal.ui.cargarlista;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CargarListaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CargarListaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}