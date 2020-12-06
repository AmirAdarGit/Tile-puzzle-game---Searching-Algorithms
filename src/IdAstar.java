import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

 

public class IdAstar {
	Board root;
	Set<Board> hashTable = new HashSet<Board>(); // (L)  Represent an hash table that hold the node that we find but nod extend alredy
	Stack<Board> myStack = new Stack<Board>(); // (H) Represent a stack in order do search in "DFS" approach
	Stack<String> hashTableID = new Stack<String>(); // for not searching the parent board
	String[] solution;
	String chooseWithOpen = "";//input value: if it "with open" -> it will print every iteration the open list
	boolean isPrintOpen;
	
	public IdAstar(Board myBoard,boolean chooseWithOpen){
		this.isPrintOpen = chooseWithOpen;//acording to the user's choose, if print the open linst.
		this.root = myBoard;	
		if(root.checkIfWin()) {
			System.out.println("Num: 1");
			System.out.println("Cost: " + root.G_cost_to_choose);
		}
		else
			this.solution = runIdAstar(myBoard);
	}

	public String[] runIdAstar(Board myBoard){
		int numberOfNodes = 1;
		long startTime = System.nanoTime();
		int limit = myBoard.getHuristicCost(myBoard);
		myBoard.setHuristics(limit);
		
		while(limit < Integer.MAX_VALUE) {
			int minF = Integer.MAX_VALUE;
			hashTable.add(myBoard);
			hashTableID.add(myBoard.getId());
			myStack.add(myBoard);  

			while (!myStack.isEmpty()) {
				Board current = myStack.pop();

				if(current.getOut() == "out") {
					current.setOut("");
					hashTable.remove(current);
					hashTableID.remove(current.getId());
				}
				else {
					current.setOut("out");
					myStack.add(current);
		 			if(isPrintOpen) printOpenList(myStack);

					ArrayList<Board> childrens =  createChildrens(current);//create all the allowd operators fron the node	
					for(Board b : childrens) {
						numberOfNodes++;
						if(b.F_cost_to_choose > limit) {
							minF = Math.min(b.F_cost_to_choose,minF);
							continue;
						}
						if(hashTable.contains(b.getId()) && (b.getOut() == "out")) {//for loop avoidance
							continue;
						}
						if(hashTable.contains(b.getId()) && (b.getOut() != "out")) {//Check if the node that in the hash table have better f(n) (less)
							Board temp = findBoard(b);

							if(temp.F_cost_to_choose > b.F_cost_to_choose) {//we find a better path to an acsist board.
								hashTable.remove(temp);
								hashTableID.remove(temp.getId());
								myStack.remove(b);
								hashTable.add(b);
								hashTableID.add(b.getId());
								continue;
							}
							else {
								continue;//continue to the next operator
							}
						}
						if(b.checkIfWin()) {							  							
 							String inalPath = b.path.substring(0,b.path.length()-1);
 							String numOfNodes = "Num: " + String.valueOf(numberOfNodes);
 							String Cost ="Cost: " + String.valueOf(b.G_cost_to_choose);
 							long endTime = System.nanoTime();
 							String algorithmTime = String.valueOf((endTime-startTime)*Math.pow(10, -9) + " seconds");
 							String[] solution = new String[4];
 							solution[0] = inalPath;
 							solution[1] = numOfNodes;
 							solution[2] = Cost;
 							solution[3] = algorithmTime;
 						 							
							return solution;
						}
						else {
							myStack.add(b);
							hashTable.add(b);
							hashTableID.add(b.getId());
						}

					}

				}

			}
			limit = minF;
		}
 		String[] solution = new String[3];
		solution[0] = "no path";
		solution[1] = "Num: 1";
		solution[2] =  String.valueOf((System.nanoTime()-startTime)*Math.pow(10, -9) + " seconds");
		return solution;
	}



	public Board findBoard(Board b) {
		for (Iterator<Board> it = hashTable.iterator(); it.hasNext(); ) {
			Board f = it.next();
			if (f.getId().equals(b.getId()))
				return f;
		}
		return null;
	}


	public String[] getSolution() {
		return this.solution;
	}

	public void printOpenList(Stack<Board> queue) {
		for(Board b : queue) {
			System.out.print(b.boardId + ", ");
		}
		System.out.println();
	}
	
	public ArrayList<Board> createChildrens(Board node) {
		ArrayList<Board> childrens = new ArrayList<Board>( );
		if(node.y_whiteT < node.mystate[0].length-1 && node.mystate[node.x_whiteT][node.y_whiteT+1].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black			

			Board copyOfNodeL = new Board(node);
			Board L = swapTile(copyOfNodeL,'L');
			if(!(hashTableID.contains(L.getId()))){
				childrens.add(L);
				if(node.mystate[node.x_whiteT][node.y_whiteT+1].color == 'G')
					L.G_cost_to_choose +=1;//green
				else
					L.G_cost_to_choose += 30;//red

				L.H_cost_to_choose = L.getHuristicCost(L);
				L.F_cost_to_choose = L.H_cost_to_choose + L.G_cost_to_choose;
				L.path += node.mystate[node.x_whiteT][node.y_whiteT+1].cellNumber + "L-" ;
			}
		}

		if(node.x_whiteT < node.mystate.length-1 && node.mystate[node.x_whiteT+1][node.y_whiteT].color != 'B'){
			Board copyOfNodeU = new Board(node);
			Board U = swapTile(copyOfNodeU,'U');
			if(!(hashTableID.contains(U.getId()))){
				childrens.add(U);
				if(node.mystate[node.x_whiteT+1][node.y_whiteT].color == 'G')
					U.G_cost_to_choose +=1;//green
				else
					U.G_cost_to_choose += 30;//red
				U.H_cost_to_choose = U.getHuristicCost(U);
				U.F_cost_to_choose = U.H_cost_to_choose + U.G_cost_to_choose;
				U.path += node.mystate[node.x_whiteT+1][node.y_whiteT].cellNumber + "U-" ;
			}
		}
		if(node.y_whiteT > 0 && node.mystate[node.x_whiteT][node.y_whiteT-1].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black
			Board copyOfNodeR = new Board(node);
			Board R = swapTile(copyOfNodeR,'R');
			if(!(hashTableID.contains(R.getId()))){
				childrens.add(R);
				if(node.mystate[node.x_whiteT][node.y_whiteT-1].color == 'G')
					R.G_cost_to_choose +=1;//green
				else
					R.G_cost_to_choose += 30;//red
				R.H_cost_to_choose = R.getHuristicCost(R);
				R.F_cost_to_choose = R.H_cost_to_choose + R.G_cost_to_choose;
				R.path += node.mystate[node.x_whiteT][node.y_whiteT-1].cellNumber + "R-" ;
			}
		}
		if(node.x_whiteT > 0 && node.mystate[node.x_whiteT-1][node.y_whiteT].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black
			Board copyOfNodeD = new Board(node);
			Board D = swapTile(copyOfNodeD,'D');
			if(!(hashTableID.contains(D.getId()))){

				childrens.add(D);
				if(node.mystate[node.x_whiteT-1][node.y_whiteT].color == 'G')
					D.G_cost_to_choose +=1;//green
				else
					D.G_cost_to_choose += 30;//red
				D.H_cost_to_choose = D.getHuristicCost(D);
				D.F_cost_to_choose = D.H_cost_to_choose + D.G_cost_to_choose;
				D.path += node.mystate[node.x_whiteT-1][node.y_whiteT].cellNumber + "D-";
			}
		}
		return childrens;
	}

	public Board swapTile(Board node, char direction) {
		if(direction == 'L') { 
			Cell tempWiteTile = new Cell (); //temporary cell for swapping in order to find the solotion
			tempWiteTile.cellNumber = node.mystate[node.x_whiteT][node.y_whiteT].cellNumber;
			node.mystate[node.x_whiteT][node.y_whiteT] = node.mystate[node.x_whiteT][node.y_whiteT+1];
			node.mystate[node.x_whiteT][node.y_whiteT+1] = tempWiteTile;
			node.findWhiteXY(node.mystate);
			node.searchForBoardId();
			return node;
		}

		else if(direction == 'U') { 
			Cell tempWiteTile = new Cell ();
			tempWiteTile = node.mystate[node.x_whiteT][node.y_whiteT]; //temporary cell for swapping in order to find the solotion
			node.mystate[node.x_whiteT][node.y_whiteT] = node.mystate[node.x_whiteT+1][node.y_whiteT];
			node.mystate[node.x_whiteT+1][node.y_whiteT] = tempWiteTile;
			node.findWhiteXY(node.mystate);
			node.searchForBoardId();
			return node;
		}
		else if(direction == 'R') { 
			Cell tempWiteTile = new Cell ();
			tempWiteTile = node.mystate[node.x_whiteT][node.y_whiteT]; //temporary cell for swapping in order to find the solotion
			node.mystate[node.x_whiteT][node.y_whiteT] = node.mystate[node.x_whiteT][node.y_whiteT-1];
			node.mystate[node.x_whiteT][node.y_whiteT-1] = tempWiteTile;
			node.findWhiteXY(node.mystate);
			node.searchForBoardId();	
			return node;
		}
		else if(direction == 'D') { 
			Cell tempWiteTile = new Cell ();
			tempWiteTile = node.mystate[node.x_whiteT][node.y_whiteT]; //temporary cell for swapping in order to find the solotion
			node.mystate[node.x_whiteT][node.y_whiteT] = node.mystate[node.x_whiteT-1][node.y_whiteT];
			node.mystate[node.x_whiteT-1][node.y_whiteT] = tempWiteTile;
			node.findWhiteXY(node.mystate);
			node.searchForBoardId();
			return node;
		}

		else
			return node;
	}





}
