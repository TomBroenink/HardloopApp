'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('LoginCtrl', function($scope, $location) {
    $scope.submit = function() {
		// Mock data voor test
		var db = {responseCode : 200, email : 'tom_broenink@hotmail.com', password : 'test'}
		
		var email = $scope.Email;
		var password = $scope.Password;
		var userObject = {};
		if (!email) {
			return alert('Emailadres is leeg!');
		}
		if (!password) {
			return alert('Password is leeg!');
		}
		userObject = {email : email, password : password};
		console.log(userObject);
		
		// Stuur userObject met AJAX call naar server
		// if responsecode = OK {
		// $location.path('/dashboard');
		// } else {
		//	alert('username of password incorrect');
		// }
		if (db.responseCode == 200) {
			// Sla hier de naam en accesslevel in sessionStorage op
			if (email == db.email) {
				if (password == db.password) {
					alert('Succesvol ingelogd!');
					$location.path('/dashboard');
					return false;
				}
			} else {
				alert('Verkeerde emailadres');
			}
		} else {
			alert('Fout bij inloggen');
		}
		
		
		
		//console.log (email + ', ' + password)
		//$location.path('/dashboard');
		//return false;
    }

  });