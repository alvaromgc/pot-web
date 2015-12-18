/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 
// Define the REST resource service, allowing us to interact with it as a high level service
angular.module('gamesService', ['ngResource']).
    factory('GameService', function($resource){
    	return {
    	      allGames: $resource('rest/surpresa/resultados', {}, {
    	        query: { method: 'GET', params: {}, isArray: true }
    	      }),
    	      mediaDesv: $resource('rest/surpresa/medDev/', {ini: '@ini', fim: '@fim'}, {
    	        query: { method: 'GET', params: {ini: '@ini', fim: '@fim'}, isArray: false }
    	      })
    	    };
    });

*/

(function (angular, undefined) {
    'use strict';
    
    angular.module('gamesService', ['ngResource'])
    
    .factory('GameService', GameService);
    
    GameService.$inject = ['$http'];
    
    function GameService ($http) {
        var factory = {};
        
        factory.getAllGames = function () {
            return $http.get('rest/surpresa/resultados');
        };
        
        factory.getMediaDesv = function (ini, fim) {
            return $http.get('rest/surpresa/medDev',
            		{
					params : {
						ini : ini,
						fim : fim
					}
        		});
        };
        
        factory.getHistoricoOcorrencias = function (ini, fim) {
            return $http.get('rest/surpresa/historicoOcorrencia',
            		{
					params : {
						ini : ini,
						fim: fim
					}
        		});
        };
        
        factory.getNumerosOcorrencias = function () {
            return $http.get('rest/surpresa/numerosOcorrencia',{});
        };
        
        return factory;
    }
})(angular);        