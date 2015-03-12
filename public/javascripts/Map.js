var Map = (function(){

	var center = [58.069, 7.993],
	boatRefs = new HashMap(),
	map = {},
	markers = [],
	historyLines = new HashMap(),
	boatIcon = {},
	myIcon = {},
	selfMarker =null,
	me = {};

	me.boatToBox = function (boat) {
        var source   = $("#entry-template").html();
        var template = Handlebars.compile(source);
        var context = {title: boat.name, telephone: boat.telephone, id:boat.id, description:boat.description, type:boat.type, lat:boat.latitude.toString().substr(0, 6),lng:boat.longitude.toString().substr(0, 6)};
        var html    = template(context);
		return html;
    },

    me.updateMarkerPos = function(boat) {      	
      	var newLatLng = new L.LatLng(boat.latitude, boat.longitude);
      	boatRefs.get(boat.id).setLatLng(newLatLng);     	
    },

   	me.updateBoat = function (id) {  
    	Gui.log(id);
   		var boat = Datastore.getBoat(id);
   		Gui.log(boat);
      	var html = this.boatToBox(boat);
      	
      	var ref = boatRefs.get(id);
      	Gui.log(ref);
      	
        ref.bindPopup(html, {closeButton:false});
        ref.update();
    },

    me.showHistory = function(marker) {
    	var id = marker.id;
    	
    	if(!id){
    		return;
    	}
    	
        var doneCallback = function(response) {
        	var history = $.map(response, function(val, i) {
            	console.log(val.latitude+"-"+val.longitude);
        		return [[val.latitude, val.longitude]];
        	});
        	if(history.length) {
        		
        		if(!historyLines.has(id)) {        			
        			var polyline = L.polyline(history, {color: '#03f', opacity: 0.5, dashArray: '5, 10'}).addTo(map);
        			historyLines.set(id, polyline);
        		}        		
        	}
        };
        Datastore.fetchHistoryForBoat(id, doneCallback);
    },

    me.addBoat = function(boat) {
		var icon = boatIcon;
		var myBoat = Datastore.getMyBoat();

        if(boat.id === myBoat.id) {
          icon = myIcon;
        } 
        var marker = L.marker(new L.LatLng(boat.latitude, boat.longitude), { icon: icon, title: boat.name });
        marker.id = boat.id;
        var html = Map.boatToBox(boat);
        marker.bindPopup(html, {closeButton:false});
        boatRefs.set(boat.id, marker);
        markers.addLayer(marker);
    },

    me.locate = function(setView) {

		map.locate({setView: setView, maxZoom: 16});

      	function onLocationFound(e) {

      		var myBoat = Datastore.getMyBoat();
      		var boats = Datastore.getBoats();

      		// if changed
      		if(myBoat.latitude != e.latitude
      				|| myBoat.longitude != e.longitude) {
      		
      			var radius = e.accuracy / 2;
      			myBoat.latitude = e.latitude;
      			myBoat.longitude = e.longitude;
      			
      			if(markers.hasLayer(boatRefs.get(myBoat.id))) {      						
      				Datastore.updatePosition();
      			}
      		}
  			if(markers.hasLayer(boatRefs.get(myBoat.id))) {
  				boatRefs.get(myBoat.id).setLatLng([myBoat.latitude,myBoat.longitude]).update();				  			
  			}
		}

		map.on('locationfound', onLocationFound);
		  
      	map.on('locationerror', function(e) {
      		toast('An error occured:'+e.message, 4000, 'error') // 4000 is the duration of the toast	
      	});

    	map.addLayer(markers);

    },
    me.getBounds = function() {
		return map.getBounds();
    },
	me.init = function() {

		map = L.map('map1', {
            center: center,
            zoom: 14,
            attribution: 'Statkart &copy;',
            maxZoom: 18,
            minZoom: 8
	    }),

		markers = L.markerClusterGroup({spiderfyOnMaxZoom:true, showCoverageOnHover: true, zoomToBoundsOnClick: true, spiderfyDistanceMultiplier:5}),
		boatIcon = L.icon({
	        iconUrl: '/assets/images/1423768479_ship.png',
	        iconSize:     [64, 64], // size of the icon
	        iconAnchor:   [32, 32], // point of the icon which will correspond to marker's location
	        popupAnchor:  [2, -32] // point from which the popup should open relative to the 
	     }),

		myIcon = L.icon({
	        iconUrl: '/assets/images/1423768568_Location.png',
	        iconSize:     [46, 64], // size of the icon
	        iconAnchor:   [23, 64], // point of the icon which will correspond to marker's location
	        popupAnchor:  [2, -68] // point from which the popup should open relative to the 
	    }),

        map.setMaxBounds([

             [55.000, -12.993],
             [80.069, 30.993]
        ]);

        var layer1 = L.tileLayer('http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=sjo_hovedkart2&zoom={z}&x={x}&y={y}',
			{
			    attribution: 'Statkart.no ©',
			    maxZoom: 18
			}
        ).addTo(map);
        
        map.on('popupopen', function(e) {
        	var marker = e.popup._source;
        	me.showHistory(marker);
        });
                
        map.attributionControl.setPrefix('');
        Datastore.fetchBoats();
	}

	return me;
}());