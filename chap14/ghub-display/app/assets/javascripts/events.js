
define(["jquery"], function($) {

  var bus = $(window) ;

  function trigger(eventType) {
    $(bus).trigger(eventType) ;
  }

  function on(eventType, f) {
    $(bus).on(eventType, f) ;
  }

  return {
    "trigger": trigger,
    "on": on
  } ;

});
