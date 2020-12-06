
public class Cell {

	String cellNumber;      // represent the cell data namber, (0 if it [_])
	char color;          // B-black, R-red, G-green
	boolean isNumber;    // [0:9]
 	
	public Cell(String number, char color , boolean isNumber) {
		this.cellNumber = number;
 		this.color = color;
 		this.isNumber = isNumber;
	}
	public Cell(String number) {
		this.cellNumber = number;
	}
	public Cell() {
		this.cellNumber = "";
		this.color = ' ';
		this.isNumber = true;
	}
	public Cell(Cell c) {
		this.cellNumber= c.cellNumber;
		this.color = c.color;
		this.isNumber = c.isNumber;
	}
	
	public String getCellNumber() {
		return cellNumber;
	}
	
	
	
}
