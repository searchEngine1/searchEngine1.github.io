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
		
		
	});
	
	
</script>
<body ng-controller="LoginPageController">
	<div class="wrapper">

		<div class="loginWrapper">
			<h1 class="animated  bounce">Wellcome to our search engine</h1>
			<div class="centerize">
			<a href="<%= contextPath %>/services/home-page/admin"><button type="button" class="btn btn-default files animated swing">Admin</button></a>
			<a href="<%= contextPath %>/services/home-page/login"><button type="button" class="btn btn-default files animated swing">User</button></a>
	 	</div>
	 	</div>
	 	
	</div>
</body>
</html>