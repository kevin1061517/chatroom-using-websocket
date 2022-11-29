package com.kevin.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class WebSocketUtils {
    public static String getRemoteIpAddr() {
        String ipAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

        return ipAddr;
    }
}
