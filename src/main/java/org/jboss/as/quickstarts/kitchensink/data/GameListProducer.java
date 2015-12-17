package org.jboss.as.quickstarts.kitchensink.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.kitchensink.model.Game;

@ApplicationScoped
public class GameListProducer {

	@Inject
	private GameRepository gameRepository;
	
	private List<Game> allGames;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Game> getAllGames() {
        return allGames;
    }
    
    public void onGameListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Game game) {
        retrieveAllGames();
    }
    
    @PostConstruct
    public void retrieveAllGames() {
    	allGames = gameRepository.findAll();
    }
}
