package com.example.demo.controller;

import com.example.demo.jna.IidkClientLibrary;
import com.example.demo.jna.MessageCallback;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {

    private final IidkClientLibrary iidkClientLibrary = IidkClientLibrary.INSTANCE;
    private final MessageCallback callback = new MessageCallback();
    private Integer clientId;

    @GetMapping("/connect")
    public void connect() {
        Pointer userData = new Memory(1024);

        clientId = iidkClientLibrary.CreateClient("4", "10.8.126.1", 1030, userData);
        System.out.println("1. Client created with id [" + clientId + "]");

        int msgHandlerRegistration = iidkClientLibrary.RegisterMessageHandler(clientId, IidkClientLibrary.ResponceType.ResponceTypeRaw, callback);
        System.out.println("2. Message handler registration result: " + msgHandlerRegistration);

        int connectionRes = iidkClientLibrary.Connect(clientId);
        System.out.println("3. Connection result: " + connectionRes);
    }

    @GetMapping("/message/send")
    public void sendMsg() {
        int messageRes = iidkClientLibrary.SendMsg(clientId, "CORE||GET_CONFIG|objtype<CAM>,objid<1>");
        System.out.println("Message sent to clientId " + clientId);
    }

    @GetMapping("/disconnect")
    public void disconnect() {
        iidkClientLibrary.Disconnect(clientId);
        System.out.println("Disconnected");
    }
}
