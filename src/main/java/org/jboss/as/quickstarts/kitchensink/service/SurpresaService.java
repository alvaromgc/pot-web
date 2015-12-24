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
package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.data.GameRepository;
import org.jboss.as.quickstarts.kitchensink.model.Game;
import org.jboss.as.quickstarts.kitchensink.model.Guess;
import org.jboss.as.quickstarts.kitchensink.model.MediaDesv;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.model.Ocorrencia;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class SurpresaService {

    @Inject
    private Logger log;

    @Inject
    private GameRepository gameRepository;
    
    @Inject
    private List<Game> allGames;
    
    @Inject
    private SurpresaEngine se;

    public MediaDesv listaMediaDevP(Integer begin, Integer end) throws Exception {
        //log.info("Calculating " + game);
       List<Game> games = gameRepository.findByRange(begin, end);
       List<List<Integer>> jogos = new ArrayList<>();
       for (Game g : games) {
    	   jogos.add(g.getList());
       }
       MediaDesv medDevp = se.mapaMediaDesvPadrao(jogos);
       return medDevp;
    }
    
    public List<Ocorrencia> listaOcorrencia(Integer begin, Integer end) throws Exception {
        //log.info("Calculating " + game);
       List<Game> games = allGames;
       List<Ocorrencia> ocorrencias = se.getHistoricoOcorrencias(games);
       Integer calcEnd = ocorrencias.size();
       if(end > begin){
    	   calcEnd = end;
       }
       return ocorrencias. subList(begin - 2, calcEnd);
    }
    
    public List<Ocorrencia> listaNumerosOcorrencia() throws Exception {
        //log.info("Calculating " + game);
       List<Game> games = allGames;
       List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
       Map<Integer, Integer> result =  se.analiseOrdemAparicoes(games);
       for (Integer num : result.keySet()) {
    	   Ocorrencia o = new Ocorrencia();
    	   o.setNumero(num);
    	   o.setQuantidade(result.get(num));
    	   o.setClasseAparicao(se.getClasseOcorrencia(result, o.getQuantidade()));
    	   ocorrencias.add(o);
       }       
       
       return ocorrencias;
    }
    
    public List<Game> gerarSurpresas(Guess guess, boolean print){
    	List<Game> surpresas = new ArrayList<Game>();
    	int[] ocorrencias = {guess.getMaiorFrequencia(),guess.getMediaMaiorFrequencia(),guess.getMediaMenorFrequencia(),guess.getMenorFrequencia()};
    	List<Game> games = allGames;
    	surpresas = se.littleSurprise(guess.getMedia(), guess.getDesvio(), guess.getQuantidade(), games, ocorrencias, print);
    	
    	return surpresas;
    }
}
