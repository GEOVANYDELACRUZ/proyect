package delacruz.examenfinal.proyectofinal.ui.consolidado;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsolidadoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConsolidadoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}