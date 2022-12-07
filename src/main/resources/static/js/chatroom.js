$(document).ready(function () {
    let websocket = null;
    function connect() {
        if('WebSocket' in window) {
            websocket = new WebSocket("ws://" + window.location.host + "/api/websocket/TPA");
        } else {
            alert('Web browser not support websocket');
            return;
        }

        websocket.onerror = function() {
            setMessageToOtherSide("WebSocket connection error");
        };

        websocket.onopen = function() {
            setMessageToOtherSide("WebSocket connection successfully");
        };

        websocket.onmessage = function(event) {
            console.log(event.data);
            setMessageToOtherSide(event.data);
        };

        websocket.onclose = function() {
            setMessageToOtherSide("WebSocket connection close");
        };
    }
    connect();



    window.onbeforeunload = function() {
        closeWebSocket();
    }

    function closeWebSocket() {
        connect();
        websocket.close();
    }

    function sendMsg() {
        const message = $('#text-input').val();
        websocket.send('{"msg":"' + message + '"}');
        setMessageToSelfSide(message);
    }

    function setMessageToOtherSide(message) {
        $('.body').append(`
                <div class="incoming">
                    <div class="bubble">
                        <p>${message}</p>
                    </div>
                </div>`);
    }

    function setMessageToSelfSide(message) {
        $('.body').append(`
                <div class="outgoing">
                    <div class="bubble lower">
                        <p>${message}</p>
                    </div>
                </div>`);
    }

    $('#send-msg-btn').click(function() {
        console.log('click');
        sendMsg();
    });

    const checkConnection = function () {
        console.log(websocket.readyState);
        if(websocket.readyState !== 1) {
            connect();
        }
    };
    setInterval(checkConnection, 2000);



});