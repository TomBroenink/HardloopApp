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

		var registerJson;

		$scope.submit = function() {
			createJson();
			console.log(jsonFile);
		}

		var jsonFile;
		function createJson() {
			registerJson = {
				"firstName" : $scope.firstName,
				"lastName" : $scope.lastName,
				"phoneNumber" : $scope.phoneNumber,
				"username" : $scope.username,
				"password" : $scope.password,
				"profile" : $scope.profile
			}
			console.log(registerJson);
		}
		$scope.fillProfile1 = function() {
			$scope.profile = "Informatie over basis zorgprofiel 1";
		}
		$scope.fillProfile2 = function() {
			$scope.profile = "Informatie over basis zorgprofiel 2";
		}
		$scope.fillProfile3 = function() {
			$scope.profile = "Informatie over basis zorgprofiel 3";
		}
		//$scope.json = jsonFile;
		
	
	});