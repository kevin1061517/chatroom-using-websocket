package com.kevin.controller;

import com.kevin.server.WebSocketDisplayServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @RequestMapping("/sendPing")
    public ResponseEntity<?> sendPing() {
        WebSocketDisplayServer.sendPing();
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/sendPingTest")
    public ResponseEntity<?> sendPingTest() {
        WebSocketDisplayServer.sendPingTest();
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/closeTest")
    public ResponseEntity<?> closeTest() {
        WebSocketDisplayServer.closeAllClient();
        return ResponseEntity.ok().build();
    }
}
