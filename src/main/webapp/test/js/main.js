function TestClient() {
  var websocket;
  var that = this;
  var respondToPing = true;

  this.log = function(data) { console.log(data); }
  this.error = function(data) { console.error(data); }

  this.onOpen = function(event) { that.log("Connected!"); }
  this.onClose = function(event) { that.log("Disconnected!"); }
  this.onMessage = function(event) {
    that.log("Received: " + event.data);

    if (respondToPing) {
      var packet = JSON.parse(event.data);

      if (packet.type == "PING") {
        that.pong();
      }
    }
  }
  this.onError = function(event) { that.error("Error with websocket: " + event.data); }

  this.connect = function(url) {
    websocket = new WebSocket(url);

    websocket.onopen = function(event) { that.onOpen(event); }
    websocket.onclose = function(event) { that.onClose(event); }
    websocket.onmessage = function(event) { that.onMessage(event); }
    websocket.onerror = function(event) { that.onError(event); }
  }

  this.disconnect = function() {
    websocket.close();
  }

  function send(object) {
    var toSend = JSON.stringify(object);

    websocket.send(toSend);
    that.log("Sent: " + toSend);
  }

  this.setRespondToPing = function(value) {
    respondToPing = value;
  }

  this.register = function(nickname, email, password) {
    send({
      type: "REGISTER",
      nickname: nickname,
      password: password,
      email: email
    });
  }

  this.login = function(email, password) {
    send({
      type: "LOGIN",
      email: email,
      password: password
    });
  }

  this.createGame = function(gameType, name, info) {
    send({
      type: "CREATE_GAME",
      gameType: gameType,
      slots: 5,
      info: info,
      name: name
    });
  }

  this.joinGame = function(name) {
    send({
      type: "JOIN_GAME",
      gameName: name
    });
  }

  this.bufferedMessage = function(payload) {
    send({
      type: "BUFFERED_MESSAGE",
      timestamp: 42,
      payload: payload
    });
  }

  this.clientMessage = function(contentType, payload) {
    send({
      type: "CLIENT_MESSAGE",
      contentType: contentType,
      timestamp: 42,
      payload: payload
    });
  }

  this.ping = function() {
    send({
      type: "PING"
    });
  }

  this.pong = function() {
    send({
      type: "PONG"
    });
  }

  this.leaveGame = function() {
    send({
      type: "LEAVE_GAME"
    });
  }

  this.closeGame = function() {
    send({
      type: "CLOSE_GAME"
    });
  }

  this.startGame = function() {
    send({
      type: "START_GAME"
    });
  }

  this.setPlayerStorage = function(game, content) {
    send({
      type: "SET_PLAYER_STORAGE",
      game: game,
      content: content
    });
  }

  this.getPlayerStorage = function(game) {
    send({
      type: "GET_PLAYER_STORAGE",
      game: game
    });
  }

  this.logMessage = function(level, message) {
    send({
      type: "LOG",
      level: level,
      message: message
    });
  }

  this.setMapStorage = function(game, map, content) {
    send({
      type: "SET_MAP_STORAGE",
      game: game,
      map: map,
      content: content
    });
  }

  this.getMapStorage = function(game, map) {
    send({
      type: "GET_MAP_STORAGE",
      game: game,
      map: map
    });
  }

  this.verifyEmail = function(email, token) {
    send({
      type: "VERIFY_EMAIL",
      email: email,
      token: token
    });
  }

  this.malformedPacket = function() {
    send({
      type: "MALFORMED"
    });
  }

  this.resetPassword = function(email, nickname) {
    send({
      type: "RESET_PASSWORD",
      email: email,
      nickname: nickname
    });
  }

  this.setNewPassword = function(email, token, password) {
    send({
      type: "SET_NEW_PASSWORD",
      email: email,
      token: token,
      password: password
    });
  }

  this.sendChatMessage = function(message) {
    send({
      type: "CHAT_MESSAGE",
      message: message
    });
  }

  this.getTakers = function() {
    send({
      type: "GET_TAKERS"
    });
  }

  this.loadingComplete = function() {
    send({
      type: "LOADING_COMPLETE"
    });
  }

  this.unregister = function(email, password) {
    send({
      type: "UNREGISTER",
      email: email,
      password: password
    });
  }
}

