import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

//import sun.awt.image.PixelConverter.Bgrx;


public class Astar {

	Set<String> closedList = new HashSet<String>(); //Represent an hash table thet hold the node that we extend alredy
	Set<Board> openList = new HashSet<Board>(); //Represent an hash table thet hold the node that we find but nod extend alredy
	PriorityQueue<Board> pQueue = new PriorityQueue<Board>(); 
	Board root;
	String[] solution;
	String chooseWithOpen = "";//input value: if it "with open" -> it will print every iteration the open list
	boolean isPrintOpen;
	
	public Astar(Board myBoard,boolean chooseWithOpen){
		this.isPrintOpen = chooseWithOpen;//acording to the user's choose, if print the open linst.
		this.root = myBoard;	
		if(root.checkIfWin()) {
			System.out.println("Num: 1");
			System.out.println("Cost: " + root.G_cost_to_choose);
		}
		else
			this.solution = Astar(myBoard);
	}

	public String[] Astar(Board myBoard){
		long startTime = System.nanoTime();
		pQueue.add(myBoard);
 		while(!pQueue.isEmpty()) {
 			if(isPrintOpen) printOpenList(pQueue);
			Board currentBoard = pQueue.poll();
			if(currentBoard.checkIfWin()) {
				String inalPath = currentBoard.path.substring(0,currentBoard.path.length()-1);
				String numOfNodes = "Num: " + String.valueOf(openList.size() + closedList.size());
				String Cost ="Cost: " + String.valueOf(currentBoard.G_cost_to_choose);
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
				openList.remove(currentBoard);
				closedList.add(currentBoard.boardId);
				ArrayList<Board> childrens =  createChildrens(currentBoard);//create all the allowd operators fron the node	
				for (Board b : childrens) {
					if(!closedList.contains(b.getId())&&(!openList.contains(b.getId()))) {
						pQueue.add(b);
					}
					else if(openList.contains(b.getId())) {//This else if statmant for change better path throw board that found alredy
						Board temp = findBoard(b);
						int b_prince = b.F_cost_to_choose;
						int temp_price = temp.F_cost_to_choose;
						if(temp_price > b_prince) {
							openList.remove(temp);
							openList.add(b);
						}
					}
				}
			}
		}
 		String[] solution = new String[3];
		solution[0] = "no path";
		solution[1] = "Num: " +(openList.size() + closedList.size());
		solution[2] =  String.valueOf((System.nanoTime()-startTime)*Math.pow(10, -9) + " seconds");
		return solution;
	}

	
	public void printOpenList(PriorityQueue<Board> queue) {
		for(Board b : queue) {
			System.out.print(b.boardId + ", ");
		}
		System.out.println();
	}
	
	public Board findBoard(Board b) {
		for (Iterator<Board> it = openList.iterator(); it.hasNext(); ) {
			Board f = it.next();
			if (f.getId().equals(b.getId()))
				return f;
		}
		return null;
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

				L.H_cost_to_choose = L.getHuristicCost(L);
				L.F_cost_to_choose = L.H_cost_to_choose + L.G_cost_to_choose;
				L.path += node.mystate[node.x_whiteT][node.y_whiteT+1].cellNumber + "L-" ;
				openList.add(L);
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
				U.H_cost_to_choose = U.getHuristicCost(U);
				U.F_cost_to_choose = U.H_cost_to_choose + U.G_cost_to_choose;
				U.path += node.mystate[node.x_whiteT+1][node.y_whiteT].cellNumber + "U-" ;
				openList.add(U);
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
				R.H_cost_to_choose = R.getHuristicCost(R);
				R.F_cost_to_choose = R.H_cost_to_choose + R.G_cost_to_choose;
				R.path += node.mystate[node.x_whiteT][node.y_whiteT-1].cellNumber + "R-" ;
				openList.add(R);
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
				D.H_cost_to_choose = D.getHuristicCost(D);
				D.F_cost_to_choose = D.H_cost_to_choose + D.G_cost_to_choose;
				D.path += node.mystate[node.x_whiteT-1][node.y_whiteT].cellNumber + "D-";
				openList.add(D);
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
