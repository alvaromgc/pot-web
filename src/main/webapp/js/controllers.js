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
 */
function MembersCtrl($scope, $http, Members, GameService) {

	$scope.inicio = 1667;
	$scope.fim = 0;
	
	$scope.chartOptions = {
	        responsive: true,
	        maintainAspectRatio: false,
	        animation: false,
	        //Number - Width of the grid lines
	        scaleGridLineWidth : 1,
	        //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
	        pointHitDetectionRadius : 2
	    };
	
    // Define a refresh function, that updates the data from the REST service
    $scope.refresh = function() {
        $scope.members = Members.query();
        GameService.getAllGames().success(function(response){
        	$scope.allGames = response;
        });
        GameService.getNumerosOcorrencias().success(function(response){
        	$scope.numerosOcorrencia = response;
        });
        
        limparGraficoMedia();
        limparGraficoOcorrenciaMaior();
        limparGraficoOcorrenciaMenor();
    };
    
    var limparGraficoMedia = function(){
    	$scope.medDesv = {};
        $scope.labels = {};
        $scope.series = [];
        $scope.data = null;
    };
    
    var limparGraficoOcorrenciaMaior = function(){
    	//$scope.medDesv = {};
        //$scope.labelsOcMaior = {};
        $scope.seriesOcMaior = [];
        $scope.dataOcMaior = null;
    };
    
    var limparGraficoOcorrenciaMenor = function(){
    	//$scope.medDesv = {};
        //$scope.labelsOcMenor = {};
        $scope.seriesOcMenor = [];
        $scope.dataOcMenor = null;
    };

    // Define a reset function, that clears the prototype newMember object, and
    // consequently, the form
    $scope.reset = function() {
        // clear input fields
        $scope.newMember = {};
    };

    // Define a register function, which adds the member using the REST service,
    // and displays any error messages
    $scope.register = function() {
        $scope.successMessages = '';
        $scope.errorMessages = '';
        $scope.errors = {};

        Members.save($scope.newMember, function(data) {

            // mark success on the registration form
            $scope.successMessages = [ 'Member Registered' ];

            // Update the list of members
            $scope.refresh();

            // Clear the form
            $scope.reset();
        }, function(result) {
            if ((result.status == 409) || (result.status == 400)) {
                $scope.errors = result.data;
            } else {
                $scope.errorMessages = [ 'Unknown  server error' ];
            }
            $scope.$apply();
        });

    };

    // Call the refresh() function, to populate the list of members
    $scope.refresh();

    // Initialize newMember here to prevent Angular from sending a request
    // without a proper Content-Type.
    $scope.reset();

    // Set the default orderBy to the name property
    $scope.orderBy = 'name';
    
    
    
    $scope.geraGraficoMedia = function(){
    	limparGraficoOcorrenciaMaior();
    	limparGraficoOcorrenciaMenor();
    	GameService.getMediaDesv($scope.inicio,0).success(function(response) {
    		$scope.medDesv = response;
    		$scope.concursos = [];
	        for(var i = $scope.inicio; i <= $scope.allGames.length; i++){
	        	$scope.concursos.push(i.toString());
	        }
		}).then(function(){
			$scope.labels = $scope.concursos;
	        $scope.series = ['Média', 'Desvio'];
	        $scope.data = [
	          $scope.medDesv.media,
	          $scope.medDesv.desvio
	        ];
		});
       
        //console.log('Mediadesv:'+JSON.stringify($scope.medDesv.media));
    	
    };
    
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
    
    $scope.geraGraficoOcorrencias = function(inicio, fim){
    	limparGraficoMedia();
    	$scope.concursos = [];
    	GameService.getHistoricoOcorrencias(inicio, fim).success(function(response) {
    		var resultadosOcorrencia = response;
    		
	        $scope.ocMaior = [];
	        $scope.ocMedioMaior = [];
	        $scope.ocMedioMenor = [];
	        $scope.ocMenor= [];
    		for(var i = inicio; i <= $scope.allGames.length; i++){
	        	$scope.concursos.push(i.toString());
	        }
    		var i = 0;
	        resultadosOcorrencia.forEach(function(res) {
	        	$scope.ocMaior[i] = res.ocorrencias[0];
	        	$scope.ocMedioMaior[i] = res.ocorrencias[1];
	        	$scope.ocMedioMenor[i] = res.ocorrencias[2];
	        	$scope.ocMenor[i] = res.ocorrencias[3];
	        	i++;
			});
		}).then(function(){
			var calcfim = 0;
			if(fim == 0){
				calcfim = $scope.concursos.length;
			}else{
				calcfim = fim;
			}
			
			$scope.labelsOc = $scope.concursos.slice(0, calcfim);
			$scope.seriesOcMaior = ['Maior Ocorrencia', 'Media Maior'];
	        $scope.dataOcMaior = [
	          $scope.ocMaior,
	          $scope.ocMedioMaior
	        ];
	        
	        //$scope.labels = $scope.concursos;
	        $scope.seriesOcMenor = ['Média Menor', 'Menor Ocorrencia'];
	        $scope.dataOcMenor = [
	          $scope.ocMedioMenor,
	          $scope.ocMenor
	        ];
	        
	        console.log("result: "+JSON.stringify($scope.labelsOc));
	        console.log("result: "+JSON.stringify($scope.concursos));
		});
       
        //console.log('Mediadesv:'+JSON.stringify($scope.medDesv.media));
    	
    };
    
}