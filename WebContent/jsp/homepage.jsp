<%@ page language="java" contentType="text/html; charset=windows-1255" pageEncoding="windows-1255"%>

<!DOCTYPE html>
<html ng-app = "searchEngineApp">
<head>

<% String contextPath = request.getContextPath(); %>

<script src="<%=contextPath %>/js/jquery-1.11.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="<%=contextPath %>/js/angular.min.js"></script>
<script src="<%=contextPath %>/js/jquery.highlight-5.js"></script>
<script src="<%=contextPath %>/js/scripts.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" media="all" href="<%=contextPath %>/css/style.css" />

<script>

/************************************ANGULAR****************************************/

	var app = angular.module('searchEngineApp',[]);

 	app.controller('homePageController', function($scope, $http, $sce) {
 		
		$("#backToSearchIndex").css("display","none");
		$("#print").css("display","none");
 		
		$scope.docs ={};
		$scope.oneDoc ={};
		$scope.locations = {};
		$scope.results = {};
		$scope.ready = false;
		$("#searchInput").keypress(function(event) {
    		if (event.which == 13) {
        		event.preventDefault();
        		$scope.searchButton($scope.search);
    		}
		});

$scope.searchButton = function(search) {

			$http.get("<%= contextPath %>/services/home-page/queries?query=" + search).success(function(data) {
				$scope.docs = data;
				
				$http.get("<%= contextPath %>/services/home-page/words?query=" + search).success(function(results) {
					//there is a match with pattern and text
					$("pre").highlight(results, { wordsOnly: true });
					$scope.results = results;
					angular.forEach(data, function(value, key) { 
						if (!results) {
					        return $sce.trustAsHtml(value.summary);
					    }
					    return $sce.trustAsHtml(value.summary.replace(new RegExp(results, 'gi'), '<span class="highlight">$&</span>'));
					});
				});
			});
		}
		
		$scope.showDocument = function(link) {
			$(".searchResultWrapper").css("display","none");
			$("#song").css("display","block");
			$("#submitButton").prop('disabled', true);
			$("#searchInput").prop('disabled', true);
			$("#backToSearchIndex").css("display","block");
			$("#print").css("display","block");
			$("#backTologin").css("display","none");
			
			
			$http.get("<%= contextPath %>/services/home-page/getSong?link=" + link).success(function(data) {
				$scope.oneDoc = data;
				$scope.ready = true;
 
			});
					
		}
		
		$scope.highlight = function(text) {

			if ($scope.ready) {
			
				$scope.results.push(""); // enable wordsOnly on single word
				$("pre").highlight($scope.results, { wordsOnly: true });
				var search = $scope.results;
			    if (!search) {
			        return $sce.trustAsHtml(text);
			    }
			    return $sce.trustAsHtml(text.replace(new RegExp(search, 'gi'), '<span class="highlight">$&</span>'));
			 }
		};
		
		$scope.returnToDocList = function() { 
			$(".searchResultWrapper").css("display","block");
			$("#song").css("display","none");
			$("#submitButton").prop('disabled', false);
			$("#searchInput").prop('disabled', false);
			$("#backToSearchIndex").css("display","none");
			$("#print").css("display","none");
			$("#backTologin").css("display","block");
			$scope.ready = false;
		}
		
	});
	function printSelection(printDoc){
		  var pwin=window.open('','print_content','width=1240px,height=960px');
		  pwin.document.open();
		  pwin.document.write('<html><body onload="window.print()">'+printDoc.innerHTML+'</body></html>');
		  pwin.document.close();
		  setTimeout(function(){pwin.close();},1000);

		}

</script>

</head>
<body ng-controller="homePageController">
	<div class="wrapper">
		<h3>Search Songs Engine</h3>
		<div class="form-group">
			<button type="submit" class="btn btn-default" id ="submitButton" ng-click ="searchButton(search)" >Submit</button>
			<input type="text" class="form-control" id="searchInput" ng-model = "search">
		</div>
		<div class="searchResultWrapper" ng-repeat="doc in docs" ng-click= showDocument(doc.link)>
			<h3 class="searchShortCut"> *The system is internal</h3>
			<h2 class="searchHeadLine">{{doc.topic}}</h2>
			<h3 class="searchShortCut">{{doc.date}}</h3>
			<h3 class="searchShortCut">{{doc.author}}</h3>
			<pre class="searchShortCut"> {{doc.summary}}</pre>
		</div>
		<div id="song"> 
			<h1> *The system is internal</h1>
			<h2 class="searchHeadLine">Topic: {{oneDoc.topic}}</h2>
			<h3 class="searchShortCut">Date : {{oneDoc.date}}</h3>
			<h3 class="searchShortCut">Author:{{oneDoc.author}}</h3>
			<pre class="searchShortCut" ng-if = ready ng-bind-html="highlight(oneDoc.summary)">{{oneDoc.summary}}</pre>
			<button type="button"  class="btn btn-default" ng-click ="returnToDocList()" >back</button>
		</div>
			<a href = "<%= contextPath %>/services/home-page/login" id ="backTologin" class="btn btn-default" >back</a>
			<button type="button" id = "print" class="btn btn-success" onclick="printSelection(document.getElementById('song'));">Print</button>
	</div>
</body>
</html>