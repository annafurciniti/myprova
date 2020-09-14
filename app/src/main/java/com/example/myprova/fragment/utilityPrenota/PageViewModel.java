package com.example.myprova.fragment.utilityPrenota;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            switch (input) {
                case 1:
                    return "SCEGLIERE UNO DEI CORSI DISPONIBILI:";
                case 2:
                    return "NON E' STATO SELEZIONATO ALCUN CORSO!";
                default:
                    return "NON E' STATO SELEZIONATO ALCUN DOCENTE!";
            }
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}