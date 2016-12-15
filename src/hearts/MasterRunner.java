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
	
	public MasterRunner(int player0Type, int player1Type, int player2Type, int player3Type) {
		
		game = new Game();

		players = new ArrayList<PlayerInterface>(); 
		
		players.add(makePlayer(player0Type));
		players.add(makePlayer(player1Type));
		players.add(makePlayer(player2Type));
		players.add(makePlayer(player3Type));

		int playerNum;
		while (game.getTurn() < 52) {
			playerNum = game.getCurrentPlayer();
			System.out.println("Game turn = " + game.getTurn()+ "\nPlayer " + playerNum + " is choosing a move...");

			Card toPlay = players.get(playerNum).playTurn(game);
			System.out.println(toPlay);
			//find card to be played in player's hand, replace chosen card with actual card from hand
			for (Card c : game.getHands().get(playerNum).getList()) {
				if (Card.cardComparator().compare(c, toPlay) == 0) {
					toPlay = c;
				}
			}
			game.playCard(new HeartsTransition(toPlay, game.getCurrentPlayer()));
			
			System.out.println("Player " + playerNum + " played " + toPlay.toString());
		}
		
		System.out.println("Game over. Player " + game.lowestScorePlayer());
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
				break;
			case HUMAN:
				break;
		}
		return player;
	}
	
	public static void main(String[] args) {
		MasterRunner master = new MasterRunner(0, 0, 0, 0);
	}
	
}
