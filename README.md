# reversi

reversi game application.
support remote game through websocket.


request
   * MyWebSocketHandler#handleTextMessage
     * TextSocketMessageParser#parse
     * UserMessageProcessor#delegate
       * UserMessageHandler#handleMessage
   * MyWebSocketHandler#sendResponseMessage
