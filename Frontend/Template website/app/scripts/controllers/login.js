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
  	console.log($http);
    $scope.submit = function() {
		// Mock data voor test
		var db = {responseCode : 200, username : 'tom_broenink@hotmail.com', password : 'test'}
		
		var username = $scope.username;
		var password = $scope.password;
		var userObject = {};
		if (!username) {
			return alert('Emailadres is leeg!');
		}
		if (!password) {
			return alert('Password is leeg!');
		}
		userObject = {"username" : username, "password" : password};
		$http({
			method:'post',
			url:'http://localhost:9002/login',
			data: userObject,
			headers: {'Content-Type': 'application/json'}
		})
		.then(function(response) {
			console.log(response);
			//$location.path('/dashboard');
			//return false;
		});

		if (db.responseCode == 200) {
			// Sla hier de naam en accesslevel in sessionStorage op
			if (username === db.username) {
				if (password === db.password) {
					alert('Succesvol ingelogd!');
					localStorage.setItem('username', db.username);
					localStorage.setItem('accessLevel', 1);
					$location.path('/dashboard');
					return false;
				}
			} else {
				alert('Verkeerde emailadres');
			}
		} else {
			alert('Fout bij inloggen');
		}
    }
  });