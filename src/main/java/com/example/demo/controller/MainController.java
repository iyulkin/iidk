package com.example.demo.controller;

import com.example.demo.jna.IidkClientLibrary;
import com.example.demo.jna.MessageCallback;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {

    private final IidkClientLibrary iidkClientLibrary = IidkClientLibrary.INSTANCE;
    private final MessageCallback callback = new MessageCallback();
    private Integer clientId;

    @PostMapping("/connect")
    public ResponseEntity<String> connect() {

        Pointer userData = new Memory(1024);

        clientId = iidkClientLibrary.CreateClient("4", "10.8.126.1", 1030, userData);
        String response = "1. Client created with id [" + clientId + "]";

        int msgHandlerRegistration = iidkClientLibrary.RegisterMessageHandler(clientId, IidkClientLibrary.ResponceType.ResponceTypeRaw, callback);
        response = response + "\n2. Message handler registration result: " + msgHandlerRegistration;

        int connectionRes = iidkClientLibrary.Connect(clientId);
        response = response + "\n3. Connection result: " + connectionRes;

        return ResponseEntity.ok(response);
    }

    @PostMapping("/message/send")
    public ResponseEntity<String> sendMsg(@RequestBody(required = false) String msg) {
        if(Objects.isNull(clientId)) {
            return ResponseEntity.badRequest().body("Not connected. Need to connect.");
        }
        msg = Objects.isNull(msg) ? "CORE||GET_CONFIG|objtype<CAM>,objid<1>" : msg;
        int messageRes = iidkClientLibrary.SendMsg(clientId, msg);
        return ResponseEntity.ok("Message sent to clientId [" + clientId + "] is:\n" + msg);
    }

    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect() {
        if(!Objects.isNull(clientId)) {
            iidkClientLibrary.Disconnect(clientId);
        }
        return ResponseEntity.ok("Disconnected");
    }
}
