package com.dennismeisner.gimprove.ViewModels;

import android.arch.lifecycle.LiveData;

import com.dennismeisner.gimprove.Models.GimproveModels.User;

public class UserViewModel {

    private LiveData<User> user;

    public LiveData<User> getUser() {
        return user;
    }
}
