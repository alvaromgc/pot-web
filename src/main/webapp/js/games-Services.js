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
    
    GameService.$inject = ['$http', '$resource'];
    
    function GameService ($http,$resource) {
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
        
        factory.register = function (game) {
            return $http.post('rest/surpresa',game);
        };
        
        factory.getWsCaixa = function (concurso) {
        	if(concurso != undefined){
        		//return $http.get('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/'+concurso,{});
        		//$resource('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/'+concurso, { format: 'json', jsoncallback: 'JSON_CALLBACK' }, { 'load': { 'method': 'JSONP' } });
        		return $http.get('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/'+concurso, { 
        			headers: {
	                'Content-Type': 'application/json' , 
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
	                'Access-Control-Allow-Headers':'X-Requested-With'	
        			}
        		});
        	}else{
        		return $http.get('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/',{
        			headers: {
	                'Content-Type': 'application/json' , 
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
	                'Access-Control-Allow-Headers':'X-Requested-With'	
        			}
        		});
        		//return $resource('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/', {format: 'json'}, { 'load' : { 'method': 'GET', 'Content-Type': undefined } });
        	}
            
        };
       /*
        {"NumeroConcurso":1771,"Acumulou":true,"EstimativaPremio":195000000.00,"ValorAcumulado":187769207.55,"Data":"2015-12-19","RealizadoEm":"Sorteio realizado em TARUMIRIM/MG  ",
        "DescricaoAcumuladoOutro":"Valor acumulado para o próximo concurso de final cinco (1775)","ValorAcumuladoOutro":14497772.35,"DataProximo":"2015-12-22","ValorAcumuladoEspecial":95412469.00,
        "Arrecadacao":109030383.00,"Sorteios":[{"NumSorteio":1,"Numeros":[38,2,27,28,32,20],"Premios":[{"Faixa":"Sena","NumeroGanhadores":0,"Valor":0.00},{"Faixa":"Quina","NumeroGanhadores":123,"Valor":51896.21},
        {"Faixa":"Quadra","NumeroGanhadores":10330,"Valor":882.75}],"Ganhadores":[]}]} 
        */
        
        return factory;
    }
})(angular);        