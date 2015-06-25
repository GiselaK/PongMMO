var http = require("http");
var fs = require("fs");

var server = http.createServer(httpHandler).listen(8080);

var httpHandler = function (req, res) {
  var body = "";
  req.on("data", function (data) {
    body += data;
  });
  req.on("end", function (){
    console.log("Req Ended");
    parseRequest(body);
  });
}

var parseRequest = function (data) {
  try {
    var jsonData = JSON.parse(data);
  }
  catch (e) {
    console.log("Json Parse Failed")
    console.log(e);
    req.writeHead(500);
    return null;
  }
}

/*var game = new function() {
  this.Point = function() {
    this.x = 0;
    this.y = 0;
  }
  this.Player = function(id) {
    this.pos = new Point();
    this.score = 0;
    this.id = igame.players.length;
  }
  this.Players = function() {
    this.players = [];
  }
  this.Ball = function () {
    this.pos = new game.Point();
  }
  this.ball = null;
  this.players = new this.Players();
}

game.ball = new game.Ball();

var handleRequest = function(data) {
  switch (data.request) {
    case "JOIN":
        game.players.players.push(new game.Player());
        res.writeHead(200);
        res.end("You have joined as player ", game.players.players[0].id);
        console.log("Player "+game.players.players[0].id+" joined.");
      break;
  }
}*/
