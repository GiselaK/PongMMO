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
var Game = new function() {
  this.players = [];
  this.p1 = [new Point(0, 0)];
  this.p1d = [new Point(0, 0)];
  this.ball = [new Ball(0, 0, 0, 0)];
  this.p2 = [new Point(0, 0)];
  this.p2d = [new Point(0, 0)];
}

var Ball = function(x, y, vx, vy) {
  this.pos = new Point(x, y);
  this.velocity = new Point(vx, vy);
}
//Handler for HTTP Requests
var httpHandler = function(req, res) {
  var body = "";
  req.on("data", function (data) {
    body += data;
  });
  req.on("end", function (){
    console.log(body+"");
    var jsonData = parseRequest(body);
    var response = handleRequest(jsonData);
    console.log(response);
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
  console.log(JSON.stringify(jsonData));
  switch (jsonData.request) {
    case "JOIN":
      Game.players.push(new Player());
      returnData = {status:200, data:"Joined as player "+last(Game.players).id};
      break;
    case "UPDATE":
      if (jsonData.player == 1) {
        Game.p1.push(new Point(0, jsonData.y));
        Game.p1d.push(new Point(0, jsonData.direction));
        returnData = {status:200, data:JSON.stringify({y: Game.p2[Game.p2.length-1].y, direction: Game.p2d[Game.p2d.length-1].y})};
      } else if (jsonData.player == 2) {
        Game.p2.push(new Point(0, jsonData.y))
        Game.p2d.push(new Point(0, jsonData.direction));
        returnData = {status:200, data:JSON.stringify({y: Game.p1[Game.p1.length-1].y, by: Game.ball[Game.ball.length-1].y, bx:  Game.ball[Game.ball.length-1].x, direction: Game.p1d[Game.p1d.length-1].y})};
      }
      break;
    case "BALL":
      if (data.meth == "SET") {
        Game.ball.push(new Ball(data.x, data.y, data.velocityX, data.velocityY));
      } else if (data.meth == "GET") {
        returnData = {status: 200, data:JSON.stringify({x: Game.ball[Game.ball.length-1].pos.x, y: Game.ball[Game.ball.length-1].pos.y, vx: Game.ball[Game.ball.length-1].velocity.vx), vy: Game.ball[Game.ball.length-1].velocity.vy, setTime: Game.ball[Game.ball.length-1].pos.time}
      }
    default:
      returnData = {status:200, data:"Request Failed"};
  }
  return returnData;
}

//Create Objects
var server = http.createServer(httpHandler).listen(8080);
