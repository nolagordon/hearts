package hearts;

public interface PlayerInterface {

	// takes in game, returns move to make on current game turn
	abstract Card playTurn(Game game);

}
