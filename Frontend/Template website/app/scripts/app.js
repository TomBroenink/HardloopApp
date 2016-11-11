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
		'ngAnimate'
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
				parent: 'reports', // dit nog aanpassen
				templateUrl: 'views/dashboard/mydetails.html',
				controller: 'MyDetailsCtrl'
			})
			.state('myclients', {
				url: '/myclients',
				parent: 'reports', // dit nog aanpassen
				templateUrl: 'views/dashboard/myclients.html',
				controller: 'MyDetailsCtrl'
			})
			.state('client', {
				url: '/:client',
				parent: 'myclients',
				templateUrl: 'views/dashboard/client.html',
				controller: 'ClientCtrl'
			})
			.state('reports', {
				url: '/reports',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/reports.html'
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