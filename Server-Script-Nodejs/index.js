var WebSocketServer = require('websocket').server;
var http = require('http');
var connArray=[];
var server = http.createServer(function(request, response) {
  // process HTTP request. Since we're writing just WebSockets
  // server we don't have to implement anything.
});
server.listen(1337, function() { });

// create the server
wsServer = new WebSocketServer({
  httpServer: server
});

// WebSocket server
wsServer.on('request', function(request) {
  var connection = request.accept(null, request.origin);
  var index =  connArray.push(connection) - 1;
  connection.on('message', function(message) {
    if (message.type === 'utf8') {
            //console.log('Received Message: ' + message.utf8Data);
            //connection.sendUTF(message.utf8Data);
            for (var i = 0; i < connArray.length; i++) {
              try
              {
                if(i !== index)
                  connArray[i].sendUTF(message.utf8Data);
              }
              catch(err)
              {
                //connArray.splice(i, 1); // remove 1 elements from ith index
              }
            }
    }
    else if (message.type === 'binary') {
            //console.log('Received Binary Message of ' + message.binaryData.length + ' bytes');
            //connection.sendBytes(message.binaryData);
            for (var i = 0; i < connArray.length; i++) {
              try
              {
                if(i !== index)
                  connArray[i].sendBytes(message.binaryData);
              }
              catch(err)
              {
                connArray.splice(i, 1); // remove 1 elements from ith index
              }
            }
    }
  });

  connection.on('close', function(connection) {
    connArray.splice(index, 1);
  });
})
