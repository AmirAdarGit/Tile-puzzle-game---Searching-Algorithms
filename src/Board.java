import java.util.ArrayList;
import java.util.Arrays;
public class Board implements Comparable<Board>
{

	Cell [][] mystate;//current config
	Cell [][] finalState;//final state

	int x_whiteT;//(x,y) cordinates of the white tile
	int y_whiteT;//(x,y) cordinates of the white tile
	String [] blackList;
	String [] redList;
	String [] greenList;
	String path="";
	String boardId;
	boolean isCutOFF = false;//for DFID
	boolean findTheGoal = false;
	
	String out = "";//for IDA*
	
	int G_cost_to_choose = 0;
	int H_cost_to_choose = 0;
	int F_cost_to_choose = 0;
	public int compareTo (Board other) {
		if(this.F_cost_to_choose == other.F_cost_to_choose) {
			return 0;
		}
		else if (this.F_cost_to_choose > other.F_cost_to_choose)
			return 1;
		else
			return -1;
	}

	public boolean equal(Board other) {
		return this.F_cost_to_choose == other.F_cost_to_choose;
	}
	
	
	public Board(Board b) {//copy constractor
		Cell[][] copy = new Cell[b.mystate.length][b.mystate[0].length];
		for(int i=0 ; i<b.mystate.length ; i++) {
			for(int j=0 ; j<b.mystate[0].length ; j++) {
				copy[i][j] = new Cell (b.mystate[i][j].cellNumber ,b.mystate[i][j].color ,b.mystate[i][j].isNumber);
 			}
		}
		//for Dfid
		this.isCutOFF = b.isCutOFF;
		this.findTheGoal = b.findTheGoal;
		//for Dfid
		
		//for IDA*
		//this.out = b.out;
		
		//for IDA*
		
		this.G_cost_to_choose = b.G_cost_to_choose;
		this.path = b.path;
		this.mystate = copy;
		this.finalState = b.finalState;
		this.x_whiteT = b.x_whiteT;
		this.y_whiteT = b.y_whiteT;
 		this.blackList = b.blackList;
		this.redList = b.redList;
		this.greenList = b.greenList;
		searchForBoardId();

	}


	public Board(Ex1 init) {
		//for Dfid
		this.isCutOFF = false;
		this.findTheGoal = false;
		//
		
		char color;
		boolean isNumber;
		int count = 1;
		blackList = init.black;
		redList = init.red;
		greenList = init.green;
		ArrayList<String> g = new ArrayList<String>(Arrays.asList(greenList));
		ArrayList<String> r = new ArrayList<String>(Arrays.asList(redList));
		ArrayList<String> b = new ArrayList<String>(Arrays.asList(blackList));

		this.mystate = new Cell[init.n][init.m];
		this.finalState = new Cell[init.n][init.m];

		for(int i=0 ; i<init.startState.length ; i++) {
			for(int j=0 ; j<init.startState[0].length ; j++) {
				if(b.contains(init.startState[i][j])) {
					color = 'B'; 
					isNumber = true;

				}
				else if(r.contains(init.startState[i][j])) {
					color = 'R';
					isNumber = true;

				}
				else if(g.contains(init.startState[i][j])) {
					color = 'G'; 
					isNumber = true;

				}
				else {
					color = '_';
					//collect the cordinates (x,y) of the white Tile
					x_whiteT = i;
					y_whiteT = j;
					isNumber = false;
				}
				mystate[i][j] = new Cell(init.startState[i][j],color,isNumber);
				finalState[i][j] = new Cell(String.valueOf(count++));
			}
		}
		finalState[init.n-1][init.m-1] = new Cell("_");
		searchForBoardId();
	}
	public void setHuristics(int H) {
		this.H_cost_to_choose = H;
	}
	public void setFcost(int F) {
		this.F_cost_to_choose = F;
	}
	public String getOut() {
		return this.out;
	}
	
	public void setOut(String s) {
		this.out = s;
	}
	public String getId() {
		return boardId;
	}
	public String searchForBoardId(){
		String id = "";
		for(int i=0 ; i<mystate.length ; i++) {
			for(int j=0 ; j<mystate[0].length ; j++) {
				id+=mystate[i][j].cellNumber;
			}
		}
		this.boardId = id;
		return id;
	}	
	
	public void findWhiteXY(Cell[][] b) {
		for(int i=0 ; i<b.length ; i++) {
			for(int j=0 ; j<b[0].length ; j++) {
				if(b[i][j].cellNumber.contains("_")) {
					this.x_whiteT = i;
					this.y_whiteT = j;
 				}
			}
		}
	}
	public void getWhiteCordinate() {
		System.out.println(x_whiteT + ", " + y_whiteT);
	}

	public void setFindTheGoal(boolean what) {
		this.findTheGoal = what;
	}

	public boolean checkIfWin() {
		for(int i=0 ; i<mystate.length ; i++) {
			for(int j=0 ; j<mystate[0].length ; j++) {
				if(mystate[i][j].cellNumber.equals(finalState[i][j].cellNumber)) continue;
				else {
					return false;
				}
			}
		}
		return true;
	}

 
	public void setWhiteTileX(int newCoordinateX) {
		this.x_whiteT = newCoordinateX;
	}
	public void setWhiteTileY(int newCoordinateX) {
		this.x_whiteT = newCoordinateX;
	}
	public void printBoard() {
		for(int i = 0 ; i < mystate.length ; i++) {
			for(int j = 0 ; j < mystate[0].length; j++) {
				System.out.print (mystate[i][j].cellNumber + " " );
			}
			System.out.println();
		}
	}
	public void printFinalBoard() {
		for(int i = 0 ; i < finalState.length ; i++) {
			for(int j = 0 ; j < finalState[0].length; j++) {
				System.out.print (finalState[i][j].cellNumber + " " );
			}
			System.out.println();
		}
	}
	
	
	//This functions calculate the heuristic according to the "Manhattan Distance" heuristic.
	public int getHuristicCost(Board B) {
		int x_node,y_node;
		String placeOfCellInWinTable;
		int huristicCount=0;
		for(int i=0; i<B.mystate.length; i++) {
			for(int j=0; j<B.mystate[0].length; j++) {
				if((!B.mystate[i][j].cellNumber.equals(B.finalState[i][j].cellNumber))&&(!B.finalState[i][j].cellNumber.equals("_"))) {
					x_node=i;
					y_node=j;
					placeOfCellInWinTable = B.finalState[i][j].cellNumber;
					huristicCount += getHuristicCostHelp(x_node,y_node,placeOfCellInWinTable,B);
				}
			}			
		}
		return huristicCount;
	}
	public int getHuristicCostHelp(int x_node,int y_node,String placeOfCellInWinTable,Board B) {
		int x_final,y_final;
		int huristicCost;
		for(int i=0; i<B.mystate.length; i++) {
			for(int j=0; j<B.mystate[0].length; j++) {
				if(B.mystate[i][j].cellNumber.equals(placeOfCellInWinTable)) {
					x_final=i;
					y_final=j;
					huristicCost = (Math.abs(x_final - x_node) + Math.abs(y_final - y_node));//x2-x1 + y2-y1 
					//System.out.println(B.mystate[i][j].color);
					if(B.mystate[i][j].color == 'R') {
						huristicCost *= 30;
					}
					return huristicCost;
				}
			}
		}
		return 0;
	}

}
