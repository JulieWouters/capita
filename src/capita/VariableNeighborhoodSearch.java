package capita;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class VariableNeighborhoodSearch {
	public int kmax;
	
	public ArrayList<int[]> runVNSAlgorithm() {
		int k = 1;
		ArrayList<int[]> x;
		x = createInitialSolution();
		// nog stopconditie
		while(k <= kmax){
			ArrayList<int[]> x2 = shake(x,k);
			int l = 1;
			while(l <= kmax){
				ArrayList<int[]> x3 = exploreNeighborhood(x2,l);
				printSolution(x3);
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
	
	public abstract int getCost(ArrayList<int[]> x);
	
	public abstract ArrayList<int[]> exploreNeighborhood(ArrayList<int[]> x, int l);
	
	public abstract ArrayList<int[]> shake(ArrayList<int[]> x, int k);

	public abstract ArrayList<int[]> createInitialSolution();

	public void printSolution(ArrayList<int[]> solution) {
		for(int i = 0; i < solution.size(); i++){
			System.out.println(Arrays.toString(solution.get(i)));
		}
		System.out.println("\n");
	}

	public abstract void parseFile(File file);
	
	public int trimAndParse(String str){
		return Integer.parseInt(str.trim());
	}
}
