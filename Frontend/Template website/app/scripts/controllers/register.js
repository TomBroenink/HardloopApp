'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
	.controller('RegisterCtrl', function($scope) {
		$scope.submit = function() {
			$scope.createJson();
			console.log(jsonFile);
		}
		var jsonFile;
		$scope.createJson = function() {
			jsonFile = {
				"firstName" : $scope.firstName,
				"lastName" : $scope.lastName,
				"phoneNumber" : $scope.phoneNumber,
				"username" : $scope.userName,
				"password" : $scope.passWord,
				"profile" : $scope.profile
			}
		}
		$scope.fillProfile1 = function() {
			$scope.profile = "profile 1 hallo\ntest";
		}
		$scope.fillProfile2 = function() {
			$scope.profile = "rlifvngetgbektbhskitehvbtrkvhsbertkl";
		}
		$scope.fillProfile3 = function() {
			$scope.profile = "nee tom dat hoeft helemaal niet";
		}
		//$scope.json = jsonFile;
		
	
	});