# Client to server
## Register client
Registers a client with the server.

    {"type":"REGISTER", "nickname":"player1", "password":"player1", "email":"player1@hm.edu"}

## Login client
Logs in a client.

    {"type":"LOGIN", "password":"player1", "email":"player1@hm.edu"}

## Get all players in the lobby
Lists all the players in the lobby.

    {"type":"GET_LOBBY_PLAYERS"}

## Create game
Creates a new game. `info` is a JSON object to store additional informations like the map name etc.

    {"type":"CREATE_GAME","name":"blafoo","gameType":"TOY_RACER","slots":20,"info":{"mapName":"Liberty City"}}

## Get all open games
Lists all open games.

    {"type":"GET_OPEN_GAMES"}

## Join game
Joins a game.

    {"type":"JOIN_GAME","gameName":"Game #1"}

## Send generic message to takers
Sends a generic message to the takers or to the lobby players. `payload` can be any valid JSON document.

    {"type":"CLIENT_MESSAGE","timestamp":1,"payload":true,"contentType":"GAME_DATA"}

## Change ready state
Changes the ready state of the client.

    {"type":"CHANGE_READY,"ready":true"}

## Log
Client sends a log entry to the server. A higher level means a more severe log entry.

    {"type":"LOG","level":1,"message":"Log Message"}

## Start Game
Game host starts the game.

    {"type":"START_GAME"}

## Buffered message
A buffered message is a message which is buffered on the server side and sent periodically to all the takers. If a new buffered message is received, it overwrites the old one. `payload` can be any valid JSON document.

    {"type":"BUFFERED_MESSAGE","timestamp":42,"payload":true}

## Ping
Pings the server. Server responds with a Pong.

    {"type":"PING"}

## Pong
Response from a client to a server ping.

    {"type":"PONG"}

## Leave game
Client leaves current game.

    {"type":"LEAVE_GAME"}

## Close game
Client marks the game as closed (no one can join any more).

    {"type":"CLOSE_GAME"}

## Set player storage
Sets the player storage. `content` can be any valid JSON object.

    {"type":"SET_PLAYER_STORAGE","game": "TOY_RACER","content":true}

## Get player storage
Gets the player storage.

    {"type":"GET_PLAYER_STORAGE","game": "TOY_RACER"}

## Set map storage
Sets the map storage. `content` can be any valid JSON object.

    {"type":"SET_MAP_STORAGE","game":"TOY_RACER","map":"Vice City","content":true}

## Get map storage
Gets the map storage.

    {"type":"GET_MAP_STORAGE","game":"TOY_RACER","map":"Vice City"}

## Verify email address
Verifies an email address. Token is the token sent to the email address of the user.

    {"type":"VERIFY_EMAIL","email":"player1@hm.edu","token":"12345"}

## Reset password
Resets a password. Server sends a token to the given email address, which can be used to set a new password.

    {"type":"RESET_PASSWORD","email":"player1@hm.edu","nickname":"Player 1"}

## Set a new password
Sets a new password. `token` is the token from the email.

    {"type":"SET_NEW_PASSWORD","email":"player1@hm.edu","token":"12345","password":"new-password"}

## Sends a chat message.
Sends a chat message. This message is delivered to the players in the lobby, or, if the client is in a game, to the takers.

    {"type":"CHAT_MESSAGE","message":"hello"}

## Get takers
Gets the takers of the current game.

    {"type":"GET_TAKERS"}

## Loading complete.
Notifies the server that the client has finished loading.

    {"type":"LOADING_COMPLETE"}

## Unregister (delete) an account.
Unregister from the server.

    {"type":"UNREGISTER","email":"foo@foo.bar","password":"password"}

# Server to client
## Register failed
Client registration failed. `reason` contains the machine readable failure reason, `message` can be displayed to the user.

    {"type":"REGISTER_FAILED","reason":"NICKNAME_ALREADY_IN_USE","message":"Nickname already in use"}

## Register successful
Client registration successful.

    {"type":"REGISTER_SUCCESSFUL","emailNeedsVerification":true}

## Login failed
Login failed. `reason` contains the machine readable failure reason, `message` can be displayed to the user.

    {"type":"LOGIN_FAILED","reason":"ALREADY_LOGGED_IN","message":"You are already logged in"}

## Login successful
Login successful. Timestamp is the current timestamp on the server to synchronize with.

    {"type":"LOGIN_SUCCESSFUL","nickname":"Moe","timestamp":42}

## All players in lobby
A list of all players in the lobby.

    {"type":"LOBBY_PLAYERS","players":["Moe", "Player2"]}

## Create game failed
Game creation failed. `reason` contains the machine readable failure reason, `message` can be displayed to the user.

    {"type":"CREATE_GAME_FAILED","reason":"SLOT_COUNT_TOO_LOW","message":"Slot count must be greater than zero"}

## Create game successful
Game creation successful.

    {"type":"CREATE_GAME_SUCCESSFUL"}

## Open games
A list of all open games. The `info` field in each game is the JSON object which was passed in the info field on game creation.

    {"type":"OPEN_GAMES","games":[{"name":"Name #1","type":"TOY_RACER","slots":20,"takers":["Player #1"],"info":{"mapName":"Liberty City"}},{"name":"Name #2","type":"TOY_RACER","slots":10,"takers":["Player #2","Player #3"],"info":{"mapName":"Vice City"}}]}

## Join game failed
Join game failed. `reason` contains the machine readable failure reason, `message` can be displayed to the user.

    {"type":"JOIN_GAME_FAILED","reason":"GAME_NOT_OPEN","message":"Game is not open"}

## Join game successful
Join game successful.

    {"type":"JOIN_GAME_SUCCESSFUL","takers":[{"nickname":"Player #1","ready":false},{"nickname":"Player #2","ready":true}]}

## Player joined game / lobby
Player joined a game or the lobby.

    {"type":"PLAYER_JOINED","nickname":"Player #4"}

## Player left game / lobby
Player left a game or the lobby.

    {"type":"PLAYER_LEFT","nickname":"Player #4"}

## Buffered messages
A bundle of buffered messages from clients.

    {"type":"BUFFERED_MESSAGES","messages":[{"type":"BUFFERED_MESSAGE","timestamp":23,"payload":true,"sender":"Player #1"},{"type":"BUFFERED_MESSAGE","timestamp":42,"payload":true,"sender":"Player #2"}]}

## Not logged in
Client wants to perform an action which needs authentication.

    {"type":"NOT_LOGGED_IN"}

## Game created
Notifies the client when a game is created.

    {"type":"GAME_CREATED","game":{"name":"Name #1","type":"TOY_RACER","slots":20,"takers":["Player #1","Player #2"],"info":{"mapName":"Liberty City"}}}

## Taker changed his ready state
When a taker changes his ready state.

    {"type":"READY_CHANGED","nickname":"Player #1","ready":true}

## Game started
Game was started by the game host.

    {"type":"GAME_STARTED"}

## Not in game
Client tried to invoke an action without being in a game.

    {"type":"NOT_IN_GAME"}

## Start game failed
Start game failed. `reason` contains the machine readable failure reason, `message` can be displayed to the user.

    {"type":"START_GAME_FAILED","reason":"NOT_HOST","message":"You are not the host"}

## Client message
A message sent from one client to all the takers/lobby players.

    {"type":"CLIENT_MESSAGE","timestamp":1,"payload":true,"contentType":"CHAT_MESSAGE","sender":"Player #1"}

## Ping
Pings the client. Client has to respond with a pong

    {"type":"PING"}

## Pong
Response to `PING`.

    {"type":"PONG"}

## Leave game successful
Response to `LEAVE_GAME`. Indicates that the client has left the game successfully.

    {"type":"LEAVE_GAME_SUCCESSFUL"}

## Game deleted
A game has been deleted (because there are no more players in it).

    {"type":"GAME_DELETED","game":"Name #1"}

## Game has been closed
Response to `CLOSE_GAME`. Indicates that the game has been closed.

    {"type":"GAME_CLOSED","game":"Name #1"}

## Player storage
Response to `GET_PLAYER_STORAGE`. `content` contains the player storage and can be any valid JSON object.

    {"type":"PLAYER_STORAGE","content":true}

## Player storage set successful.
Response to `SET_PLAYER_STORAGE`. Player storage set successful.

    {"type":"SET_PLAYER_STORAGE_SUCCESSFUL"}

## Map storage
Response to `GET_MAP_STORAGE`. `content` contains the map storage and can be any valid JSON object.

    {"type":"MAP_STORAGE","content":true}

## Map storage set successful.
Response to `SET_MAP_STORAGE`. MAP storage set successful.

    {"type":"SET_MAP_STORAGE_SUCCESSFUL"}

## Log successful
Response to `LOG`. Log entry successfully stored.

    {"type":"LOG_SUCCESSFUL"}

## Email verification successful
Email has been successfully verified.

    {"type":"VERIFY_EMAIL_SUCCESS"}

## Email verification failed
Email couldn't verified.

    {"type":"VERIFY_EMAIL_FAILED","reason":"INVALID_TOKEN","message":"Invalid token"}

## Invalid packet
If a packet couldn't be parsed. `packet` contains the data, which could not be parsed.

    {"type":"INVALID_PACKET","packet":"foobar"}

## Reset password successful
If the password reset token has been successful sent to the players email address.

    {"type":"RESET_PASSWORD_SUCCESSFUL"}

## Reset password failed
If the password reset failed.

    {"type":"RESET_PASSWORD_FAILED","reason":"EMAIL_NOT_FOUND","message":"Email not found"}

## Set new password failed
If setting a new password failed.

    {"type":"SET_NEW_PASSWORD_FAILED","reason":"INVALID_TOKEN","message":"Invalid token"}

## Set new password successful.
Password of the player was successfully changed.

    {"type":"SET_NEW_PASSWORD_SUCCESSFUL"}

## Hello
Hello is sent when a client connects to the server.

    {"type":"HELLO","buildNumber":"#1","buildDate":"1970-01-01_00:00","buildRev":"1"}

## Chat message
A chat message from another player.

    {"type":"CHAT_MESSAGE","message":"Hello","sender":"Player #1"}

## Takers
The takers of the current game.

    {"type":"TAKERS","takers":[{"nickname":"Player #1","ready":true},{"nickname":"Player #2","ready":false},{"nickname":"Player #3","ready":false}]}

## Internal server error
Something went wrong on server side. Oops.

    {"type":"INTERNAL_SERVER_ERROR","details":"foobar"}

## Loading complete
Notifies the clients that a client has finished loading.

    {"type":"LOADING_COMPLETE","nickname":"Player #1"}

## All loading complete
Notifies the client that all clients have finished loading.

    {"type":"ALL_LOADING_COMPLETE"}

## Promoted to host
Notifies a client that he is now the host.

    {"type":"PROMOTED_TO_HOST"}

## Host has changed
Notifies a client that the host has changed.

    {"type":"HOST_CHANGED","newHost":"Player #2"}

## Unregister failed.
Unregistering an account from the server has failed.

    {"type":"UNREGISTER_FAILED","reason":"EMAIL_NOT_FOUND","message":"Email not found"}

## Unregister successful.
Unregistering an account was successful.

    {"type":"UNREGISTER_SUCCESSFUL"}
