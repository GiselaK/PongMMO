# PongMMO Server
The Node.js backend for the PongMMO android app.

## Connection Protocols

1. JOIN - Attempt to join the game.

2. READY - Tell the server that your client is ready for the game to begin.

3. CHECK - Check the value of various game state variables.

4. MOVE - Push an update of your current paddle position.

5. RESET - Reset the game and player data.

## Debug Page
Some protocols avaliable for debug at the /debug page.

## Connection Documentation
Connection protocols are documented in the wikki, and are linked from the [connection page](https://github.com/PongMMO/PongMMO-S/wiki/Connection-Methods).