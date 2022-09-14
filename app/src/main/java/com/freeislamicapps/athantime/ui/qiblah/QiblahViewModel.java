package com.freeislamicapps.athantime.ui.qiblah;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QiblahViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QiblahViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Coming Soon");
    }

    public LiveData<String> getText() {
        return mText;
    }
}