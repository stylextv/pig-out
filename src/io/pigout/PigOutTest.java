package io.pigout;

import java.util.Random;
import java.util.Scanner;

public class PigOutTest {
	
	private static final int WINNING_SCORE = 100;
	private static final int INITIAL_SCORE = 0;
	private static final int INITIAL_TURN_SCORE = 0;
	private static final int HIGHEST_ROLL_SCORE = 6;
	private static final int PIG_OUT_ROLL_SCORE = 1;
	
	private static final Random RANDOM = new Random();
	private static final Scanner SCANNER = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Let's play a game!");
		
		int score = INITIAL_SCORE;
		int opponentScore = INITIAL_SCORE;
		int turnScore = INITIAL_TURN_SCORE;
		boolean playerTurn = RANDOM.nextBoolean();
		
		while(true) {
			PigOut.Action action;
			if(playerTurn) {
				
				System.out.println("It's your turn. Make a decision by entering either 'roll' or 'pass'.");
				action = readAction();
				
			} else {
				
				action = PigOut.bestAction(score, opponentScore, turnScore);
				double winningProbability = PigOut.winningProbability(score, opponentScore, turnScore);
				String message = String.format("It's my turn and I'm gonna choose %s. I'm %s%% certain, that I'll win.", action, Math.round(winningProbability * 100));
				System.out.println(message);
			}
			
			boolean turnOver = false;
			
			if(action == PigOut.Action.ROLL) {
				
				int rollScore = RANDOM.nextInt(HIGHEST_ROLL_SCORE) + 1;
				String message = String.format("Rolled a %s!", rollScore);
				System.out.println(message);
				
				if(rollScore == PIG_OUT_ROLL_SCORE) {
					
					turnScore = INITIAL_TURN_SCORE;
					turnOver = true;
					
				} else {
					
					turnScore += rollScore;
				}
				
			} else {
				
				turnOver = true;
			}
			
			if(turnOver) {
				
				score += turnScore;
				turnScore = INITIAL_TURN_SCORE;
				
				System.out.println("Great! The turn is now over.");
				int s1 = playerTurn ? score : opponentScore;
				int s2 = playerTurn ? opponentScore : score;
				String message = String.format("You have %s points, while I have %s points.", s1, s2);
				System.out.println(message);
				
				if(score >= WINNING_SCORE) {
					
					if(playerTurn) System.out.println("Oh no! I've lost... GG.");
					else           System.out.println("The game is over! I won.");
					break;
				}
				
				int s = score;
				score = opponentScore;
				opponentScore = s;
				playerTurn = !playerTurn;
			}
		}
		
		SCANNER.close();
	}
	
	private static PigOut.Action readAction() {
		String line = SCANNER.nextLine();
		switch (line) {
			case "roll" -> {
				
				return PigOut.Action.ROLL;
				
			}
			case "pass" -> {
				
				return PigOut.Action.PASS;
				
			}
			default -> {
				
				System.out.println("I cannot understand you. Please try entering your choice again.");
				return readAction();
			}
		}
	}
	
}
