package delacruz.examenfinal.alumnoapp.ui.VisualizarNotas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class visualizarnotaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public visualizarnotaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}