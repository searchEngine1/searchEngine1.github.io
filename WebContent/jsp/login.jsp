<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>

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
 <link rel="stylesheet" type="text/css" media="all" href="<%=contextPath %>/css/animate.css" />
</head>

<script>

/************************************ANGULAR****************************************/
	var app = angular.module('searchEngineApp',[]);
	
	app.controller('LoginPageController', function($scope, $http) {
		
		$(".help").hide();
		
		$scope.help = function(){
			$(".upload").hide();
			$(".help").show();
		}
		
	});
	
	
</script>
<body ng-controller="LoginPageController">
	<div class="wrapper">
		<div class="loginWrapper">
			<h1 class="animated  bounce"> Welcome to song search engine</h1>
				<a href="<%= contextPath %>/services/home-page/show"><button type="button" class="btn btn-default files  animated swing">Enter search engine</button></a>
				
				<!-- insert to DB new file -->
				<button type="button" class="btn btn-default files  animated swing" ng-click="help()">Help</button>
			
			<div class="help">
			<h2>Explanations about the queries in our search engine:</h2>
			<ul>
				<h2>The supported operators are:</h2>
				<li>
					<b>AND</b>
					<ul>example: hey AND mama</ul>
				</li>
				<li>
					<b>OR</b>
					<ul>example: hey OR mama</ul>
				</li>
				<li>
					<b>NOT</b>
					<ul>example: hey NOT mama</ul>
				</li>
				<li>
					<b>JOKER</b>
					<ul>example: *ways</ul>
					<ul>example: ways*</ul>
					<ul>example: *ways*</ul>
					<ul>example: w*s</ul>
				</li>
				<li>
					<b>BRACKETS</b>
					<ul>example: ( ( hey or mama ) and tell ) and your </ul>
				</li>
			</ul>
			
			<h2>* The search engine returns the results for synonyms words also</h2>
			</div>
				<a href="<%= contextPath %>/services/home-page/home"><button type="button" id ="backToSearchIndex" class="btn btn-default  animated swing"  >back</button>
			</a>
		</div>
	 	
	</div>
</body>
</html>