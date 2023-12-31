package io.pigout;

public class PigOut {
	
	private static final int WINNING_SCORE = 100;
	private static final int INITIAL_SCORE = 0;
	private static final int INITIAL_TURN_SCORE = 0;
	private static final int HIGHEST_ROLL_SCORE = 6;
	private static final int PIG_OUT_ROLL_SCORE = 1;
	
	private static final int UPDATE_STATE_ITERATION_AMOUNT = 100;
	private static final double ALWAYS_LOSING_WINNING_PROBABILITY = 0;
	private static final double ALWAYS_WINNING_WINNING_PROBABILITY = 1;
	
	private static final double[][][] winningProbabilities = new double[WINNING_SCORE + 1][WINNING_SCORE + 1][WINNING_SCORE + 1];
	private static final Action[][][] bestActions = new Action[WINNING_SCORE + 1][WINNING_SCORE + 1][WINNING_SCORE + 1];
	
	static {
		for(int i = 0; i < UPDATE_STATE_ITERATION_AMOUNT; i++) {
			for(int score = WINNING_SCORE; score >= INITIAL_SCORE; score--) {
				for(int opponentScore = WINNING_SCORE; opponentScore >= INITIAL_SCORE; opponentScore--) {
					for(int turnScore = WINNING_SCORE; turnScore >= INITIAL_SCORE; turnScore--) {

						updateState(score, opponentScore, turnScore);
					}
				}
			}
		}
	}
	
	private static void updateState(int score, int opponentScore, int turnScore) {
		if(score + turnScore >= WINNING_SCORE) {
			updateWinningProbability(score, opponentScore, turnScore, ALWAYS_WINNING_WINNING_PROBABILITY);
			updateBestAction(score, opponentScore, turnScore, Action.PASS);
		}
		
		if(opponentScore >= WINNING_SCORE) {
			updateWinningProbability(score, opponentScore, turnScore, ALWAYS_LOSING_WINNING_PROBABILITY);
			updateBestAction(score, opponentScore, turnScore, Action.PASS);
		}
		
		double passWinningProbability = 1 - winningProbability(opponentScore, score + turnScore, INITIAL_TURN_SCORE);
		double rollWinningProbability = 0;
		
		for(int rollScore = 1; rollScore <= HIGHEST_ROLL_SCORE; rollScore++) {
			if(rollScore == PIG_OUT_ROLL_SCORE) {
				
				rollWinningProbability += 1 - winningProbability(opponentScore, score, INITIAL_TURN_SCORE);
				
			} else {
				
				rollWinningProbability += winningProbability(score, opponentScore, turnScore + rollScore);
			}
		}
		
		rollWinningProbability /= HIGHEST_ROLL_SCORE;
		
		if(passWinningProbability >= rollWinningProbability) {
			
			updateWinningProbability(score, opponentScore, turnScore, passWinningProbability);
			updateBestAction(score, opponentScore, turnScore, Action.PASS);
			
		} else {
			
			updateWinningProbability(score, opponentScore, turnScore, rollWinningProbability);
			updateBestAction(score, opponentScore, turnScore, Action.ROLL);
		}
	}
	
	public static double winningProbability(int score, int opponentScore, int turnScore) {
		if(score + turnScore >= WINNING_SCORE) return ALWAYS_WINNING_WINNING_PROBABILITY;
		if(opponentScore >= WINNING_SCORE) return ALWAYS_LOSING_WINNING_PROBABILITY;
		
		return winningProbabilities[score][opponentScore][turnScore];
	}
	
	private static void updateWinningProbability(int score, int opponentScore, int turnScore, double winningProbability) {
		winningProbabilities[score][opponentScore][turnScore] = winningProbability;
	}
	
	public static Action bestAction(int score, int opponentScore, int turnScore) {
		if(score + turnScore >= WINNING_SCORE || opponentScore >= WINNING_SCORE) return Action.PASS;
		
		return bestActions[score][opponentScore][turnScore];
	}
	
	private static void updateBestAction(int score, int opponentScore, int turnScore, Action bestAction) {
		bestActions[score][opponentScore][turnScore] = bestAction;
	}
	
	public enum Action {
		
		ROLL, PASS;
		
	}
	
}
