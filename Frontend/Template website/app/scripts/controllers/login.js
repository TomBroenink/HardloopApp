'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('LoginCtrl', function($scope, $location, $http) {
    $scope.submit = function() {
		var username = $scope.username;
		var password = $scope.password;
		var userObject = {};
		if (!username) {
			return alert('Gebruikersnaam is leeg!');
		}
		if (!password) {
			return alert('Wachtwoord is leeg!');
		}
		userObject = {"username" : username, "password" : password};
		$http({
			method:'post',
			url:'http://localhost:9002/login',
			data: userObject,
			headers: {'Content-Type': 'application/json'}
		})
		.then(function(response) {
			if (response.status == 200) {
				var user = response.data;
				localStorage.setItem('firstName', user.firstName);
				localStorage.setItem('lastName', user.lastName);
				localStorage.setItem('accessLevel', user.accessLevel);
				localStorage.setItem('monitorId', user.monitorId);
				localStorage.setItem('personId', user.personId);
				localStorage.setItem('username', user.username);
				$location.path('/dashboard');
				return false;
			}
		});
	}
  });