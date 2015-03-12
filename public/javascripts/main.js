

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