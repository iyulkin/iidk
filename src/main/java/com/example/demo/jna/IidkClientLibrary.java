package com.example.demo.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface IidkClientLibrary extends Library {
    IidkClientLibrary INSTANCE = Native.load("iidk_client_x64", IidkClientLibrary.class);

    interface ResponceType {
        int ResponceTypeRaw = 0;
        int ResponceTypeJson = 1;
    }

    Integer CreateClient(String id, String ip, Integer port, Pointer userData);

    int RegisterMessageHandler(Integer clientId, int responseType, Callback callback);

    int Connect(Integer clientId);

    int SendMsg(Integer clientId, String msg);

    void Disconnect(Integer clientId);


    void CallbackConnected(Pointer userData);

    void CallbackMessage(Pointer userData, String message);

    int RegisterConnectedHandler(Integer objectPtr, Callback callback);
}
