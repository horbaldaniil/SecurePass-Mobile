package com.example.securepass.ui.folders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoldersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FoldersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is folders fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}