
define(["underscore", "d3", "nvd3"], function(_, d3, nv) {

    function generateDataFromModel(model) {
      var language2Repos = _.groupBy(model.repos, function(repo) { 
        return repo.language ;
      }) ;
      var plotObjects = _.map(language2Repos, function(repos, language) {
        var sizes = _.map(repos, function(repo) { return repo.size; });
        var totalSize = _.reduce(sizes, function(memo, size) { return memo + size; }, 0) ;
        return { label: language, size: totalSize } ;
      }) ;
      return plotObjects; 
    }

    function build(model, divName) {
      var transformedModel = generateDataFromModel(model) ;
      console.log(transformedModel) ;
      nv.addGraph(function() {
        var chart = nv.models.pieChart()
          .x(function (d) { return d.label ; })
          .y(function (d) { return d.size ;})
          .width(350)
          .height(350) ;

        d3.select(divName).append("svg")
          .datum(transformedModel)
          .transition()
          .duration(350)
          .attr('width', 350)
          .attr('height', 350)
          .call(chart) ;

        return chart ;
      });
    }

    return { "build" : build } ;

});
