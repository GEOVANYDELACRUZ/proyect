package delacruz.examenfinal.proyectofinal.ui.asignarnota;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AsignarNotasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AsignarNotasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}