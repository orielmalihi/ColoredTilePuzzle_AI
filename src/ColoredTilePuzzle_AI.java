import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * this class deals with the input/output of the game including loading
 * the first state, running the game and returning the output of the game
 * @author oriel
 *
 */
public class ColoredTilePuzzle_AI {

	private String algo = "";
	private boolean isWithTime = true, isWithOpen = true;
	private State firstState;
	private search_algorithms search_algo;

	public ColoredTilePuzzle_AI() {}
	public void LoadGame(String file) throws IOException{
		File fileName = new File(file);
		BufferedReader br = new BufferedReader(new FileReader(fileName)); 

		String string;
		algo = br.readLine(); // row 1 = algo
		string = br.readLine(); // row 2 = time
		if(string.contains("no"))
			isWithTime = false;
		string = br.readLine(); // row 3 = open
		if(string.contains("no"))
			isWithOpen = false;
		string = br.readLine(); // row 4 = size
		String[] size = string.split("x");
		int n = Integer.parseInt(size[0]);
		int m = Integer.parseInt(size[1]);
		Tile[][] mat = new Tile[n][m];
		String[] black = br.readLine().substring(6).replaceAll("\\s","").split(","); // black tiles
		ArrayList<Integer> blacks = new ArrayList<Integer>();
		for (int i = 0; i < black.length; i++) {
			if(!black[i].isEmpty())
				blacks.add(Integer.parseInt(black[i]));
		}
		String[] red = br.readLine().substring(4).replaceAll("\\s","").split(","); // red tiles
		ArrayList<Integer> reds = new ArrayList<Integer>();
		for (int i = 0; i < red.length; i++) {
			if(!red[i].isEmpty())
				reds.add(Integer.parseInt(red[i]));
		}

		for(int i =0; i<mat.length; i++) {
			String[] values = br.readLine().split(",");
			for(int j = 0; j<mat[0].length; j++) {
				if(values[j].equals("_")) {
					mat[i][j] = null;
				}
				else {
					mat[i][j] = new Tile(Integer.parseInt(values[j]));
					if(reds.contains(mat[i][j].getVal())) {
						mat[i][j].setColor(2);
					}
					if(blacks.contains(mat[i][j].getVal())) {
						mat[i][j].setColor(3);
					}
				}
			}
		}
		firstState = new State(mat);

	}

	public void solve() {
		switch(algo) {
		case "BFS":
			search_algo = new BFS_Search(firstState, isWithTime, isWithOpen);
			if(firstState.h()==-1)
				return;
			search_algo.solve_game();
			break;
		case "DFID":
			search_algo = new DFID_Search(firstState, isWithTime, isWithOpen);
			if(firstState.h()==-1)
				return;
			search_algo.solve_game();
			break;
		case "A*":
			search_algo = new A_Star_Search(firstState, isWithTime, isWithOpen);
			if(firstState.h()==-1)
				return;
			search_algo.solve_game();
			break;
		case "IDA*":
			search_algo = new IDA_Star_Search(firstState, isWithTime, isWithOpen);
			if(firstState.h()==-1)
				return;
			search_algo.solve_game();
			break;
		case "DFBnB":
			search_algo = new DFBnB_Search(firstState, isWithTime, isWithOpen);
			if(firstState.h()==-1)
				return;
			search_algo.solve_game();
			break;
		}
	}


	/**
	 * saves the result to the program folder
	 * @param file
	 */
	public void saveResult(String file) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			if(!search_algo.isSolved()) {
				writer.write("no path\nNum: "+search_algo.getNum());
			} else {
				String output = search_algo.getPath()+"\n";
				output += "Num: "+search_algo.getNum()+"\n";
				output += "Cost: "+search_algo.getCost()+"\n";
				if(isWithTime)
					output += (search_algo.getTime()/1000.0)+" seconds\n";
				writer.write(output);
			}
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * prints the result to the screen
	 */

	public void printResult() {
		if(!search_algo.isSolved()) {
			System.out.println("no path\nNum: "+search_algo.getNum());
		} else {
			String output = search_algo.getPath()+"\n";
			output += "Num: "+search_algo.getNum()+"\n";
			output += "Cost: "+search_algo.getCost()+"\n";
			if(isWithTime)
				output += (search_algo.getTime()/1000.0)+" seconds\n";
			System.out.println(output);
		}

	}
	
	/**
	 * returns the first state of the game
	 * @return
	 */

	public State getFirstState() {
		return firstState;
	}
	/**
	 * returns the algorithm that was chosen to solve this game
	 * @return
	 */
	
	public String getAlgo() {
		return algo;
	}
}
