package com.example.demo.jna;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class MessageCallback implements Callback {

    public void callback(Pointer userData, String message) {
        System.out.println(message);
    }
}