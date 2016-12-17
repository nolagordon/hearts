/*
Describes a game's state
used for TD learning

note: for any binary attributes,
features have value 1 if they are present and 0 if they are not present in the current state
FEATURES BY INDEX:
0: short clubs (0/1)
1: short spades (0/1)
2: short diamonds (0/1)
3: short hearts (0/1)
4: pts/num of hearts player has taken (0-13)
5: pts/num of hearts opponents have taken (0-13)
6: pts/num of hearts still left to be taken (0-13)
*/

package rl;

import hearts.Hand;
import hearts.Card;
import java.util.ArrayList;

public class GameState {

	int[] scores;
	int playerScore;
	ArrayList<Hand> hands;
	Hand playerHand;
	boolean terminal;

	// store whether we want to and/or the features
	boolean or;
	boolean and;

	public static final int NUM_FEATURES = 7;

	public GameState(int[] scores, int playerScore, ArrayList<Hand> hands, Hand playerHand, boolean terminal,
			boolean or, boolean and) {
		this.scores = scores;
		this.playerScore = playerScore;
		this.hands = hands;
		this.playerHand = playerHand;
		this.terminal = terminal;
		this.and = and;
		this.or = or;
	}

	public int getScore() {
		return playerScore;
	}

	public boolean isTerminal() {
		return terminal;
	}

	// returns an array of doubles that represents the features of this game
	// state
	public double[] getFeatureArr() {
		int totalFeatures = NUM_FEATURES;
		if (or) {
			totalFeatures += NUM_FEATURES * NUM_FEATURES;
		}
		if (and) {
			totalFeatures += NUM_FEATURES * NUM_FEATURES;
		}

		double[] result = new double[totalFeatures];
		// first four features say whether the player is short different suits
		for (int i = 0; i < 4; i++) {
			result[i] = 1;
		}

		// first assume short everything, then go thru cards and fix
		for (Card c : playerHand.getList()) {
			switch (c.getSuit()) {
			case Card.CLUBS:
				result[0] = 0;
				break;
			case Card.SPADES:
				result[1] = 0;
				break;
			case Card.DIAMONDS:
				result[2] = 0;
				break;
			default:
				result[3] = 0;
			}
		}
		// store curFeature to use as an index for results
		int curFeature = 4;

		// the next 3 features record details about the score
		// 1. how many hearts the player has collected
		// 2. how many hearts do the other players have
		// 3. how many hearts have not yet been played
		result[curFeature++] = playerScore;
		double totalScore = 0;
		for (int i : scores) {
			totalScore += i;
		}
		result[curFeature++] = totalScore - playerScore;
		result[curFeature++] = 13 - totalScore;

		// add the results of bitwise and/bitwise or if they are turned on
		if (or) {
			for (int i = 0; i < NUM_FEATURES; i++) {
				for (int j = 0; j < NUM_FEATURES; j++) {
					result[curFeature++] = (int) result[i] | (int) result[j];
				}
			}
		}

		if (and) {
			for (int i = 0; i < NUM_FEATURES; i++) {
				for (int j = 0; j < NUM_FEATURES; j++) {
					result[curFeature++] = (int) result[i] & (int) result[j];
				}
			}
		}

		return result;
	}

}