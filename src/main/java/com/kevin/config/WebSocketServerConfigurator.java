package com.kevin.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

public class WebSocketServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        System.out.println("modifyHandshake");
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            Map<String, Object> userProperties = sec.getUserProperties();
            userProperties.put(HttpSession.class.getName(), httpSession);
        }
    }
}
