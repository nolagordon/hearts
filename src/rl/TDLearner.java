/*
 TD learner
trains a perceptron to produce rewards for a variety of states

Source code adapted from Tim Eden, Anthony Knittel, and Raphael van Uffelen
See original code at: 
http://www.cse.unsw.edu.au/~cs9417ml/RL1/index.html
 */
package rl;
import hearts.Game;
import java.util.ArrayList;

public class TDLearner {

    // stores the perceptron
    Perceptron learner;
    // the learning factor (i think, gonna have this represent lambda???)
    public static final double LF = 0.75;

    double decay = 0.5;

    public TDLearner(int numFeatures) {
	learner = new Perceptron(numFeatures, LF);

	int gameCount = 0;
	// run multiple times: receive a game state and train on it
	// for now train on 500 games :)
	while(gameCount < 500) {

	    // TODO: go up to the game class and somehow implement this method
	    Game game = new Game();
	    ArrayList<GameState> hist = new ArrayList<GameState>();//game.simulate(); 
	    // we'll need to add some method perhaps to the game class to simulate a game
	    // and return an array of game states 

	    double[] stateVals = new double[hist.size()];
	    for (int i = hist.size() - 1; i >= 0; i--) {	    
		GameState s = hist.get(i);
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

	// given a list of actions (i.e. a list of states you can transition to)
	// use a greedy selection policy with some exploration to select the next move
	public int selectAction(ArrayList<GameState> states) {
	    int selectedAction = 0;
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
	    if (allSame) {
		selectedAction
	    }
	    
	    return selectedAction;
	    
	}

    }

 
   
}