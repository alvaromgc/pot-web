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
    .config(['$sceDelegateProvider', function($sceDelegateProvider) {
	  // We must whitelist the JSONP endpoint that we are using to show that we trust it
	  $sceDelegateProvider.resourceUrlWhitelist([
	    'self',
	    'https://www.lotodicas.com.br/**'
	  ]);
	}])
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
        
        factory.getSurpresas = function (guess) {
            return $http.post('rest/surpresa/surpresinhas',guess);
        };
        
        factory.getNumerosOcorrencias = function () {
            return $http.get('rest/surpresa/numerosOcorrencia',{});
        };
        
        factory.register = function (game) {
            return $http.post('rest/surpresa',game);
        };
        
        factory.getGame = function (concurso) {
            return $http.get('rest/surpresa/game',{
            	params : {
					numero : concurso
				}
            });
        };
        
        factory.getWsCaixa = function (concurso) {
        	var token = '37362116091d2facbbc33fecdaf75bcd6e19da4bc06754506009db6152fe39ea';
        	if(concurso != undefined){
        		//return $http.get('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/'+concurso,{});
        		//$resource('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/'+concurso, { format: 'json', jsoncallback: 'JSON_CALLBACK' }, { 'load': { 'method': 'JSONP' } });
        		return $http.get('https://www.lotodicas.com.br/api/v2/mega_sena/results/'+concurso+'?token='+token, { 
        			headers: {
	                'Content-Type': 'application/json' , 
	                'Access-Control-Allow-Origin': 'https://www.lotodicas.com.br/api/v2',
	                'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
	                'Access-Control-Allow-Headers':'X-Requested-With'	
        			}
        		});
        	}else{
        		return $http.get('https://www.lotodicas.com.br/api/v2/mega_sena/results/last?token='+token,{
        			headers: {
	                'Content-Type': 'application/json' , 
	                'Access-Control-Allow-Origin': 'https://www.lotodicas.com.br/api/v2',
	                'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
	                'Access-Control-Allow-Headers':'X-Requested-With'	
        			}
        		});
        		//return $resource('http://wsloterias.azurewebsites.net/api/sorteio/getresultado/1/', {format: 'json'}, { 'load' : { 'method': 'GET', 'Content-Type': undefined } });
        	}
            
        };
        /*
          {"numero":1667,"data":"2015-01-07","sorteio":[51,24,56,7,12,44],"ganhadores":[1,143,7934],"rateio":[5862774.49,13544.91,348.75],
          "acumulado":"nao","valor_acumulado":0,"cidades":[["VILA VELHA","ES"]],"proximo_estimativa":3000000,"proximo_data":"2015-01-10"}
         */
       /*
        {"NumeroConcurso":1771,"Acumulou":true,"EstimativaPremio":195000000.00,"ValorAcumulado":187769207.55,"Data":"2015-12-19","RealizadoEm":"Sorteio realizado em TARUMIRIM/MG  ",
        "DescricaoAcumuladoOutro":"Valor acumulado para o próximo concurso de final cinco (1775)","ValorAcumuladoOutro":14497772.35,"DataProximo":"2015-12-22","ValorAcumuladoEspecial":95412469.00,
        "Arrecadacao":109030383.00,"Sorteios":[{"NumSorteio":1,"Numeros":[38,2,27,28,32,20],"Premios":[{"Faixa":"Sena","NumeroGanhadores":0,"Valor":0.00},{"Faixa":"Quina","NumeroGanhadores":123,"Valor":51896.21},
        {"Faixa":"Quadra","NumeroGanhadores":10330,"Valor":882.75}],"Ganhadores":[]}]} 
        */
        
        return factory;
    }
})(angular);        