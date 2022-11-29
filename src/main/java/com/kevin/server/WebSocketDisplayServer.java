package com.kevin.server;

import com.kevin.pojo.Client;
import com.kevin.utils.WebSocketUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ServerEndpoint(value = "/websocket/{teamName}")
public class WebSocketDisplayServer {
    private static final Map<String, Client> CURRENT_CLIENTS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("teamName") String teamName) {
        String remoteIpAddr = WebSocketUtils.getRemoteIpAddr();
        Client client = new Client(teamName, remoteIpAddr, session);
        CURRENT_CLIENTS.put(session.getId(), client);
    }

    @OnMessage
    public void onMessage(Session session, String json) {
        Client client = CURRENT_CLIENTS.get(session.getId());
        System.out.println(json);
    }

    @OnClose
    public void onClose(Session session) {
        Client client = CURRENT_CLIENTS.get(session.getId());
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
        CURRENT_CLIENTS.forEach((id, client) -> {
            try {
                client.getSession().getBasicRemote().sendText(message);
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
