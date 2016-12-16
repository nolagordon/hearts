package hearts;

import java.util.ArrayList;

import fr.avianey.mcts4j.DefaultNode;
import fr.avianey.mcts4j.Node;
import fr.avianey.mcts4j.UCT;

/**
  * @author ameliaarcher
  * @author nola gordon
  * 
  */

import rl.TDLearner;

public class MasterRunner {
	
	// list strategies
	public final int UCT = 0;
	public final int RAND = 1;
	public final int TRICK = 2;
	public final int TDLEARN = 3;
	public final int HUMAN = 4;
	
	public final int PLAYERS = 4;
	
	Game game;
	ArrayList<PlayerInterface> players;
	
    public MasterRunner(int player0Type, int player1Type, int player2Type, int player3Type, PlayerInterface tdlearner) {
		
		game = new Game();

		players = new ArrayList<PlayerInterface>(); 
		
		players.add(makePlayer(player0Type));
		players.add(makePlayer(player1Type));
		players.add(makePlayer(player2Type));
		players.add(makePlayer(player3Type));

		int playerNum;
		while (game.getTurn() < 52) {
			playerNum = game.getCurrentPlayer();
			//System.out.println("Game turn = " + game.getTurn()+ "\nPlayer " + playerNum + " is choosing a move...");
			Card toPlay = players.get(playerNum).playTurn(game);
			game.playCard(new HeartsTransition(toPlay, playerNum));
			System.out.println("Player " + playerNum + " played " + toPlay.toString());
			if (game.getHistory( playerNum, (game.getTurn() - 2)/4) == null) { 
			    System.out.println("Didn't update history for player " + playerNum + " for trick " + ((game.getTurn() - 2)/4)); 
			    System.exit(0);
			}
		}
		
		System.out.println("Game over. Player " + game.lowestScorePlayer() + " won!");

		// determine whether we are in a simulation
		if (tdlearner != null) {
		    for (int i = 0; i < 4; i++) {
			((TDLearner) tdlearner).train(game.getHistory(i));
		    }
		}
	}
	
	private PlayerInterface makePlayer(int playerType) {
		PlayerInterface player = new HeartsRunner(game);
		switch(playerType) {
			case UCT: player = new HeartsRunner(game);
				break;
			case RAND: 
				break;
			case TRICK:
				break;
			case TDLEARN:
			    // note: for now we are doing 7 features
			    player = new TDLearner(7, false, false);
			    // simulate 100 games to train our perceptron on
			    for (int i = 0; i < 100; i++) {
				new MasterRunner(0,0,0,0,player);
			    }
				break;
			case HUMAN:
				break;
		}
		return player;
	}
    
	public static void main(String[] args) {
	    MasterRunner master = new MasterRunner(3, 0, 0, 0, null);
	}
	
}
