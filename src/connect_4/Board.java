package connect_4;

public class Board {
	int m, n;
	int[][] board;
	
	// blank board
	public Board(int m, int n) {
		this.m = m;
		this.n = n;
		board = new int[m][n];
	}
	
	// board from other board
	// player 1 is me, 2 is opponent/computer
	// will put piece in lowest open slot in given column
	public Board(Board oldBoard, int column, int player) {
		m = oldBoard.getBoard().length;
		n = oldBoard.getBoard()[0].length;
		board = oldBoard.getBoard();
		
		// find lowest open row in column
		int row = 0;
		for (int i=0; i<m; i++) {
			if (board[i][column] == 0) {
				row = i;
			}
		}
		if (board[row][column] == 0)
			board[row][column] = player;
	}

	public int getN() {
		return n;
	}

	public int getM() {
		return m;
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public void printBoard() {
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public Board copy() {
		Board newBoard = new Board(m, n);
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				newBoard.getBoard()[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
}
