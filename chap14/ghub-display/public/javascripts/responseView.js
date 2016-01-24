
define(["jquery", "model", "events", "repoTable", "repoGraph"],
function($, model, events, repoTable, repoGraph) {

  var successfulResponseHtml = 
    "<div class='col-md-6' id='response-table'></div>" +
    "<div class='col-md-6' id='response-graph'></div>" ;

  var failedResponseHtml = "<div class='col-md-12'>Not found</div>" ;

  function initialize() {
    events.on("model_updated", function() {
      if (model.exists) {
        $("#response").html(successfulResponseHtml) ;
        repoTable.build(model, "#response-table") ;
        repoGraph.build(model, "#response-graph") ;
      }
      else {
        $("#response").html(failedResponseHtml) ;
      }
    }) ;
  }

  return { "initialize": initialize } ;

});
