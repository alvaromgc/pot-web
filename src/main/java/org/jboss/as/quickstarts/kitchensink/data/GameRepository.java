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
package org.jboss.as.quickstarts.kitchensink.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.as.quickstarts.kitchensink.model.Game;
import org.jboss.as.quickstarts.kitchensink.model.Game_;

@ApplicationScoped
public class GameRepository {

    @Inject
    private EntityManager em;

    public Game findById(Integer id) {
        return em.find(Game.class, id);
    }

    public List<Game> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Game> criteria = cb.createQuery(Game.class);
        Root<Game> g = criteria.from(Game.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(g);
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Game> findByRange(Integer begin, Integer end) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Game> criteria = cb.createQuery(Game.class);
        Root<Game> g = criteria.from(Game.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        if(end != null && end != 0){
        	criteria.select(g).where(cb.between(g.get(Game_.concurso), begin, end));
        }else{
        	criteria.select(g).where(cb.ge(g.get(Game_.concurso), begin));
        }
        return em.createQuery(criteria).getResultList();
    }
}
