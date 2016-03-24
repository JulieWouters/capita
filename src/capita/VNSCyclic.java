package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class VNSCyclic extends VariableNeighborhoodSearch {
	public int timespan;
	public int[] cycle;
	public int[] demand;
	
	@Override
	public int getCost(ArrayList<int[]> x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<int[]> exploreNeighborhood(ArrayList<int[]> x, int l) {
		ArrayList<ArrayList<int[]>> allNeighbours;
		if (l < 3) {
			allNeighbours = getAllKSwapNeighbours(x, l);
		} else if (l == 3) {
			allNeighbours = getAllAddDropNeighbours(x);
		} else {
			allNeighbours = getAllKSwapNeighbours(x, l);
		}
		
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		int cost = Integer.MAX_VALUE;
		for(ArrayList<int[]> neighbour : allNeighbours) {
			int newCost = getCost(neighbour);
			if(newCost < cost) {
				cost = newCost;
				bestNeighbour = neighbour;
			}
		}
		
		return bestNeighbour;
	}

	private ArrayList<ArrayList<int[]>> getAllAddDropNeighbours(ArrayList<int[]> x) {
		ArrayList<ArrayList<int[]>> allNeighbours = new ArrayList<ArrayList<int[]>>();
		Random generator = new Random();
		for(int i = 0; i < x.size() + 1; i++) {
			if(i < x.size()) {
				x.remove(i);
				allNeighbours.add(x);
			} else {
				int[] permutation = cycle.clone();
				permutate(permutation, generator.nextInt(timespan-1)+1);
				x.add(permutation.clone());
				allNeighbours.add(x);
			}
		}
		
		return allNeighbours;
	}

	private ArrayList<ArrayList<int[]>> getAllKSwapNeighbours(ArrayList<int[]> x, int l) {

		return null;
	}

	@Override
	public ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		if (k < 3) {
			return getRandomKSwapNeighbour(x, k);
		} else if (k == 3) {
			return getRandomAddDropNeighbour(x);
		} else {
			return getRandomKSwapNeighbour(x, k-1);
		}
	}

	private ArrayList<int[]> getRandomAddDropNeighbour(ArrayList<int[]> x) {
		Random generator = new Random(); 
		int index = generator.nextInt(x.size() + 1);
		if(index < x.size()) {
			x.remove(index);
			return x;
		} else {
			int[] permutation = cycle.clone();
			permutate(permutation, generator.nextInt(timespan-1)+1);
			x.add(permutation.clone());
			return x;
		}
	}

	private ArrayList<int[]> getRandomKSwapNeighbour(ArrayList<int[]> x, int k) {
		Random generator = new Random();
		int[] permutation = cycle.clone();
		ArrayList<int[]> randomNeighbour = x;
		int n = k;
		while(n > 0){
			int rowIndex = generator.nextInt(x.size()-(k-n));
			randomNeighbour.remove(rowIndex);
			n--;
		}
		n = k;
		while(n > 0){
			permutate(permutation, generator.nextInt(timespan-1)+1);
			randomNeighbour.add(permutation.clone());
			n--;
		}
		return randomNeighbour;
	}

	@Override
	public ArrayList<int[]> createInitialSolution() {
		ArrayList<int[]> solution = new ArrayList<int[]>();
		int[] perm = cycle.clone();
		for(int i = 0; i < timespan; i++){
			while(perm[i] == 0){
				permutate(perm,1);
			}
			while(!demandSatisfied(solution,i)){
				solution.add(perm.clone());
			}
		}
		return solution;
	}

	private boolean demandSatisfied(ArrayList<int[]> solution, int i) {
		if(solution.size() < demand[i]){
			return false;
		}
		int nbEmpl = 0;
		for(int[] row: solution){
			nbEmpl = nbEmpl + row[i];
		}
		return nbEmpl >= demand[i];
	}

	private void permutate(int[] perm, int j) {
		while(j > 0){
			int temp = perm[0];
			for(int i = timespan - 1; i > 0; i--){
				perm[(i+1) % timespan] = perm[i];
			}
			perm[1]=temp;
			j--;
		}
	}

	@Override
	public void parseFile(File file) {
		FileReader in;
		try {
			in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			String line = null;
			while( (line = reader.readLine()) != null) {
				String[] split = line.split("-");
				timespan = trimAndParse(split[0]);
				cycle = new int[timespan];
				String[] cycleString = split[1].substring(1,split[1].length()-1).split(",");
				for(int i = 0; i < timespan; i++){
					cycle[i] = trimAndParse(cycleString[i]);
				}
				demand = new int[timespan];
				String[] demandString = split[2].substring(1,split[2].length()-1).split(",");
				for(int i = 0; i < timespan; i++){
					demand[i] = trimAndParse(demandString[i]);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
