package com.example.library.service;

import com.example.library.repository.Repository;

public class UserService {

    private final Repository systemData;

    public UserService(Repository systemData) {
        this.systemData = systemData;
    }


}
