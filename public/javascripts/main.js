

	 var wsUri = "ws://localhost:9000/socket"; 
	 var output;

function testWebSocket() { 
	websocket = new WebSocket(wsUri); 
	websocket.onopen = function(evt) { onOpen(evt) }; 
	websocket.onclose = function(evt) { onClose(evt) }; 
	websocket.onmessage = function(evt) { onMessage(evt) }; 
	websocket.onerror = function(evt) { onError(evt) }; }  


function onOpen(evt) { 
	console.log("CONNECTED");
	console.log(evt);
	var msg = JSON.toString({"client": "Jonsen"});
	doSend(msg); 
}  

function onClose(evt) { 
	console.log("DISCONNECTED"); }  

function onMessage(evt) { 
	
	console.log(evt);
	var data = JSON.parse(evt.data);
	console.log(data); 
}

function doSend(message) { 	  
	websocket.send(message);
} 

$(document).ready(function() {
        	

  Datastore.init();
  Map.init();
  Gui.init();
  
  Datastore.getUpdatesPeriodically();
  
  if ('addEventListener' in document) {
    document.addEventListener('DOMContentLoaded', function() {
        FastClick.attach(document.body);
    }, false);
  }
  document.addEventListener("touchstart", function(){}, true);

});    