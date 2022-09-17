package com.freeislamicapps.athantime.ui.qiblah;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.freeislamicapps.athantime.R;

public class QiblahViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;

    public QiblahViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue(application.getApplicationContext().getString(R.string.title_qiblah));
    }

    public LiveData<String> getText() {
        return mText;
    }
}