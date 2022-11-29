package com.kevin.controller;

import com.kevin.server.WebSocketDisplayServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/websocket/push/{teamName}")
    public ResponseEntity<?> pushToSpecificTeam(@PathVariable String teamName, @RequestParam String message) {
        WebSocketDisplayServer.sendMsgToTeam(teamName, message);
        Map<String, String> map = new HashMap<>();
        map.put("teamName", teamName);
        map.put("message", message);

        return ResponseEntity.ok(map);
    }

    @RequestMapping("/test")
    public ResponseEntity<?> test() {


        return ResponseEntity.ok().build();
    }
}
