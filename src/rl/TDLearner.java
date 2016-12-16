/*
 TD learner
trains a perceptron to produce rewards for a variety of states

Source code adapted from Tim Eden, Anthony Knittel, and Raphael van Uffelen
See original code at: 
http://www.cse.unsw.edu.au/~cs9417ml/RL1/index.html
 */
package rl;

import hearts.Game;
import hearts.PlayerInterface;
import hearts.Card;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class TDLearner implements PlayerInterface {

    // stores the perceptron
    Perceptron learner;
    // the learning factor (i think, gonna have this represent lambda???)
    public static final double LF = 0.75;

    double decay = 0.5;

    public TDLearner(int numFeatures, boolean and, boolean or) {
	int totalFeatures = numFeatures;
	if (and) totalFeatures += numFeatures * numFeatures;
	if (or) totalFeatures += numFeatures * numFeatures;
	learner = new Perceptron(numFeatures, LF);

    }

    // train the perceptron on a given history of game states
    public void train(ArrayList<GameState> hist) {

	if (hist.size() == 0) {
	    System.out.println("error: length of history is 0");
	    System.exit(0);
	}
	// stores the value of each state in the history
	double[] stateVals = new double[hist.size()];
	for (int i = hist.size() - 1; i > 0; i--) {	    
	    GameState s = hist.get(i);
	    if (s == null) {
		System.out.println("error: history contains null state at index " + i);
		System.exit(0);
	    }
	    double actualVal = 0;
	    if (s.isTerminal()) {
		actualVal = s.getScore();
	    } else {
		// idk whether this is correct...
		actualVal = ((1 - decay) * s.getScore()) + (decay * stateVals[i-1]);
	    }
	    
	    // compare the expected value of the state calculated by the learner 
	    // and compare it to the actual value
	    // compute error and update the perceptron weights
	    double expectedVal = learner.classify(s.getFeatureArr());
	    learner.update(s.getFeatureArr(), Math.abs(expectedVal - actualVal));
	    stateVals[i] = actualVal;
	}
    }

    public Card playTurn(Game game) {
	// get all possible actions, then evaluate them with selectAction
	Map<GameState, Card> moves = game.getPossibleStates(game.getCurrentPlayer());
	
	// compile an array list of game states from the map
	ArrayList<GameState> states = new ArrayList<GameState>();
	for (GameState state: moves.keySet()) {
	    System.out.println("added to states list");
	    states.add(state);
	}
	int bestMove = selectAction(states);

	// TODO: move this to masterrunner
	// update the player
	game.next();

	// reach back into the map to find the card associated with the best move
	return moves.get(states.get(bestMove));
	
    }

    // given a list of actions (i.e. a list of states you can transition to)
    // use a greedy selection policy with some exploration to select the next move
    public int selectAction(ArrayList<GameState> states) {
	int selectedAction = 0;
	if (states.size() == 1) { return 0; }
	double selectedActionVal = learner.classify(states.get(0).getFeatureArr());
	boolean allSame = true;
	
	// for all actions, run them thru the perceptron to calculate their expected value
	// go for the action with the least value (bc hearts is won by winning as few
	// pts as possible)
	for (int i = 1; i < states.size(); i++) {
	    GameState curState = states.get(i);
	    double curVal = learner.classify(curState.getFeatureArr());
	    
	    if (curVal != selectedActionVal) {
		allSame = false;
	    }
	    
	    if (curVal < selectedActionVal) {
		selectedAction = i;
		selectedActionVal = curVal;
	    }
	}
	
	// if the value of all states are equal, choose a random action
	// TODO: actually implement this
	//	if (allSame) {
	//  selectedAction;
	//}
	
	return selectedAction;
	
    }

}
