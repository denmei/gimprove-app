package com.example.dennismeisner.gimprove.ViewModels;

import android.arch.lifecycle.LiveData;

import com.example.dennismeisner.gimprove.GimproveModels.User;

public class UserViewModel {

    private LiveData<User> user;

    public LiveData<User> getUser() {
        return user;
    }
}
