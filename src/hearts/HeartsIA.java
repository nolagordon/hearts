package hearts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.avianey.mcts4j.DefaultNode;
import fr.avianey.mcts4j.Node;
import fr.avianey.mcts4j.Path;
import fr.avianey.mcts4j.Transition;
import fr.avianey.mcts4j.UCT;

import hearts.Game;

/*
 * This file is part of mcts4j.
 * <https://github.com/avianey/mcts4j>
 *  
 * Copyright (C) 2012 Antoine Vianey
 * 
 * minimax4j is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * minimax4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with minimax4j. If not, see <http://www.gnu.org/licenses/lgpl.html>
 */

/**
 * Simple Hearts IA to showcase the API. 
 * 
 * original code @author antoine vianey
 * modified by @author ameliaarcher and @author nola gordon
 */
public class HeartsIA extends UCT<HeartsTransition, DefaultNode<HeartsTransition>> {
        
    private Game game;
    
    private int currentPlayer;
    private int nextPlayer;
    
    public HeartsIA(Game game) {
    	super();
		if (game.getTurn() == 0) {
			currentPlayer = game.hasTwoOfClubs();
			
		} else {
			currentPlayer = game.getCurrentPlayer();
		}
		for (Hand hand : game.getHands()) {
			hand.print();
		}
		this.game = game;
    }	

    
    //TODO
    // Make new tree with given moves from game already constructed
    public void reset(Game game) {
    	HeartsTransition[] transitions = game.getMctsTransitions();
    	pathToRoot = new Path<HeartsTransition, DefaultNode<HeartsTransition>>(newNode(null, false));
    	for (int turn = 0; turn < game.getTurn(); turn++) {
    		this.pathToRoot = new Path<HeartsTransition, DefaultNode<HeartsTransition>>(pathToRoot.endNode());
    		this.pathToRoot.endNode().makeRoot();
    	} 
    	
    }
    
    @Override
    public boolean isOver() {
        return game.getTurn() >= 52;
    }

    @Override
    public void makeTransition(HeartsTransition transition) {
    	nextPlayer = game.playCard(transition);
        next();
    }

    @Override
    public void unmakeTransition(HeartsTransition transition) {
    	nextPlayer = game.unplayCard(transition.getCard(), transition.getPlayer());
        previous();
    }

    @Override
    public Set<HeartsTransition> getPossibleTransitions() {
    	Set<HeartsTransition> moves = new HashSet<HeartsTransition>();
    	// if it's the first move of the game, must be 2 of clubs
    	if (game.getTurn() == 0) {
	    moves.add(new HeartsTransition(game.getTwoOfClubs(), currentPlayer));
    	} else {
    		moves = game.getPossibleMoves(currentPlayer);
    	}
        return moves;
    }

    @Override
    public void next() {
        currentPlayer = nextPlayer;
    }

    @Override
    public void previous() {
        currentPlayer = nextPlayer;
    }
    
    public String toString() {    	
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

	@Override
	public HeartsTransition simulationTransition(Set<HeartsTransition> possibleTransitions) {
	 	List<HeartsTransition> transitions = new ArrayList<HeartsTransition>(possibleTransitions);
		return transitions.get((int) Math.floor(Math.random() * possibleTransitions.size()));
	}

	@Override
	public HeartsTransition expansionTransition(Set<HeartsTransition> possibleTransitions) {
		List<HeartsTransition> transitions = new ArrayList<HeartsTransition>(possibleTransitions);
		return transitions.get((int) Math.floor(Math.random() * possibleTransitions.size()));
	}

	@Override
	public int getWinner() {
		return game.lowestScorePlayer();
	}

	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Game getGame() {
		return game;
	}

	@Override
	public DefaultNode<HeartsTransition> newNode(Node<HeartsTransition> parent, boolean terminal) {
		return new DefaultNode<HeartsTransition>(parent, terminal);
	}
	
}
