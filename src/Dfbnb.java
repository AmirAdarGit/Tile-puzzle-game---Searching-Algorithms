import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Dfbnb {

	Board root;
	Set<Board> hashTable = new HashSet<Board>(); // (L)  Represent an hash table that hold the node that we find but nod extend alredy
	Stack<Board> myStack = new Stack<Board>(); // (H) Represent a stack in order do search in "DFS" approach
	Stack<String> hashTableID = new Stack<String>(); // for not searching the parent board
	Set<String> numbersOfCreatedNodes = new HashSet<String>();//in order to count the number of the created nodes.
	Set<String> openListOfNodes = new HashSet<String>();//in order to count the number of the created nodes.

	Board winBoard;
	String[] solution;
	String chooseWithOpen = "";//input value: if it "with open" -> it will print every iteration the open list
	boolean isPrintOpen;

	public Dfbnb(Board myBoard,boolean chooseWithOpen){
		this.root = myBoard;
		this.isPrintOpen = chooseWithOpen;
		if(root.checkIfWin()) {
			System.out.println("Num: 1");
			System.out.println("Cost: " + root.G_cost_to_choose);
		}
		else
			this.solution = runDfbnb(myBoard);
	}

	public String[] runDfbnb(Board myBoard){
		int numberOfNodes = 0;
		long startTime = System.nanoTime();
		int numOfGreenAndRedElents = myBoard.redList.length + myBoard.greenList.length;//The threshold will have the minimum value of Max value in java or the factorial of the non black cells.
		int threshold = Math.min(Integer.MAX_VALUE,factorial(numOfGreenAndRedElents));//for the threshold
		String pathResult = "";
		int limit = myBoard.getHuristicCost(myBoard);
		myBoard.setHuristics(limit);
		myBoard.setFcost(limit);
		hashTable.add(myBoard);//For loop avoidance (hold the board as is)
		hashTableID.add(myBoard.getId());//For loop avoidance (hold the id of the board)
		myStack.add(myBoard);//For Dfs search.
		numbersOfCreatedNodes.add(myBoard.getId());
		openListOfNodes.add(myBoard.getId());
		
		
		if(isPrintOpen) printOpenList(openListOfNodes);
		while(!myStack.isEmpty()){
			Board current = myStack.pop();
			if(current.getOut()=="out") {//If the node is not part of the path that i work on we take it out and clear the "out" var
				current.setOut("");
				hashTable.remove(current);
				hashTableID.remove(current.getId());
			}
			else {
				openListOfNodes.remove(current.getId());
				current.setOut("out");
				myStack.add(current);
				ArrayList<Board> childrens =  createChildrens(current);//create all the allowd operators fron the node	
				PriorityQueue<Board> pQueue = new PriorityQueue<Board>(); //The queue is for node ordaring.
				for(Board b : childrens) {
					pQueue.add(b);
					numbersOfCreatedNodes.add(b.getId());
					openListOfNodes.add(b.getId());
				}
				if(isPrintOpen) printOpenList(openListOfNodes);
				
				for(Board b : pQueue) {//sorted childrens by F(b)
					if(b.F_cost_to_choose >= threshold) {//if the current son have F > then the thresholl remove the node and all other nodes
						pQueue.remove(b);
						pQueue.clear();
						continue;
					}
					else if(hashTable.contains(b) && b.getOut()=="out") {//for loop avoidance proper, if we find a node that on branch, avoid it.  
						pQueue.remove(b);
						continue;
					}
					else if(hashTable.contains(b.getId()) && (b.getOut() != "out")) {
						Board temp = findBoard(b);
						if(temp.F_cost_to_choose <= b.F_cost_to_choose) {//we find a better path to an acsist board.
							pQueue.remove(b);
							continue;
						}
						else {						
							hashTable.remove(temp);
							hashTableID.remove(temp.getId());
							myStack.remove(b);
							hashTable.add(b);
							hashTableID.add(b.getId());
							continue;
						}
					}
					else if(b.checkIfWin()) {//f(g) < t
						//System.out.println(b.path);//printing the path for each solution
						threshold = b.F_cost_to_choose;
						winBoard = b;
						pQueue.remove(b);
						pQueue.clear();
						for(Board delit : pQueue) {
							pQueue.remove(delit);
						}
						continue;
					}

				}
				Stack<Board> forReversTheElement = new Stack<Board>();
				while(!pQueue.isEmpty()) {
					forReversTheElement.add(pQueue.poll());
				}
				while(!forReversTheElement.isEmpty()) {
					myStack.add(forReversTheElement.peek());
					hashTableID.add(forReversTheElement.peek().boardId);
					hashTable.add(forReversTheElement.pop());
				}
			}
		}

		String inalPath = winBoard.path.substring(0,winBoard.path.length()-1);
		String numOfNodes = "Num: " + String.valueOf(numbersOfCreatedNodes.size());
		String Cost ="Cost: " + String.valueOf(winBoard.G_cost_to_choose);
		long endTime = System.nanoTime();
		String algorithmTime = String.valueOf((endTime-startTime)*Math.pow(10, -9) + " seconds");
		String[] solution = new String[4];
		solution[0] = inalPath;
		solution[1] = numOfNodes;
		solution[2] = Cost;
		solution[3] = algorithmTime;

		return solution;




	}








	public String[] getSolution() {
		return this.solution;
	}

	public void printOpenList(Set<String> nodes) {
		for(String b : nodes) {
			System.out.print(b + ", ");
		}
		System.out.println();
	}


	public Board findBoard(Board b) {
		for (Iterator<Board> it = hashTable.iterator(); it.hasNext(); ) {
			Board f = it.next();
			if (f.getId().equals(b.getId()))
				return f;
		}
		return null;
	}

	public int factorial(int n) 
	{ 
		if (n == 0) 
			return 1; 

		return n*factorial(n-1); 
	}
	public ArrayList<Board> createChildrens(Board node) {
		ArrayList<Board> childrens = new ArrayList<Board>( );
		if(node.y_whiteT < node.mystate[0].length-1 && node.mystate[node.x_whiteT][node.y_whiteT+1].color != 'B') {
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
