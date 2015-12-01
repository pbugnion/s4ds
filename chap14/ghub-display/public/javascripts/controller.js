
define(["jquery", "events", "model"], function($, events, model) {

  function initialize() {
    $("#user-selection").change(function() {
      var user = $("#user-selection").val() ;
      console.log("Fetching information for " + user) ;
      $("*").css({"cursor": "wait"}) ;
      $.getJSON("/api/repos/" + user, function(data) {
        model.exists = true ;
        model.repos = data ;
      }).fail(function() {
        model.exists = false ;
        model.repos = [] ;
      }).always(function() {
        model.ghubUser = user ;
        $("*").css({"cursor": "initial"}) ;
        events.trigger("model_updated") ;
      });
    }) ;
  }

  return { "initialize": initialize };

});
