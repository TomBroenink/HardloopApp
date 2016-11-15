'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
	.controller('RegisterMonitorCtrl', function($scope) {
		
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
});