package hearts;

import java.util.ArrayList;

import fr.avianey.mcts4j.sample.SampleRunner;

/*
 * This file is part of minimax4j.
 * <https://github.com/avianey/minimax4j>
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
 * Run a game between two HeartsIA opponent...
 * 
 * original code @author antoine vianey
 * modified by @author ameliaarcher and @author nola gordon
 */
public class HeartsRunner extends SampleRunner<HeartsTransition> implements PlayerInterface {

    public HeartsRunner(Game game) {
    	super(new HeartsIA(game));
    }
    
    public void printRunInfo() {
    	Game simulatedGame = ((HeartsIA) this.getMcts()).getGame();
        System.out.println("Final scores:");
        int[] scores = simulatedGame.getScores();
        for (int s : scores) {
        	System.out.print(s + ", ");
        }

        ArrayList<Card[]> tricks = simulatedGame.getTricks();
	ArrayList<int[]> cardPlayers = simulatedGame.getCardPlayers();
	int[] winners = simulatedGame.getWinners();
		
	System.out.println("");
        for (int t = 0; t < tricks.size(); t++) {
	    System.out.println("Trick " + t + ": ");
	    System.out.println("Scores: ");
	    for (int i = 0; i < 4; i++) {
	    }
	    for (int i = 0; i < 4; i++) {
            	if (tricks.get(t)[i] != null) {
		    System.out.println("Player " + cardPlayers.get(t)[i] + " played the " + tricks.get(t)[i]);
            	}
	    }
	    System.out.println("Player " + winners[t] + " won this trick");
        }
        System.out.println("Winner: " + simulatedGame.lowestScorePlayer() );

    }
    
    public Card playTurn(Game game) {
	//System.out.println("calling playTurn method");
	
	Game gameBaby = game.clone();
	int turn = gameBaby.turn;
	//	gameBaby.printHands();
	//	System.out.println("current player is " + game.getCurrentPlayer());


	HeartsRunner runSearch = new HeartsRunner(gameBaby);
	//	System.out.println("Checkpt 1");	
	runSearch.run(System.currentTimeMillis(), 100);
	//	System.out.println("checkpt 2");

    	Card playedCard = ((HeartsIA)runSearch.getMcts()).getGame().getTricks().get(turn/4)[turn % 4];

	// update the player
	game.next();

	return playedCard;

    }
    
   /* public static void main(String[] args) {
    	Game game = new Game();
        HeartsRunner runner = new HeartsRunner(game);
        runner.run(System.currentTimeMillis(), 10000);
        runner.printRunInfo();
    }*/
    
}
