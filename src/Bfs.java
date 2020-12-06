import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;




public class Bfs {

	Queue<Board> queue = new LinkedList<Board>();
	Set<String> closedList = new HashSet<String>(); //Represent an hash table thet hold the node that we extend alredy
	Set<String> openList = new HashSet<String>(); //Represent an hash table thet hold the node that we find but nod extend alredy
	Board root;//Represent the board that we get from the user
	String chooseWithOpen = "";//input value: if it "with open" -> it will print every iteration the open list
	boolean isPrintOpen;
	String[] solution;

	public Bfs(Board myBoard,boolean chooseWithOpen){
		this.root = myBoard;	
		this.isPrintOpen = chooseWithOpen;//acording to the user's choose, if print the open linst.

		if(root.checkIfWin()) {
			System.out.println("Num: 1");
			System.out.println("Cost: " + root.G_cost_to_choose);
		}
		else
			this.solution = runBfs(myBoard);
	}
	public String[] runBfs(Board myBoard){
		int numOFNodes = 1;
		openList.add(myBoard.getId());
		if(isPrintOpen) printOpenList(openList);
		long startTime = System.nanoTime();
		queue.add(myBoard);//Add the root to the queue
		while(!queue.isEmpty()) {//The loop for each level in the algorithm
			Board node = queue.remove();//Use and remove the first node from the queue	
			ArrayList<Board> childrens =  createChildrens(node);//create all the allowd operators fron the node	
			closedList.add(node.searchForBoardId());
			openList.remove(node.searchForBoardId());
			if(isPrintOpen) printOpenList(openList);
			for (Board b : childrens) {	
				numOFNodes++;
				if(b.checkIfWin()){	

					String inalPath = b.path.substring(0,b.path.length()-1);
					String numOfNodes = "Num: " + String.valueOf(numOFNodes);
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
				openList.add(myBoard.getId());
				queue.add(b);
			}

		}
		String[] solution = new String[3];
		solution[0] = "no path";
		solution[1] = "Num: " +(openList.size() + closedList.size());
		solution[2] =  String.valueOf((System.nanoTime()-startTime)*Math.pow(10, -9) + " seconds");
		return solution;
	}

	public void printOpenList(Set<String> openList) {
		for(String s : openList) {
			System.out.print(s + ", ");
		}
		System.out.println();
	}

	public String[] getSolution() {
		return this.solution;
	}







	public ArrayList<Board> createChildrens(Board node) {
		//node.printBoard();
		ArrayList<Board> childrens = new ArrayList<Board>( );
		if(node.y_whiteT < node.mystate[0].length-1 && node.mystate[node.x_whiteT][node.y_whiteT+1].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black			
			Board copyOfNodeL = new Board(node);
			Board L = swapTile(copyOfNodeL,'L');
			if(!closedList.contains(L.boardId)&&!openList.contains(L.boardId)){	
				childrens.add(L);
				if(node.mystate[node.x_whiteT][node.y_whiteT+1].color == 'G')
					L.G_cost_to_choose +=1;//green
				else
					L.G_cost_to_choose += 30;//red
				L.path += node.mystate[node.x_whiteT][node.y_whiteT+1].cellNumber + "L-" ;
				openList.add(L.searchForBoardId());
			}
		} 

		if(node.x_whiteT < node.mystate.length-1 && node.mystate[node.x_whiteT+1][node.y_whiteT].color != 'B'){
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black
			Board copyOfNodeU = new Board(node);
			Board U = swapTile(copyOfNodeU,'U');
			if(!closedList.contains(U.boardId)&&!openList.contains(U.boardId)){	
				childrens.add(U);
				if(node.mystate[node.x_whiteT+1][node.y_whiteT].color == 'G')
					U.G_cost_to_choose +=1;//green
				else
					U.G_cost_to_choose += 30;//red
				U.path += node.mystate[node.x_whiteT+1][node.y_whiteT].cellNumber + "U-" ;
				openList.add(U.getId());
			}
		}
		if(node.y_whiteT > 0 && node.mystate[node.x_whiteT][node.y_whiteT-1].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black
			Board copyOfNodeR = new Board(node);
			Board R = swapTile(copyOfNodeR,'R');	 
			if(!closedList.contains(R.boardId)&&!openList.contains(R.boardId)){	
				childrens.add(R);
				if(node.mystate[node.x_whiteT][node.y_whiteT-1].color == 'G')
					R.G_cost_to_choose +=1;//green
				else
					R.G_cost_to_choose += 30;//red
				R.path += node.mystate[node.x_whiteT][node.y_whiteT-1].cellNumber + "R-" ;
				openList.add(R.getId());
			}
		}
		if(node.x_whiteT > 0 && node.mystate[node.x_whiteT-1][node.y_whiteT].color != 'B') {
			//				&& !openList.contains(node.getBoardId()))  {//the block cannot be black
			Board copyOfNodeD = new Board(node);
			Board D = swapTile(copyOfNodeD,'D');
			if(!closedList.contains(D.boardId)&&!openList.contains(D.boardId)){	
				childrens.add(D);
				if(node.mystate[node.x_whiteT-1][node.y_whiteT].color == 'G')
					D.G_cost_to_choose +=1;//green
				else
					D.G_cost_to_choose += 30;//red
				D.path += node.mystate[node.x_whiteT-1][node.y_whiteT].cellNumber + "D-";
				openList.add(D.getId());
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













