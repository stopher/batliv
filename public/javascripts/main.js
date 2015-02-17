
$(document).ready(function() {
        	

  Map.init();
  Datastore.init();
  Gui.init();
  
  if ('addEventListener' in document) {
    document.addEventListener('DOMContentLoaded', function() {
        FastClick.attach(document.body);
    }, false);
  }
  document.addEventListener("touchstart", function(){}, true);

});    