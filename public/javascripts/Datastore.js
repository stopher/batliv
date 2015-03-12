var Datastore = (function(){
	
	var myBoat = {
		id:null,
		name: "",
        telephone: "",
        description: "",
        type: "",
        latitude: 58.069,
        longitude: 7.993		
	},
	boats = new HashMap(),
	BASE_URL = "http://"+APP_HOST+"/",
	settings =  {
    
		NEW_BOAT_URL : BASE_URL+'boat',
		FETCH_BOATS_URL : BASE_URL+'boats',
	    FETCH_AREA_URL : BASE_URL+'area',
	    FETCH_HISTORY_URL : BASE_URL+'history',
	    UPDATE_POSITION_URL : BASE_URL+'position',
	    FETCH_CHAT_URL : BASE_URL+'chat'
	},
	doRequest = function(url, type, data, doneCallback, toastmessage) {
	    var xhr = $.ajax({
	      type: type,
	      url: url,
	      data: JSON.stringify(data),
	      contentType: 'application/json; charset=UTF-8',
	      dataType: 'json'
	    })
	    .done(function( response ) {
	      if(doneCallback) {
	        doneCallback(response);
	      }
	      if(toastmessage) {
	        toast(toastmessage, 4000, 'info') // 4000 is the duration of the toast
	      }
	    })
	    .fail(function(jqXHR, textStatus, errorThrown) {
	      toast('An error occured:'+textStatus+". Desc:"+errorThrown, 4000, 'error') // 4000 is the duration of the toast
	    })
	    .always(function( jqXHR, textStatus, errorThrown ) {
	      Gui.hideLoader();
	    });  
	},
	doGetRequest = function(url, type, data, doneCallback, toastmessage) {
	    var xhr = $.ajax({
	      type: type,
	      url: url,
	      data: data,
	      dataType: 'json'
	    })
	    .done(function( response ) {
	      if(doneCallback) {
	        doneCallback(response);
	      }
	      if(toastmessage) {
	        toast(toastmessage, 4000, 'info') // 4000 is the duration of the toast
	      }
	    })
	    .fail(function(jqXHR, textStatus, errorThrown) {
	      toast('An error occured:'+textStatus+". Desc:"+errorThrown, 4000, 'error') // 4000 is the duration of the toast
	    })
	    .always(function( jqXHR, textStatus, errorThrown ) {
	      Gui.hideLoader();
	    });  
	},  
	me = {};
	me.getMyBoat = function() {
		return myBoat;
	},
	me.getBoats = function() {
		return boats;
	},	
	me.getBoat = function(id) {
		return boats.get(id);
	},
	me.updatePosition = function() {
	    var doneCallback = function(response) {
	      myBoat = response;
	      Map.updateBoat(myBoat.id);
	    };
    	doRequest(settings.UPDATE_POSITION_URL, "POST", myBoat, doneCallback, "Updated my boat");
	},
	me.fetchHistoryForBoat = function(id, doneCallback) {
		doGetRequest(settings.FETCH_HISTORY_URL, "GET", {"id":id}, doneCallback, "Fetched history");
	},

	me.saveBoat = function() {
		
	    myBoat.name = $("#yourname").val();  
	    myBoat.telephone = $("#yourtelephone").val();  
	    myBoat.description = $("#yourdescription").val();  
	    myBoat.type = $("#yourtype").val();

	    localStorage.setItem("myBoat", JSON.stringify(myBoat)); // local store
	
	    var doneCallback = function(response) {
	      
	      myBoat = response;
		  if(!boats.has(myBoat.id)) {
			  Map.addBoat(myBoat);
		  }
		  boats.set(myBoat.id, myBoat);
		  
	      Gui.log(myBoat);
	      Map.updateBoat(myBoat.id);
	    };
	    doRequest(settings.NEW_BOAT_URL, "POST", myBoat, doneCallback, "Saved boat");
	},
	me.getUpdatesPeriodically = function() {
	  setInterval(me.fetchBoatsInArea, 60000);  
	},
	me.fetchBoatsInArea = function ( ) {
	    Gui.log("fetching boats in area");
	    var bounds = Map.getBounds();
	    var northwestLat = bounds.getNorthWest().lat;
	    var northwestLng = bounds.getNorthWest().lng;
	    var southeastLat = bounds.getSouthEast().lat;
	    var southeastLng = bounds.getSouthEast().lng;

	    var doneCallback = function(response) {
 
			for (var i = 0; i < response.length; i++) {
			    var boat = response[i];
			    if(boats.has(i)) {
			      var exBoat = boats.get(i);
			      
			      exBoat.latitude = boat.latitude;
			      exBoat.longitude = boat.longitude;
			      boats.set(i, exBoat);
			      Map.updateMarkerPos(exBoat);
			    }
			}
	    };
	    doRequest(settings.FETCH_AREA_URL, "POST", {"northwestLat":northwestLat, "northwestLng":northwestLng, "southeastLat":southeastLat,"southeastLng":southeastLng }, doneCallback, "Loaded boats");
  	},
	me.fetchBoats = function () {
	    var doneCallback = function(response) {
	 
	      for (var i = 0; i < response.length; i++) {
	        var boat = response[i];
	        boats.set(boat.id, boat);
	        Map.addBoat(boat);
	      }
	      //Map.locate(true);
	    };
	    doGetRequest(settings.FETCH_BOATS_URL, "GET", {}, doneCallback, "Loaded boats");
  	},
  	
  	me.fetchChat = function () {
  		var doneCallback = function(response) {  			 
  	      for (var i = 0; i < response.length; i++) {
  	    	  console.log(response[i]);
  	      }
  	    };
  	    doGetRequest(settings.FETCH_CHAT_URL, "GET", {}, doneCallback, "Loaded chat");
  	},

  	me.init = function() {
     	if(localStorage.getItem("myBoat")) {
        	myBoat = JSON.parse(localStorage.getItem("myBoat"));
     	}
	};

	return me;
}());