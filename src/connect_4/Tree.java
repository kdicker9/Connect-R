package connect_4;

import java.util.ArrayList;
import java.util.List;

public class Tree<Node> {
    private Node root;
    private Node current;

    public Tree(Board board) {
        root = new Node();
        root.board = board.copy();
        current = root;
    }
    
    public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public Node getCurrent() {
		return current;
	}

	public void setCurrent(Node current) {
		this.current = current;
	}

	public static class Node {
        private Board board;
        private Node parent;
        private List<Node> children = new ArrayList<Node>();
        private double min = 100000;
        private double max = 0;
        private double score = 0;
        private int player = 0; // who's turn that node would belong to
        
        public Node() {
        }
        
        public Board getBoard() {
        	return board;
        }
        
        public void setBoard(Board board) {
        	this.board = board;
        }
        
        public Node getParent() {
        	return parent;
        }
        
        public void setParent(Node parent) {
        	this.parent = parent;
        }
        
        public List<Node> getChildren() {
        	return children;
        }
        
        public void setChildren(List<Node> children) {
        	this.children = children;
        }
        
        public void addChild(Node child) {
        	children.add(child);
        }
        
        public double getMin() {
        	return min;
        }
        
        public void setMin(double min) {
        	this.min = min;
        }
        
        public double getMax() {
        	return max;
        }
        
        public void setMax(double max) {
        	this.max = max;
        }
        
        public int getPlayer() {
        	return player;
        }
        
        public void setPlayer(int player) {
        	this.player = player;
        	if (player == 1) {
        		score = 100000000;
        	}
        }
        
        public double getScore() {
        	return score;
        }
        
        public void setScore(double score) {
        	this.score = score;
        }
        
        // generates children for one move ahead of current board
        public ArrayList<Node> generateChildren() {
        	int childPlayer = 0;
    		ArrayList<Node> children = new ArrayList<Node>(); 
    		for (int i=0; i<this.getBoard().getN(); i++) {
    			Node newChild = new Node();
    			if (this.getPlayer() == 1) {
					childPlayer = 2;
				} 
				else {
					childPlayer = 1;
				}
    			newChild.setPlayer(childPlayer);
    			newChild.setBoard(new Board(this.getBoard().copy(), i, childPlayer));
    			newChild.setParent(this);
    			// if a column is full, doesn't create a board in that column
    			if (newChild.getBoard() != this.getBoard())
    				children.add(newChild);
    		}
    		return children;
    	}
        
        public int getDepth() {
        	int depth = 0;
        	Node c = this;
        	while (c.getParent() != null) {
        		depth++;
        		c = c.getParent();
        	}
        	return depth;
        }
        
        // returns height along leftmost branch
        public int getHeight() {
        	int height = 0;
        	Node c = this;
        	while (c.getChildren().size() != 0) {
        		height++;
        		c = c.getChildren().get(0);
        	}
        	return height;
        }
        
        // generates a score for board
        public void generateScore(int inarow) {
        	double p2score = 0;
        	double p1score = 0;
        	
        	// see if we won or lost
        	int cv = Runner.checkVictory(board, inarow);
        	if (cv == 2) {
        		p2score = 1000000;
        	}
        	if (cv == 1) {
        		p2score = -1000000;
        	}
        	
        	int p2touch = 0;
        	int p1touch = 0;
        	int p2open = 0;
        	int p1open = 0;
        	
        	// finds number of touching pieces for each player
        	// if it has another piece to the right, bottom, bleft, or bright, add one
        	for (int i=0; i<board.getM(); i++) {
        		for (int j=0; j<board.getN(); j++) {
        			if (board.getBoard()[i][j] == 1) {
        				// checks to the right
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 1) {
        					p1touch++;
        					if (p1touch >= 2)
        						p1score += 10;
        					if (p1touch >= 3)
        						p1score += 100;
        				}
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 0) {
        					p1open++;
        				}
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 2) {
        					p1touch--;
        				}
        				// checks down
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 1) {
        					p1touch++;
        					if (p1touch >= 2)
        						p1score += 10;
        					if (p1touch >= 3)
        						p1score += 100;
        				}
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 0) {
        					p1open++;
        				}
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 2) {
        					p1touch--;
        				}
        				// checks down-right
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 1) {
        					p1touch++;
        					if (p1touch >= 2)
        						p1score += 10;
        					if (p1touch >= 3)
        						p1score += 100;
        				}
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 0) {
        					p1open++;
        				}
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 2) {
        					p1touch--;
        				}
        				// checks down-left
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 1) {
        					p1touch++;
        					if (p1touch >= 2)
        						p1score += 10;
        					if (p1touch >= 3)
        						p1score += 100;
        				}
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 0) {
        					p1open++;
        				}
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 2) {
        					p1touch--;
        				}
        			}
        			else if (board.getBoard()[i][j] == 2) {
        				// checks to the right
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 2) {
        					p2touch++;
        					if (p2touch >= 2)
        						p2score += 10;
        					if (p2touch >= 3)
        						p2score += 50;
        				}
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 0) {
        					p2open++;
        				}
        				if (j + 1 < board.getN() && board.getBoard()[i][j+1] == 1) {
        					p2touch--;
        				}
        				// checks down
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 2) {
        					p2touch++;
        					if (p2touch >= 2)
        						p2score += 10;
        					if (p2touch >= 3)
        						p2score += 50;
        				}
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 0) {
        					p2open++;
        				}
        				if (i + 1 < board.getM() && board.getBoard()[i+1][j] == 1) {
        					p2touch--;
        				}
        				// checks down-right
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 2) {
        					p2touch++;
        					if (p2touch >= 2)
        						p2score += 10;
        					if (p2touch >= 3)
        						p2score += 50;
        				}
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 0) {
        					p2open++;
        				}
        				if (i + 1 < board.getM() && j + 1 < board.getN() && board.getBoard()[i+1][j+1] == 1) {
        					p2touch--;
        				}
        				// checks down-left
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 2) {
        					p2touch++;
        				}
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 0) {
        					p2open++;
        				}
        				if (i + 1 < board.getM() && j - 1 >= 0 && board.getBoard()[i+1][j-1] == 1) {
        					p2touch--;
        				}
        			}
        		}
        	}
        	
        	p2score += p2touch + p2open;
        	p2score -= p1touch + p1open*2;
        	p2score -= p1score;
        	score = p2score;
        	//System.out.println(p2score);
        }
    }

}
