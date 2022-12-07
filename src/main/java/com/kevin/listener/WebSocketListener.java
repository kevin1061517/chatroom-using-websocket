package com.kevin.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class WebSocketListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        System.out.println("requestInitialized session = " + session);
        session.setAttribute("ClientIP", request.getRemoteAddr());//把HttpServletRequest中的IP地址放入HttpSession中，关键字可任取，此处为ClientIP
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("requestDestroyed sre = " + sre);
    }
}
