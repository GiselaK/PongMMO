var http = require("http");
var fs = require("fs");

var debugPage = fs.readFileSync("debug.html");

var parsePostData = function(data) {
    data = data.split("&");
    returnd = {};
    for (i = 0; i < data.length; i++) {
        data[i] = data[i].split("=");
        returnd[data[i][0]] = data[i][1];
    }
    return returnd;
}

var Point = function(x, y) {
    this.x = x;
    this.y = y;
}

var Player = function() {
    this.paddle = new Point(0, 0);
    this.ready = false;
}

var Game = function() {
    this.players = 0;
    this.p1 = new Player();
    this.p2 = new Player();
}

var game = new Game();

var processer = function(data) {
    switch (data.type) {
        case "JOIN":
            switch (game.players) {
                case 0:
                    console.log("Player one joined");
                    game.players++;
                    return JSON.stringify({result: "success", method: "JOIN", player: "ONE"});
                    break;
                case 1:
                    console.log("Player two joined");
                    game.players++;
                    return JSON.stringify({result: "success", method: "JOIN", player: "TWO"});
                    break;
                case 2:
                    console.log("Third player join attempt");
                    return JSON.stringify({result: "failure", method: "JOIN", issue: "FULL"});
                    break;
                default: 
                    console.log("Error: JOIN request defaulted");
                    return JSON.stringify({result: "error", method: "JOIN", issue: "SERVER"});
                    break;
            }
        case "READY":
            if (data.player == "ONE") {
                console.log("Player one ready")
                game.p1.ready = true;
            } else if (data.player == "TWO") {
                console.log("Player two ready");
                game.p2.ready = true;
            } else {
                console.log("ERROR: READY request invalid user id")
            }
            
            if (game.p1.ready && game.p2.ready) {
                console.log("Both players ready");
                return JSON.stringify({result: "success", method: "READY", ready: "TRUE"});
            } else {
                return JSON.stringify({result: "success", method: "READY", ready: "FALSE"});
            }
        case "CHECK":
            switch (data.checkType) {
                case "READY":
                    console.log("Ready state check request");
                    if (game.p1.ready && game.p2.ready) {
                        return JSON.stringify({result: "success", method: "CHECK", checkType: "READY", ready: "TRUE"});
                    } else {
                        return JSON.stringify({result: "success", method: "CHECK", checkType: "READY", ready: "FALSE"});
                    }
                case "PADDLE":
                    if (data.checkPlayer == "ONE") {
                        console.log("Check of paddle positio for player ONE");
                        return JSON.stringify({result: "success", method: "CHECK", checkType: "PADDLE", paddleX: game.p1.paddle.x, paddleY: game.p1.paddle.y});
                    } else if (data.checkPlayer == "TWO") {
                        console.log("Check of paddle position for player TWO.");
                        return JSON.stringify({result: "success", method: "CHECK", checkType: "PADDLE", paddleX: game.p2.paddle.x, paddleY: game.p2.paddle.y});
                    } else {
                        console.log("Error: PADLE CHECK request with invalid player.");
                        return JSON.stringify({result: "failure", method: "CHECK", checkType: "PADDLE"});
                    }
                    break;
                default:
                    console.log("Error: CHECK request defaulted");
                    return JSON.stringify({result: "failure", method: "CHECK", checkType: "READY"});
                    break;
            }
        case "MOVE":
            if (data.checkPlayer == "ONE") {
                game.p1.paddle.x = data.x;
                game.p1.paddle.y = data.y;
                console.log("Player ONE paddle set to "+ data.x+", "+data.y);
                return JSON.stringify({result: "success", method: "MOVE", player: "ONE", y: game.p2.paddle.y});
            } else if (data.checkPlayer == "TWO") {
                game.p2.paddle.x = data.x;
                game.p2.paddle.y = data.y;
                console.log("Player TWO paddle set to "+ game.p2.paddle.x+", "+game.p2.paddle.y);
                return JSON.stringify({result: "success", method: "MOVE", player: "TWO", y: game.p1.paddle.y});
            } else {
                console.log("Error: MOVE request with invalid player.")
                return JSON.stringify({result: "failure", method: "MOVE", player: data.player});
            }
        case "RESET":
            game = new Game();
            break;
    }
}

var server = http.createServer(function(req, res) {
    if (req.url == "/debug") {
        res.writeHead(200);
        res.end(debugPage);
    } else {
        var body = '';
        req.on('data', function (data) {
            body += data;
        });
        req.on('end', function () {
            res.writeHead(200);
            data = parsePostData(body);
            if (JSON.stringify(data) == "{}") {
                res.end("No Data"); 
            } else {
                res.end(processer(data));
            }
        }); 
    }
}).listen(process.env.PORT || 8000);
