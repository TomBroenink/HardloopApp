'use strict';

/**
 * @ngdoc overview
 * @name yapp
 * @description
 * # yapp
 *
 * Main module of the application.
 */
angular
	.module('yapp', [
		'ui.router',
		'ngAnimate',
	])
	.config(function($stateProvider, $urlRouterProvider) {
		var userLoggedIn;
		if (localStorage.length == 0) {
			userLoggedIn = false;
		} else {
			userLoggedIn = true;
		}
 		$urlRouterProvider.when('/dashboard', '/dashboard/overview');
		$urlRouterProvider.otherwise('/login');
		$stateProvider
			.state('base', {
				abstract: true,
				url: '',
				templateUrl: 'views/base.html'
			})
			.state('login', {
				url: '/login',
				parent: 'base',
				templateUrl: 'views/login.html',
				controller: 'LoginCtrl'
			})
			.state('dashboard', {
				url: '/dashboard',
				parent: 'base',
				templateUrl: 'views/dashboard.html',
				controller: 'DashboardCtrl'
			})
			.state('overview', {
				url: '/overview',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/overview.html'
			})
			.state('mydetails', {
				url: '/mydetails',
				parent: 'dashboard', 
				templateUrl: 'views/dashboard/mydetails.html',
				controller: 'MyDetailsCtrl'
			})
			.state('editdetails', {
				url: '/editdetails',
				parent: 'dashboard', 
				templateUrl: 'views/dashboard/editdetails.html',
				controller: 'EditDetailsCtrl'
			})
			.state('myclients', {
				url: '/myclients',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/myclients.html',
				controller: 'MyClientsCtrl'
			})
			.state('client', {
				url: '/clients/:id',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/client.html',
				controller: 'ClientCtrl'
			})
			.state('newclient', {
				url: '/newclient',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/newclient.html',
				controller: 'NewClientCtrl'
			})
			.state('routes', {
				url: '/routes',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/routes.html',
				controller: 'RoutesCtrl'
			})
			.state('register', {
				url: '/register',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/register.html',
				controller: 'RegisterCtrl'

			})
			.state('404', {
				url: '/404',
				parent: 'base',
				templateUrl: '/404.html'
			})
			.state('logout', {
				url: '/logout',
				template: '',
				controller: 'LogoutCtrl'
			});
	});