package connect_4;

import java.util.ArrayList;
import java.util.Scanner;

public class Runner {
	public static void main(String[] args) {
		// setup
		Scanner scan = new Scanner(System.in);
		int m,n,o;
		final long tlim;
		System.out.println("How many rows?");
		m = Integer.parseInt(scan.next());
		System.out.println("How many columns?");
		n = Integer.parseInt(scan.next());
		System.out.println("How many in a row to win?");
		o = Integer.parseInt(scan.next());
		System.out.println("How many ms per move?");
		tlim = Long.parseLong(scan.next());
		Board gameBoard = new Board(m,n);
		gameBoard.printBoard();
		int firstMove = 1; // 1 for player/opponent, 2 for computer
		Boolean gameOver = false;
		
		// alternate between player and computer
		if (firstMove == 1) { // player first
			while (gameOver == false) {
				gameBoard = playerTurn(gameBoard, scan);
				gameBoard.printBoard();
				int cv = checkVictory(gameBoard, o);
				if (cv > 0) {
					gameOver = true;
					System.out.println("Player " + cv + " wins!");
					break;
				}
				if (cv == 0) {
					gameOver = true;
					System.out.println("It's a tie!");
					break;
				}
				gameBoard = computerTurn(gameBoard, tlim, o);
				gameBoard.printBoard();
				cv = checkVictory(gameBoard, o);
				if (cv > 0) {
					gameOver = true;
					System.out.println("Player " + cv + " wins!");
					break;
				}
				if (cv == 0) {
					gameOver = true;
					System.out.println("It's a tie!");
					break;
				}
			}
		}
		if (firstMove == 2) { // computer first
			while (gameOver == false) {
				gameBoard = computerTurn(gameBoard, tlim, o);
				gameBoard.printBoard();
				int cv = checkVictory(gameBoard, o);
				if (cv > 0) {
					gameOver = true;
					System.out.println("Player " + cv + " wins!");
					break;
				}
				if (cv == 0) {
					gameOver = true;
					System.out.println("It's a tie!");
					break;
				}
				gameBoard = playerTurn(gameBoard, scan);
				gameBoard.printBoard();
				cv = checkVictory(gameBoard, o);
				if (cv > 0) {
					gameOver = true;
					System.out.println("Player " + cv + " wins!");
					break;
				}
				if (cv == 0) {
					gameOver = true;
					System.out.println("It's a tie!");
					break;
				}
			}
		}
		scan.close();
	}
	
	// returns player number if player has r in a row; 0 if none
	static int checkVictory(Board board, int inArow) {
		int[][] b = board.getBoard();
		int numEmpty = 0;
		for (int i=0; i<board.getM(); i++) {
			for (int j=0; j<board.getN(); j++) {
				if (b[i][j] != 0) {
					int player = b[i][j];
					int connected = 1;
					// check down, right, down-right, down-left
					
					// down, so increment i (row) from current position
					for (int k=1; k<inArow; k++) {
						if (i+k < board.getM()) {
							if (b[i+k][j] == player) {
								connected++;
								if (connected == inArow) {
									return player;
								}
							}
							else
								k = inArow;
						}
					}
					
					connected = 1;
					
					// right, so increment j (column) from current position
					for (int k=1; k<inArow; k++) {
						if (j+k < board.getN()) {
							if (b[i][j+k] == player) {
								connected++;
								if (connected == inArow) {
									return player;
								}
							}
							else
								k = inArow;
						}
					}
					
					connected = 1;
					
					// down-right, so increment i (row) and k (column) from current position
					for (int k=1; k<inArow; k++) {
						if (i+k < board.getM() && j+k < board.getN()) {
							if (b[i+k][j+k] == player) {
								connected++;
								if (connected == inArow) {
									return player;
								}
							}
							else
								k = inArow;
						}
					}
					
					connected = 1;
					
					// down-left, so decrement i (row) and j (column) from current position
					for (int k=1; k<inArow; k++) {
						if (i+k > board.getM() && j-k > 0) {
							if (b[i-k][j-k] == player) {
								connected++;
								if (connected == inArow) {
									return player;
								}
							}
							else
								k = inArow;
						}
					}
				}
				else {
					numEmpty++;
				}
			}
		}
		if (numEmpty == 0) {
			return 0;
		}
		return -1;
	}
	
	// currently allows player to place in a full column, but it acts as a wasted turn
	static Board playerTurn(Board board, Scanner scan) {
		System.out.println("Enter a column: 0" + "-" + (board.getN()-1));
		int column = Integer.parseInt(scan.next());
		while (column > board.getN()){
			System.out.println("Please enter a VALID column: ");
			column = Integer.parseInt(scan.next());
		}
		System.out.println("Player has dropped a piece in column " + column);
		return new Board(board, column, 1);
	}
	
	static Board computerTurn(Board board, long timeLimit, int inarow) {
		Board compMove = makeMove(board, timeLimit, inarow).copy();
		System.out.println("Computer has placed a piece");
		return compMove;
	}
	
	// creates a tree for a given amount of time (ms), returns a Board (move)
	static Board makeMove(Board gameBoard, long timeLimit, int inarow) {
		// copy board
		Board board = gameBoard.copy();
		
		long startingTime = System.nanoTime();
		long currentTime = startingTime;
		Tree<Tree.Node> moves = new Tree<Tree.Node>(board);
		moves.getRoot().setPlayer(1);
		
		// while time passed is less than time limit, generate children
		ArrayList<Tree.Node> currentLeaves = moves.getRoot().generateChildren();
		moves.getRoot().getChildren().addAll(currentLeaves);
		ArrayList<Tree.Node> futureLeaves = new ArrayList<Tree.Node>();
		
		// generate boards for certain amount of time limit
		System.out.println("Generating tree...");
		while (currentTime - startingTime < timeLimit*1000000) {
			for (Tree.Node leaf : currentLeaves) {
				for (Tree.Node leafChild : leaf.generateChildren()) {
					leafChild.setParent(leaf);
					leaf.addChild(leafChild);
					futureLeaves.add(leafChild);
				}
			}
			currentLeaves.clear();
			for (Tree.Node leaf : futureLeaves) {
				currentLeaves.add(leaf);
			}
			futureLeaves.clear();
			currentTime = System.nanoTime();
		}
//		for (int i=0; i<5; i++) {
//			for (Tree.Node leaf : currentLeaves) {
//				leaf.setChildren(leaf.generateChildren());
//				futureLeaves.addAll(leaf.getChildren());
//			}
//			currentLeaves.clear();
//			currentLeaves.addAll(futureLeaves);
//			futureLeaves.clear();
//		}
		
		// score leaves
		System.out.println("Scoring leaves...");
		boolean atRoot = false;
		for (Tree.Node n : currentLeaves) {
			n.generateScore(inarow);
		}
		if (currentLeaves.get(0).getParent() == null) {
			atRoot = true;
		}
		
		// use leaves to min max parents, using currentLeaves even though they're not leaves anymore
		System.out.println("Computing minimax...");
		ArrayList<Tree.Node> futureNodes = new ArrayList<Tree.Node>(); 
		while (atRoot == false) {
			for (Tree.Node n : currentLeaves) {
				// if player is 2, use max, else use min
				if (n.getParent().getPlayer() == 2) {
					if (n.getScore() > n.getParent().getScore()) {
						n.getParent().setScore(n.getScore());
					}
				}
				else {
					if (n.getScore() < n.getParent().getScore()) {
						n.getParent().setScore(n.getScore());
					}
				}
				futureNodes.add(n.getParent());
			}
			currentLeaves.clear();
			for (Tree.Node no : futureNodes) {
				currentLeaves.add(no);
			}
			futureNodes.clear();
			if (currentLeaves.get(0).getParent() == null) {
				atRoot = true;
			}
		}
		
		// select a board (go to highest child)
		System.out.println("Choosing move...");
		Tree.Node r = moves.getRoot();
		Tree.Node bestMove = r.getChildren().get(0);
		for (Tree.Node m : r.getChildren()) {
			if (m.getScore() > bestMove.getScore()) {
				bestMove = m;
			}
		}
		System.out.println("Best state value:" + bestMove.getScore() + " points...");
		return bestMove.getBoard();
	}
}
