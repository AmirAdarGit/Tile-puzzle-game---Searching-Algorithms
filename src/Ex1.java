import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

//import org.graalvm.compiler.nodes.debug.BlackholeNode;

import java.util.ArrayList; // import the ArrayList class
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Ex1 {
	String algorithmName;
	String runTime;
	String outputFile;
	String [] black;
	String [] red;
	String [] green;
	String boardSize;
	int n;
	int m;
	String [][] startState;
	String [] checkValidInput;
	PrintStream out = new PrintStream(new FileOutputStream("output.txt"));//out stream for results

    
    
    
    
    public Ex1() throws NumberFormatException, IOException {
 
    	//Input the real path of the input file in order to run the algorithms//
     	File file = new File("input.txt"); 
		BufferedReader inFile = new BufferedReader(new FileReader(file)); 
        
        
		this.algorithmName = inFile.readLine();
		this.runTime = inFile.readLine(); //with or without time.
		this.outputFile = inFile.readLine();//with ot without open list

		boardSize = inFile.readLine();    
		int index = boardSize.indexOf('x');
		this.n = Integer.parseInt(boardSize.substring(0, index));  
		this.m = Integer.parseInt(boardSize.substring(index+1,boardSize.length()));

		if(n == 0 || m == 0) { //input edge cases 
			System.err.println("n or m cannot be equall to 0!");
			return;
		}	
		String blackCells = inFile.readLine();
		int blackIndex = blackCells.indexOf(":");
		blackCells = blackCells.substring(blackIndex+1);
		this.black = blackCells.split(",");

		String redCells = inFile.readLine();
		int redIndex = redCells.indexOf(":");
		redCells = redCells.substring(redIndex+2);
		this.red = redCells.split(",");


		this.green = findGreenCells(black,red,n,m);
		this.checkValidInput = new String[n*m]; //[1,2,3,4,5....n-1,_]
		this.startState  = new String[n][m];

		int i = 0;//For each line in the 2d array 
		String values;
		while((values = inFile.readLine()) != null && (!values.isEmpty())) {
			String[] line = values.split(",");
			for(int j=0 ; j<line.length ; j++) {
				if(line[j].matches("[0-9]+")||(line[j].matches("_"))){
					if(line[j].matches("[0-9]+")) {
						int temp = Integer.parseInt(line[j]);
						if(temp > checkValidInput.length-1) {
							System.err.println("Invalid input, values of "+ line[j]);
							return;
						}
						if(checkValidInput[temp-1] == null) {
							checkValidInput[temp-1]=line[j];
						}
						else {
							System.err.println("Invalid input, there duplicates values of "+ line[j]);
							return;
						}
					}
					else {// the char is "_" input to the last cell at checkValidInput array
						if(checkValidInput[checkValidInput.length-1] == null) {
							checkValidInput[checkValidInput.length-1]=line[j];
						}
						else {
							System.err.println("Invalid input, there duplicates values of " + line[j]);
							return;
						}
					}
				}
				else {
					System.out.println(line[j]);
					System.err.println("Invalid input, the input can be only numbers and _");
					return;
				}
			}

			if(line.length != m) { //input edge cases 
				System.err.println("Invalid input, n/m does'nt equal to the board size");
				return;
			}
			startState[i] = line;
			i++;
		}

		Board myBoard = new Board(this); 
		switch(algorithmName)
		{

		//---------------------------------------------BFS Algorithm-------------------------------------//
		case "BFS" :  //rum BFS Algorithm		
			if(this.outputFile.contains("no open")) {
				if(this.runTime.contains("with time")) {
					Bfs bfs = new Bfs (myBoard,false);			
					printEverythingOut(bfs.getSolution());//print out to "output.txt" file with time
				}
				else {
					Bfs bfs = new Bfs(myBoard,false);
					printEverythingOutNoTime(bfs.getSolution());//print out to "output.txt" file without time
				}
			}
			else {//print with openlist
				if(this.runTime.contains("with time")) {
					Bfs bfs = new Bfs (myBoard,true);
					printEverythingToConsuleWithTime(bfs.getSolution());//print to console with open list and with time
				}
				else {
					Bfs bfs = new Bfs (myBoard,true);
					printEverythingToConsuleNoTime(bfs.getSolution());//print to console with open list and without time
				}
			}			
			break;  
			
			//---------------------------------------------DFID Algorithm-------------------------------------//	
		case "DFID" :
			if(this.outputFile.contains("no open")) {
				if(this.runTime.contains("with time")) {
					Dfid dfid = new Dfid(myBoard,false);
					if(dfid.getSolution() != null)
						printEverythingOut(dfid.getSolution());
				}
				else {
					Dfid dfid = new Dfid(myBoard,false);
					printEverythingOutNoTime(dfid.getSolution());
				}
			}
			else {
				//print with openlist
				if(this.runTime.contains("with time")) {
					Dfid dfid = new Dfid(myBoard,true);
					printEverythingToConsuleWithTime(dfid.getSolution());
				}
				else {
					Dfid dfid = new Dfid(myBoard,true);
					printEverythingToConsuleNoTime(dfid.getSolution());
				}
			}

			break; 


			//---------------------------------------------A* Algorithm-------------------------------------//	
		case "A*" :   	
			if(this.outputFile.contains("no open")) {
				if(this.runTime.contains("with time")) {
					Astar astar = new Astar(myBoard,false);
					if(astar.getSolution() != null)
						printEverythingOut(astar.getSolution());
				}
				else {
					Astar astar = new Astar(myBoard,false);
					printEverythingOutNoTime(astar.getSolution());
				}
			}
			else {
				//print with openlist
				if(this.runTime.contains("with time")) {
					Astar astar = new Astar(myBoard,true);
					printEverythingToConsuleWithTime(astar.getSolution());
				}
				else {
					Astar astar = new Astar(myBoard,true);
					printEverythingToConsuleNoTime(astar.getSolution());
				}
			}
			break;  
			
			//---------------------------------------------IDA* Algorithm-------------------------------------//	

 	
		case "IDA*" :
			if(this.outputFile.contains("no open")) {
				if(this.runTime.contains("with time")) {
					IdAstar idastar = new IdAstar(myBoard,false);
					if(idastar.getSolution() != null)
						printEverythingOut(idastar.getSolution());
				}
				else {
					IdAstar idastar = new IdAstar(myBoard,false);
					printEverythingOutNoTime(idastar.getSolution());
				}
			}
			else {
				//print with openlist
				if(this.runTime.contains("with time")) {
					IdAstar idastar = new IdAstar(myBoard,true);
					printEverythingToConsuleWithTime(idastar.getSolution());
				}
				else {
					IdAstar idastar = new IdAstar(myBoard,true);
					printEverythingToConsuleNoTime(idastar.getSolution());
				}
			}		
			break; 
			
			
			//---------------------------------------------DFBnB Algorithm-------------------------------------//	
		case "DFBnB" :
			if(this.outputFile.contains("no open")) {
				if(this.runTime.contains("with time")) {
					Dfbnb dfbnb = new Dfbnb(myBoard,false);
					if(dfbnb.getSolution() != null)
						printEverythingOut(dfbnb.getSolution());
				}
				else {
					Dfbnb dfbnb = new Dfbnb(myBoard,false);
					printEverythingOutNoTime(dfbnb.getSolution());
				}
			}
			else {
				//print with openlist
				if(this.runTime.contains("with time")) {
					Dfbnb dfbnb = new Dfbnb(myBoard,true);
					printEverythingToConsuleWithTime(dfbnb.getSolution());
				}
				else {
					Dfbnb dfbnb = new Dfbnb(myBoard,true);
					printEverythingToConsuleNoTime(dfbnb.getSolution());
				}
			}



			break;

		default :
			System.out.println("Error input algorithm.");
		}

	}


	public void printEverythingToConsuleWithTime(String[] print) {
		for(int i = 0; i<print.length; i++) {
			System.out.println(print[i]);
		}
	}

	public void printEverythingToConsuleNoTime(String[] print) {
		for(int i = 0; i<print.length-1; i++) {
			System.out.println(print[i]);
		}
	}

	public void printEverythingOut(String[] print) {
		for(int i = 0; i<print.length; i++) {
			out.append(print[i] + "\n");
		}
	}
	public void printEverythingOutNoTime(String[] print) {
		for(int i = 0; i<print.length-1; i++) {
			out.append(print[i] + "\n");
		}
	}	


	public String[] findGreenCells(String[] black, String[] red,int n,int m) {
		for (int i = 0; i < black.length; i++)//In order to remove oll the white space in the array
			black[i] = black[i].trim();

		for (int i = 0; i < red.length; i++)//In order to remove oll the white space in the array
			red[i] = red[i].trim();

		ArrayList<String> array = new ArrayList<String>(); // Create an ArrayList object
		for(int i=1; i<(n*m) ; i++) {
			array.add(String.valueOf(i));
		}
		for(int i=0 ; i<black.length ; i++) {
			if(array.contains(black[i])) {
				array.remove(black[i]);
			}
		}
		for(int i=0 ; i<red.length ; i++) {
			if(array.contains(red[i])) {
				array.remove(red[i]);
			}
		}

		String[] green = array.toArray(new String[0]);
		return green;
	}



	public void printInitBoard() {
		for(int i = 0 ; i < startState.length ; i++) {
			for(int j = 0 ; j < startState[0].length; j++) {
				System.out.print (startState[i][j]+ " " );
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		Ex1 init = new Ex1();



	}
}
