## Real-Time Chat Room using WebSocket
Build a real-time chat room application providing a place where you can chat with your friends just like WooTalk.<br>
In shorts, this is a good project that you can learn websocket overall.

### How to close a websocket when client's network suddenly turn offline
1. Using the js offline event handle

   If we would like to detect if the user went offline we simply add websocket close function into offline event function.
    #### front-end
    ``` Javascript
        function closeWebSocket() {
            websocket.close();
        }
        $(window).on('beforeunload offline', event => {
            closeWebSocket();
        });
    ``` 
    #### back-end (WebSocketServer)
    ``` Java
        @OnClose
        public void onClose(Session session) {
            Client client = CURRENT_CLIENTS.get(session.getId());
            log.info("onClose. client name: {}", client.getTeamName());
            CURRENT_CLIENTS.remove(session.getId());
        }
    ```
2. Using Ping interval on the client-side and decrease the websocket session timeout on server-side

   If websocket server doesn't receive any message in specific time, it will lead to timeout. So we can use this mechanism to decrease the timeout to close session if client doesn't send any ping due to offline.
   #### front-end
    ``` Javascript
        // send ping to server every 3 seconds
        const keepAlive = function (timeout = 20000) {
            if (websocket.readyState === websocket.OPEN) {
                websocket.send('ping');
            }
    
            setTimeout(keepAlive, timeout);
        };
    ``` 
    #### back-end (WebSocketConfig)
    ``` Java
        @Bean
        public ServletServerContainerFactoryBean createWebSocketContainer() {
            ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
            container.setMaxSessionIdleTimeout(5000L);
    
            return container;
        }
    ```

## Demo Screenshot(gif)
![image](https://i.imgur.com/4BdjD1V.gif)

## Link
[ChatRoom Demo](https://webforchatroom.herokuapp.com/)

## Reference
* https://blog.csdn.net/qq_35017509/article/details/86243089 
* https://www.callicoder.com/deploy-host-spring-boot-apps-on-heroku/
* https://blog.csdn.net/u013716535/article/details/86180412 
* https://www.cnblogs.com/wqsbk/p/8661141.html
* https://elements.heroku.com/buttons/juliuskrah/spring-profiles
* stackoverflow
  * https://stackoverflow.com/questions/50117284/how-to-check-is-a-websocket-connection-is-alive
  * https://stackoverflow.com/questions/59634335/detect-websocket-loss-of-internet-client-side-fast
  * https://stackoverflow.com/questions/10585355/sending-websocket-ping-pong-frame-from-browser
