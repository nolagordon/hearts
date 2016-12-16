/*
 Takes number of inputs as arg and creates a percpetron

weights updated with the equation
w = w + (alpha) * error * (phi)
where error is provided by TD learning
and phi, the features of the current state
 */

package rl;

import java.util.ArrayList;

public class Perceptron {
	double[] weights;
	// the learning factor
	double lf;

	public Perceptron(int numInputs, double learningFactor) {
		lf = learningFactor;
		weights = new double[numInputs];

		// initialize all weights to be 0.5
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 0.5;
		}
	}

	// given an array of input values that represent information about
	// the current state, return a double that corresponds to the value
	// of that state
	public double classify(double[] input) {
		// linear regression
		double result = 0;
		for (int i = 0; i < weights.length; i++) {
			result += input[i] * weights[i];
		}
		return (1 / (1 + Math.exp(result)));// / (double) weights.length;
	}

	// updates the weight vector given the input features and the
	// error (which will be calculated through TD learning)
	public void update(double[] input, double error) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] + (lf) * error * input[i];
			if (Double.isNaN(weights[i])) {
			    System.out.println("lf: " + lf);
			    System.out.println("error: " + error);
			    System.out.println("input[i]: " + input[i]);
			    System.exit(0);
			}
		}
	}

	public double[] getWeights() {
		return weights;
	}
}