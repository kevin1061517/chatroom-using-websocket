package com.kevin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String teamName;
    private String ipAddress;
    private Session session;
    private boolean isAlive;

    public Client(String teamName, String ipAddress, Session session) {
        this.teamName = teamName;
        this.ipAddress = ipAddress;
        this.session = session;
        this.isAlive = true;
    }
}
