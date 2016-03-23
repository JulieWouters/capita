package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VariableNeighborhoodSearch {
	public static int timespan;
	public static int workDayMin;
	public static int workDayMax;
	public static int offDayMin;
	public static int offDayMax;
	public static int[] demand;
	public static int kmax;

	public static void main(String[] args) {
		File file = new File(args[0]);
		parseFile(file);
		kmax = Integer.parseInt(args[1]);

		ArrayList<int[]> solution = runVNSAlgorithm();
		printSolution(solution);
	}

	private static ArrayList<int[]> runVNSAlgorithm() {
		int k = 1;
		ArrayList<int[]> x;
		x = createInitialSolution();
		// nog stopconditie
		while(k <= kmax){
			ArrayList<int[]> x2 = shake(x,k);
			int l = 1;
			while(l <= kmax){
				ArrayList<int[]> x3 = exploreNeighborhood(x2,l);
				if(getCost(x3) < getCost(x2)){
					x2 = x3;
					l = 1;
				}
				else
					l++;
			}
			if(getCost(x2) < getCost(x)){
				x = x2;
				k = 1;
			}
			else
				k++;
		}
		return x;
	}

	private static int getCost(ArrayList<int[]> x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static ArrayList<int[]> exploreNeighborhood(ArrayList<int[]> x2, int l) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<int[]> createInitialSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	private static void parseFile(File file) {
		FileReader in;
		try {
			in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			String line = null;
			while( (line = reader.readLine()) != null) {
				String[] split = line.split("-");
				timespan = Integer.parseInt(split[0]);
				workDayMin = Integer.parseInt(split[1]);
				workDayMax = Integer.parseInt(split[2]);
				offDayMin = Integer.parseInt(split[3]);
				offDayMax = Integer.parseInt(split[4]);
				demand = new int[timespan];
				String[] array = split[5].substring(1).split(",");
				for(int i = 0; i < timespan; i++){
					demand[i] = Integer.parseInt(array[i]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printSolution(ArrayList<int[]> solution) {
		for(int i = 0; i < solution.size(); i++){
			System.out.println(Arrays.toString(solution.get(i)));
		}
	}
}
