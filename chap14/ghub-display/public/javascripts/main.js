(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    // path to the web jars. These definitions allow us 
    // to use "jquery", rather than "../lib/jquery/jquery",
    // when defining module dependencies.
    paths: {
      "jquery": "../lib/jquery/jquery",
      "underscore": "../lib/underscorejs/underscore",
      "d3": "../lib/d3js/d3",
      "nvd3": "../lib/nvd3/nv.d3",
      "bootstrap": "../lib/bootstrap/js/bootstrap"
    },

    shim: {
// hack to get nvd3 to work with requirejs.
// see this so question:
// http://stackoverflow.com/questions/13157704/how-to-integrate-d3-with-require-js#comment32647365_13171592        
      nvd3: {
        deps: ["d3.global"],
        exports: "nv"
      },
      bootstrap : { deps :['jquery'] }
    }

  }) ;
})(requirejs) ;

// hack to get nvd3 to work with requirejs.
// see this so question on Stack Overflow:
// http://stackoverflow.com/questions/13157704/how-to-integrate-d3-with-require-js#comment32647365_13171592
define("d3.global", ["d3"], function(d3global) {
  d3 = d3global;
});


// Initialize the view and controller to bootstrap the application.
require(["controller", "responseView"],
function(controller, responseView) {
  controller.initialize();
  responseView.initialize() ;
}) ;
