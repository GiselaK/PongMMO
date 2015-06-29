/*
* PongMMO - Node.js Server
* Version 1.0.0
* By: Elias Lawson-Fox
*/

//Imports
var http = require("http");

//Helper Functions
var last = function(arr) {
  return arr[arr.length-1];
}

//Helper Classes
var Point = function(x, y) {
  this.x = x;
  this.y = y;
  this.time = Math.floor(Date.now() / 1000);
}
var Player = function() {
  this.pos = new Point(0, 0);
  this.score = 0;
  this.id = Game.players.length+1;
}

//Global Game Class
var Ball = function(x, y, vx, vy) {
  this.pos = new Point(x, y);
  this.velocity = new Point(vx, vy);
}

var game = function() {
  this.players = [];
  this.p1 = [new Point(0, 0)];
  this.p1d = [new Point(0, 0)];
  this.ball = [new Ball(0, 0, 0, 0)];
  this.p2 = [new Point(0, 0)];
  this.p2d = [new Point(0, 0)];
}

var games = [];
games.push(new game());

//Handler for HTTP Requests
var httpHandler = function(req, res) {

  var body = "";
  req.on("data", function (data) {
    body += data;
  });
  req.on("end", function (){
    var jsonData = parseRequest(body);
    var response = handleRequest(jsonData);
    res.writeHead(response.status);
    res.end(response.data);
  });
}

//Convert Reqests to JSON
var parseRequest = function(data) {
    data = data.split("&");
    parsedData = {};
    for (i = 0; i < data.length; i++) {
        data[i] = data[i].split("=");
        parsedData[data[i][0]] = data[i][1];
    }
    return parsedData;
}

//Handle the Actual Server Request
var handleRequest = function(jsonData) {
  var returnData;
  console.log(jsonData.request + " Request")
  switch (jsonData.request) {
    case "JOIN":
      if (games[games.length-1].players.length == 0) {
        games[games.length-1].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 1, game: games.length-1})};
      } else if (games[games.length-1].players.length == 1) {
        games[games.length-1].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 2})};
      } else {
        games.push(new game());
        games[games.length-1].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 1})};
      }
      break;
    case "UPDATE":
      var Game = game[jsonData.game];
      if (jsonData.player == 1) {
        Game.p1.push(new Point(0, jsonData.y));
        Game.p1d.push(new Point(0, jsonData.direction));
        Game.ball.push(new Ball(jsonData.bx, jsonData.by, jsonData.velocityX, jsonData.velocityY));
        returnData = {status:200, data:JSON.stringify({y: Game.p2[Game.p2.length-1].y, direction: Game.p2d[Game.p2d.length-1].y, timeStamp: Game.p2[Game.p2.length-1].time})};
      } else if (jsonData.player == 2) {
        Game.p2.push(new Point(0, jsonData.y))
        Game.p2d.push(new Point(0, jsonData.direction));
        returnData = {status:200, data:JSON.stringify({y: Game.p1[Game.p1.length-1].y, by: Game.ball[Game.ball.length-1].y, bx:  Game.ball[Game.ball.length-1].x, direction: Game.p1d[Game.p1d.length-1].y, timeStamp: Game.p1[Game.p1.length-1].time, bx: Game.ball[Game.ball.length-1].pos.x, by: Game.ball[Game.ball.length-1].pos.y, vx: Game.ball[Game.ball.length-1].velocity.x, vy: Game.ball[Game.ball.length-1].velocity.y, setTime: Game.ball[Game.ball.length-1].pos.time})};
      }
      break;
    case "RESET":
      Game = new function() {
        this.players = [];
        this.p1 = [new Point(0, 0)];
        this.p1d = [new Point(0, 0)];
        this.ball = [new Ball(0, 0, 0, 0)];
        this.p2 = [new Point(0, 0)];
        this.p2d = [new Point(0, 0)];
      }
      returnData = {status: 200, data:"Meh"};
      break;
    default:
      returnData = {status:200, data:"Request Failed"};
  }
  return returnData;
}

//Create Objects
var server = http.createServer(httpHandler).listen(8080);
