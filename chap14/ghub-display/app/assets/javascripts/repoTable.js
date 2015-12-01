
define(["underscore"], function(_) {

  var rowTemplate = _.template("<tr>" + 
    "<td><%= name %></td>" +
    "<td><%= language %></td>" + 
    "<td><%= size %></td>" +
    "</tr>") ;

  var repoTable = _.template("<table id='repo-table' class='table'>" +
    "<thead>" +
      "<tr>" +
        "<th>Name</th><th>Language</th><th>Size</th>" +
      "</tr>" +
    "</thead>" +
    "<tbody>" +
      "<%= tbody %>" +
    "</tbody>" +
    "</table>") ;

  function build(model, divName) {
    var tbody = "" ;
    _.each(model.repos, function(repo) {
      tbody += rowTemplate(repo) ;
    }) ;
    var table = repoTable({tbody: tbody}) ;
    $(divName).html(table) ;
  }

  return { "build": build } ;
}) ;
