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
  this.ready = false;
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
  // var ready="FALSE"
  console.log(jsonData.request + " Request");
  switch (jsonData.request) {
    case "JOIN":
    console.log("JOINING player:"+(games[0].players.length+1));
      if (games[0].players.length == 0) {
        games[0].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 1, game: 0, ready: "FALSE"})};
      } else if (games[0].players.length == 1) {
        // ready="TRUE";
        games[0].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 2, game: 0, ready: "FALSE"})};
      } else {
        games[0].players=[];
        console.log("New Game Players:"+games[0].players.length);
        games[0].p1 = [new Point(0, 0)];
        games[0].p1d = [new Point(0, 0)];
        games[0].ball = [new Ball(0, 0, 0, 0)];
        games[0].p2 = [new Point(0, 0)];
        games[0].p2d = [new Point(0, 0)];
        games[0].players.push(new Player());
        returnData = {status:200, data:JSON.stringify({player: 1, game: 0, ready: "FALSE"})};
      } 
      break;
    case "CHECK":
    // console.log("PLAYERS:"+games[0].players.length);
    console.log("Checking players:"+games[0].players.length);
      if (games[0].players.length == 0) {
        returnData = {status:200, data:JSON.stringify({player: 0, game: 0, ready: "FALSE"})};

      } else if (games[0].players.length == 1) {
        returnData = {status:200, data:JSON.stringify({player: 1, game: 0, ready: "FALSE"})};
       
      }
        else {
        returnData = {status:200, data:JSON.stringify({player: 2, game: 0, ready: "TRUE"})};
      }
      break;
      
    case "UPDATE":
      var Game = games[jsonData.game];
      var theTime = Math.floor(Date.now() / 1000);
      // var run = 0;
      // console.log(jsonData);
      if (jsonData.player == 1) {
        Game.p1.push(new Point(0, jsonData.y));
        Game.p1d.push(new Point(0, jsonData.direction));
        Game.ball.push(new Ball(jsonData.bx, jsonData.by, jsonData.velocityX, jsonData.velocityY));
        lastPlayerUpdate=Game.p2[Game.p2.length-1].time;
        // console.log(Math.floor(Date.now() / 1000));
        // if(run==30){
          // console.log("theTime:"+theTime+" lastPlayerUpdate:"+lastPlayerUpdate);
          // if(theTime>Game.p2[Game.p2.length-1].time+5000){
            returnData = {status:200,data:JSON.stringify({y: Game.p2[Game.p2.length-1].y, direction: Game.p2d[Game.p2d.length-1].y, timeStamp: Game.p2[Game.p2.length-1].time})};
          // }
          // else{
          //   // returnData = {status:200, data:JSON.stringify({y: Game.p2[Game.p2.length-1].y, direction: Game.p2d[Game.p2d.length-1].y, timeStamp: Game.p2[Game.p2.length-1].time})};
          // }
        // }
      
          
          
      } else {
        Game.p2.push(new Point(0, jsonData.y));
        Game.p2d.push(new Point(0, jsonData.direction));
        lastPlayerUpdate=Game.p1[Game.p1.length-1].time;
        // console.log(Math.floor(Date.now() / 1000));
        // console.log("theTime:"+theTime+" lastPlayerUpdate:"+lastPlayerUpdate);
        // if(run==30){
          // console.log("theTime:"+theTime+" lastPlayerUpdate:"+lastPlayerUpdate);
          // if(theTime>Game.p1[Game.p1.length-1].time+5000){
          //   returnData = {status:200, data:JSON.stringify({y: Game.p1[Game.p1.length-1].y, by: Game.ball[Game.ball.length-1].y, bx:  Game.ball[Game.ball.length-1].x, direction: Game.p1d[Game.p1d.length-1].y, timeStamp: Game.p1[Game.p1.length-1].time, bx: Game.ball[Game.ball.length-1].pos.x, by: Game.ball[Game.ball.length-1].pos.y, vx: Game.ball[Game.ball.length-1].velocity.x, vy: Game.ball[Game.ball.length-1].velocity.y, setTime: Game.ball[Game.ball.length-1].pos.time})};
          // }
          // else{
            returnData = {status:200, data:JSON.stringify({y: Game.p1[Game.p1.length-1].y, by: Game.ball[Game.ball.length-1].y, bx:  Game.ball[Game.ball.length-1].x, direction: Game.p1d[Game.p1d.length-1].y, timeStamp: Game.p1[Game.p1.length-1].time, bx: Game.ball[Game.ball.length-1].pos.x, by: Game.ball[Game.ball.length-1].pos.y, vx: Game.ball[Game.ball.length-1].velocity.x, vy: Game.ball[Game.ball.length-1].velocity.y, setTime: Game.ball[Game.ball.length-1].pos.time})};
          // }
          // run=0;
        // }
        // else{
        //   run++;
        // }
      }
      break;
    default:
      returnData = {status:200, data:"Request Failed"};

  }
  return returnData;
}
 
//Create Objects
var server = http.createServer(httpHandler).listen(8080);