To run:
cd src
javac fr/avianey/mcts4j/*.java fr/avianey/mcts4j/sample/*.java hearts/*.java rl/*.java
java hearts.MasterRunner

Note:
As of 12/16/2016, our TD learner is still having issues. The update method for the weights function seems to have some issue, as weight values end up growing exponentially large. 