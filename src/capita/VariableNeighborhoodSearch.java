package capita;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class VariableNeighborhoodSearch {
	public int kmax;
	public int[] demand;
	
	public ArrayList<int[]> runVNSAlgorithm() {
		int k;
		ArrayList<int[]> x;
		x = createInitialSolution();
//		System.out.println("INITIAL SOLUTION");
//		printSolution(x);
//		System.out.println("WITH COST:");
//		System.out.println(getCost(x));
		// nog stopconditie
		int iter = 3;
		int cost = getCost(x);
		while(iter > 0){
			if(cost == getCost(x)){
				iter--;
			}
			else{
				cost = getCost(x);
				iter = 3;
			}
			k = 1;
			while(k <= kmax){
				ArrayList<int[]> x2 = shake(x,k);
				int l = 1;
				while(l <= kmax){
					ArrayList<int[]> x3 = exploreNeighborhood(x2,l);
					if(getCost(x3) < getCost(x2)){
						x2 = x3;
						l = 1;
					}
					else {
						l++;
					}
				}
				if(getCost(x2) < getCost(x)){
					x = x2;
					k = 1;
				}
				else {
					k++;
				}
			}
//			System.out.println(getCost(x));
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
	
	public int getDemandShortage(ArrayList<int[]> solution) {
		if(solution.size() == 0) {
			return 100;
		}
		int[] currentWorkTotals = new int[solution.get(0).length];
		for (int i = 0; i < solution.get(0).length; ++i) {
			int totalValue = 0;
			for(int j = 0; j < solution.size(); j++) {
				totalValue = totalValue + solution.get(j)[i];
			}
		    currentWorkTotals[i] = totalValue;
		}
		int shortage = 0;
		for(int k = 0; k < currentWorkTotals.length; k++) {
			if(currentWorkTotals[k] < demand[k]) {
				shortage = shortage + demand[k] - currentWorkTotals[k];
			}
		}
		return shortage;
	}
}
