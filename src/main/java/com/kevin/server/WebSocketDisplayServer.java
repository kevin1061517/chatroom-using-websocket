package com.kevin.server;

import com.kevin.config.WebSocketServerConfigurator;
import com.kevin.pojo.Client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@ServerEndpoint(value = "/api/websocket/{teamName}", configurator = WebSocketServerConfigurator.class)
public class WebSocketDisplayServer {
    private static final Map<String, Client> CURRENT_CLIENTS = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    @OnOpen
    public void onOpen(Session session, @PathParam("teamName") String teamName) {
        HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
        System.out.println("onOpen#" + httpSession.getAttribute("ClientIP"));

        Client client = new Client(teamName, "123", session);
        CURRENT_CLIENTS.put(session.getId(), client);

        CURRENT_CLIENTS.values().forEach(eachClient -> {
            Session eachSession = eachClient.getSession();
            if(eachSession.isOpen()) {
                try {
                    eachSession.getBasicRemote().sendText("Welcome: " + teamName);
                    executorService.scheduleAtFixedRate(() -> {
                        try {
                            String data = "Ping";
                            ByteBuffer payload = ByteBuffer.wrap(data.getBytes());
                            eachSession.getBasicRemote().sendPing(payload);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    },
                    5, 1500000, TimeUnit.SECONDS);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Client client = CURRENT_CLIENTS.get(session.getId());
        sendMsgToAll(message, session.getId());

        System.out.println(message);
    }

    @OnMessage
    public void pongMessage(Session session, PongMessage pongMessage) {
        log.info("Pong message received: " + Instant.now());
        log.info(pongMessage.toString());
    }

    @OnClose
    public void onClose(Session session) {
        Client client = CURRENT_CLIENTS.get(session.getId());
        log.info("onClose. client name: {}", client.getTeamName());
        CURRENT_CLIENTS.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        CURRENT_CLIENTS.remove(session.getId());
    }

    private synchronized static List<Client> getAllClients() {
//        return CURRENT_CLIENTS.values().stream().sorted((a, b) -> {
//            return a.getSession().getId().compareTo(b.getSession().getId());
//        }).collect(Collectors.toList());
        return CURRENT_CLIENTS.values()
                .stream()
                .sorted(Comparator.comparing(a -> a.getSession().getId()))
                .collect(Collectors.toList());
    }

    public synchronized static void sendMsgToAll(String message) {
        sendMsgToAll(message, null);
    }

    public synchronized static void sendMsgToAll(String message, String excludeSessionId) {
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                if(!StringUtils.equals(id, excludeSessionId)) {
                    client.getSession().getBasicRemote().sendText(message);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized static void sendPing() {
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                Session session = client.getSession();
                if(session.isOpen()) {
                    String data = "Ping";
                    ByteBuffer payload = ByteBuffer.wrap(data.getBytes());
                    client.getSession().getBasicRemote().sendPing(payload);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized static void sendPingTest() {
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                String data = "Ping";
                ByteBuffer payload = ByteBuffer.wrap(data.getBytes());
                client.getSession().getBasicRemote().sendPing(payload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized static void closeAllClient() {
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                client.getSession().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized static void sendMsgToTeam(String teamName, String message) {
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                String currentTeamName = client.getTeamName();
                if(currentTeamName.equals(teamName)) {
                    client.getSession().getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
