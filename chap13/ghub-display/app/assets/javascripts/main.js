

(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    paths: {
      "jquery": "../lib/jquery/jquery",
      "underscore": "../lib/underscorejs/underscore",
      "d3": "../lib/d3js/d3",
      "nvd3": "../lib/nvd3/nv.d3",
      "bootstrap": "../lib/bootstrap/js/bootstrap"
    },
// hack to get nvd3 to work with requirejs.
// see this so question:
// http://stackoverflow.com/questions/13157704/how-to-integrate-d3-with-require-js#comment32647365_13171592
    shim: {
      nvd3: {
        deps: ["d3.global"],
        exports: "nv"
      },
      bootstrap : { deps :['jquery'] }
    }

  }) ;
})(requirejs) ;

// hack to get nvd3 to work with requirejs.
// see this so question:
// http://stackoverflow.com/questions/13157704/how-to-integrate-d3-with-require-js#comment32647365_13171592
define("d3.global", ["d3"], function(d3global) {
  d3 = d3global;
});

require(["jquery", "underscore", "bootstrap", "model", "events", "responseView"], 
function($, _, bootstrap, model, events, responseView) {

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

  responseView.initialize() ;

}) ;
