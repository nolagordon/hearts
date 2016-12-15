package hearts;

public abstract class AbstractPlayer {
	
	// int in range (0,4) that IDs this player in the game
	int playerNum;
	
	public AbstractPlayer(int playerNum) {
		this.playerNum = playerNum;
	}
	
	// takes in game, returns move to make on current game turn
	abstract Card playCard(Game game);
	
}
