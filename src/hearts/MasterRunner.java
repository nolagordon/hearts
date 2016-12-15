package hearts;

import fr.avianey.mcts4j.DefaultNode;
import fr.avianey.mcts4j.Node;
import fr.avianey.mcts4j.UCT;

/**
  * @author ameliaarcher
  * @author nola gordon
  * 
  * 
  * Notes Dec 14:
  * All players should share the same game
  * Change heartsrunner to go turn by turn
  * 
  */

public class MasterRunner {
	
	// list strategies
	public final int UCT = 0;
	public final int RAND = 1;
	public final int TRICK = 2;
	public final int TDLEARN = 3;
	
	public final int PLAYERS = 4;
	
	Game game;
	
	public MasterRunner() {
		game = new Game();
		//
		for (int trick = 0; trick < 13; trick++) {
			for (int player = 0; player < 4; player++) {
				
			}
		}
	}
}
