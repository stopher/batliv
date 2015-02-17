var Datastore = (function(){
	
	var myBoat = {
		id:0,
		name: "",
        telephone: "",
        description: "",
        type: "",
        latitude: 58.069,
        longitude: 7.993		
	},
	boats = new HashMap(),
	BASE_URL = "http://192.168.2.96:9000/",
	settings =  {
    
		NEW_BOAT_URL : BASE_URL+'boat',
		FETCH_BOATS_URL : BASE_URL+'boats',
    FETCH_AREA_URL : BASE_URL+'area',
    FETCH_HISTORY_URL : BASE_URL+'history',
    UPDATE_POSITION_URL : BASE_URL+'position'
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

    if(myBoat.id) {
      boats.set(myBoat.id, myBoat);
    }

    localStorage.setItem("myBoat", JSON.stringify(myBoat)); // local store

    var doneCallback = function(response) {
      myBoat = response;
      Map.updateBoat(myBoat.id);
    };
    doRequest(settings.NEW_BOAT_URL, "POST", myBoat, doneCallback, "Saved boat");
  },
	
  me.fetchBoatsInArea = function ( ) {
    

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
          console.log(exBoat.id);
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
      //boats = response;      
      for (var i = 0; i < response.length; i++) {
        var boat = response[i];
        boats.set(boat.id, boat);
        Map.addBoat(boat);
      }
      Map.locate(true);
    };
    doRequest(settings.FETCH_BOATS_URL, "GET", myBoat, doneCallback, "Loaded boats");
  },

	me.init = function() {
     	if(localStorage.getItem("myBoat")) {
        	myBoat = JSON.parse(localStorage.getItem("myBoat"));
     	}
	};

	return me;
}());