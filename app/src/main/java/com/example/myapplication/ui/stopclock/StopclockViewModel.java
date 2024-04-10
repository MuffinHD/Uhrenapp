package com.example.myapplication.ui.stopclock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StopclockViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StopclockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is stopclock fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}