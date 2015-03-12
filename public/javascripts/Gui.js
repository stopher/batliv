var Gui = (function(){

	var me = {};
	var homePage = document.getElementById("map1"),
	accountpage = document.getElementById("accountpage"),
	currentPage = homePage;

	function slidePageFrom(page, from) {
	    // Position the page at the starting position of the animation
	    page.className = "page " + from;
	    // Position the new page and the current page at the ending position of their animation with a transition class indicating the duration of the animation
	    page.className ="page transition center";
	    currentPage.className = "page transition " + (from === "left" ? "right" : "left");
	    currentPage = page;
	}
	me.showLoader = function() {
		$(".loader").show();
	},
	me.hideLoader = function() {
		$(".loader").hide();
	},
	me.log = function(text) {
		if (typeof console == "object") {
			console.log(text);
		}
	},
	me.init = function() {
		$(".button-collapse").sideNav({edge: 'left'});
		$('.modal-trigger').leanModal({
			dismissible: true, // Modal can be dismissed by clicking outside of the modal
			opacity: 0.2, // Opacity of modal background
			in_duration: 300, // Transition in duration
			out_duration: 200, // Transition out duration
			ready: function() {   $("body").addClass("modal-open"); }, // Callback for Modal open
			complete: function() { $("body").removeClass("modal-open"); } // Callback for Modal close
			});

		  $('select').material_select();
		  $('.collapsible').collapsible({
		    accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
		  });

		  $(".baatok").on("click", function() {
		      // save boat
		      Datastore.saveBoat();

		      $("#map1").fadeIn();
		      $(".mmbtn").removeClass("active");
		      $(".kart").addClass("active");
		  });

		  $(".findme").on("click",function() {
		  	$('.button-collapse').sideNav('hide'); 
		  	$("#map1").fadeIn();
		    Map.locate(true);
		  });

			/*
		  $(".locateboat").on("click", function() {
		     //console.log("locating boat"); 
		     $('#modalFriends').closeModal();
		     Map.map.setView(new L.LatLng(51.3, 0.7),9);
		  });
		  */

		  $(".minbaat").on("click",function() {
		     $('.button-collapse').sideNav('hide'); 
		     $("#map1").fadeOut();
		  });

		  $(".kart").on("click",function() {
		      $("#map1").fadeIn();
		      $('.button-collapse').sideNav('hide'); 
		  });

		  $(".show-history").on("click", function() {
		  	var dataId = $(this).attr("data-id");        	
		  });

		  $(".mmbtn").on("click",function() {          
		    $(".mmbtn").removeClass("active");
		    $(this).addClass("active");
		  });
		  		  	
		  $(".chat").on("click", function()  {
			  $('.button-collapse').sideNav('hide'); 			 			  
			  $('#modalChat').openModal();  
			  Datastore.fetchChat();
		  });
		  
		  $(".minbaat").on("click", function() {

		  	var myBoat = Datastore.getMyBoat();

		    $("#yourname").val(myBoat.name).siblings("label").addClass("active");          
		    $("#yourtelephone").val(myBoat.telephone).siblings("label").addClass("active");
		    $("#yourdescription").val(myBoat.description).siblings("label").addClass("active");
		    
		    
		    $("#yourtype option[selected]").removeAttr("selected");
		    
		    if(myBoat.type) {		    	
		    	$("#yourtype option[value="+myBoat.type+"]").attr("selected", "selected");
		    }

		  });
	};

	return me;
}());