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
		
		$(".upload").hide();
		$scope.docList = {};
		$scope.docListDisable = {};
		$scope.selection = [];
		$listToUpload ="";
		$scope.uploadNewFiles = function(){
			$(".uploadFiles").show();
			$(".upload").show();
			$(".enableDocs").hide();
			$(".disableDocs").hide();
			$http.get("<%= contextPath %>/services/home-page/getFileNames").success(function(data) {
				$scope.listOfFiles = data;
			});
		}
		
			$scope.uploadFileToDB = function(){	
				
				angular.forEach($scope.selection, function(value, key) {
						$listToUpload+=value;
						$listToUpload+=",";
					});
					
				$http.get("<%= contextPath %>/services/home-page/upload?files=" + $listToUpload).success(function(data) {
					if (data == true){
						$scope.selection = [];
						$listToUpload = "";
						$http.get("<%= contextPath %>/services/home-page/getFileNames").success(function(data) {
							$scope.listOfFiles = data;
						});
						$('input:checkbox').removeAttr('checked');
					}
				});
			}
		
			// toggle selection for a given employee by name
			  $scope.toggleSelection = function toggleSelection(file) {
			     var idx = $scope.selection.indexOf(file);
			     // is currently selected
			     if (idx > -1) {
			       $scope.selection.splice(idx, 1);
			     }

			     // is newly selected
			     else {
			       $scope.selection.push(file);
			     }
			   };
			   
		$scope.showDisableDocs = function(){
			$(".enableDocs").hide();
			$(".uploadFiles").hide();
			$(".upload").hide();
			$(".disableDocs").show();
			$http.get("<%= contextPath %>/services/home-page/getDocList").success(function(data) {
				$scope.docListDisable = data;
			});
		}
		
		$scope.disableDoc = function(index,doc){
			$http.get("<%= contextPath %>/services/home-page/disableDoc?docId=" + doc).success(function() {
				$scope.docListDisable.splice(index, 1);
			});
		}
		
		$scope.showEnableDocs = function(){
			$(".disableDocs").hide();
			$(".uploadFiles").hide();
			$(".enableDocs").show();
			$(".upload").hide();
			$http.get("<%= contextPath %>/services/home-page/getdocListEnable").success(function(data) {
				$scope.docList = data;
			});
		}
		$scope.enableDoc = function(index,doc){
			$http.get("<%= contextPath %>/services/home-page/enableDoc?docId=" + doc).success(function() {
				$scope.docList.splice(index, 1)
			});
		}
		
	});
	
	
</script>
<body ng-controller="LoginPageController">
	<div class="wrapper">

		<div class="loginWrapper">
			<h1 class="animated  bounce">Admin</h1>
		<button type="button" class="btn btn-default files  animated swing" ng-click="showDisableDocs()">Disable documents</button>
			<button type="button" class="btn btn-default files  animated swing" ng-click="showEnableDocs()">Enable documents</button>
			<button type="button" class="btn btn-default files  animated swing" ng-click="uploadNewFiles()">Upload new files</button>
			<div ng-repeat="file in listOfFiles" class="uploadFiles">
			 	<label>
				 	<input id="{{file}}" type="checkbox" value="{{file}}" ng-checked="selection.indexOf(file) > -1" ng-click="toggleSelection(file)" />
				 	{{file}}
			 	</label>
			</div>
			
			<button type="button" class="btn btn-info upload" ng-click="uploadFileToDB()">Upload</button>
			<div ng-repeat="doc in docListDisable"  class="disableDocs">
			 	<label><input type="radio" name="optradio" ng-click="disableDoc($index,doc.id)">{{doc.link}}</label>
			</div>
			
			<div ng-repeat="doc in docList" class="enableDocs">
			 	<label><input type="radio" name="optradio" ng-click="enableDoc($index,doc.id)">{{doc.link}}</label>
			</div>
			<a href="<%= contextPath %>/services/home-page/home"><button type="button" id ="backToSearchIndex" class="btn btn-default  animated swing" ng-click ="returnToHome()" >back</button>
			</a>
			
	 	
	</div>
</body>
</html>