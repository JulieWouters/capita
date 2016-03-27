package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

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
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		
		if (l < 1) {
			bestNeighbour = getBestKSwapNeighbour(x, l);
		} else if (l == 1) {
			bestNeighbour = getBestAddDropNeighbour(x);
		} else {
			bestNeighbour = getBestKSwapNeighbour(x, kmax+1-l);
		}
		
		return bestNeighbour;
	}

	private ArrayList<int[]> getBestAddDropNeighbour(ArrayList<int[]> x) {
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		int currentCost = Integer.MAX_VALUE;
		
		for(int i = 0; i < x.size() + 1; i++) {
			if(i < x.size()) {
				ArrayList<int[]> newNeighbour = (ArrayList<int[]>) x.clone();
				newNeighbour.remove(i);
				int newCost = getCost(newNeighbour);
				if(newCost < currentCost) {
					currentCost = newCost;
					bestNeighbour = newNeighbour;
				}
			} else {
				for(int j = 0; j < cycle.length; j++) {
					int[] permutation = cycle.clone();
					permutate(permutation, j);
					ArrayList<int[]> newNeighbour = (ArrayList<int[]>) x.clone();
					newNeighbour.add(permutation.clone());
					int newCost = getCost(newNeighbour);
					if(newCost < currentCost) {
						currentCost = newCost;
						bestNeighbour = newNeighbour;
					}
				}
			}
		}
		
		return bestNeighbour;
	}

	private ArrayList<int[]> getBestKSwapNeighbour(ArrayList<int[]> x, int l) {
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		int currentCost = Integer.MAX_VALUE;
		
		int n = x.size();
		int[] possibleIndices = new int[n];
		IntStream.range(0,n).forEach(val -> possibleIndices[val] = val);
		ArrayList<Integer>[] allCombinationsOfIndices = getAllCombinationsOfIndices(possibleIndices, l);
		
		int[][] possiblePermutations = new int[cycle.length][cycle.length];
		int [] initialPermutation = cycle.clone();
		for(int i = 0; i < cycle.length; i++) {
			permutate(initialPermutation, 1);
			possiblePermutations[i] = initialPermutation;
		}
		ArrayList<int[]>[] allCombinationsOfPermutations = getAllCombinationsOfPermutations(possiblePermutations, l);
		
		for(ArrayList<Integer> indicesToChange : allCombinationsOfIndices) {
			for(ArrayList<int[]> newPermutationsToAdd : allCombinationsOfPermutations) {
				ArrayList<int[]> newNeighbour = (ArrayList<int[]>) x.clone();
				for(int j = newNeighbour.size() - 1; j >= 0; j--) {
					if(indicesToChange.contains(j)) {
						newNeighbour.remove(j);
					}
				}
				int indexOfPermutationToAdd = 0;
				while(newNeighbour.size() < x.size()) {
					newNeighbour.add(newPermutationsToAdd.get(indexOfPermutationToAdd));
					indexOfPermutationToAdd++;
				}
				
				int newCost = getCost(newNeighbour);
				if(newCost < currentCost) {
					bestNeighbour = newNeighbour;
					currentCost = newCost;
				}
			}
		}
		return bestNeighbour;
	}
	
	private ArrayList<Integer>[] getAllCombinationsOfIndices(int[] possibleValues, int length) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> allSolutions[]	 = new ArrayList[(int)Math.pow(possibleValues.length, length)];
		
		if(length == 1) {
			int index = 0;
			for(int value : possibleValues) {
				ArrayList<Integer> newList = new ArrayList<Integer>();
				newList.add(value);
				allSolutions[index] = newList;
				index++;
			}
			return allSolutions;
		} else {
			ArrayList<Integer>[] allSubSolutions = getAllCombinationsOfIndices(possibleValues, length - 1);
			
			int solutionArrayIndex = 0;
			for(int i = 0; i < possibleValues.length; i++) {
				for(int j = 0; j < allSubSolutions.length; j++) {
					ArrayList<Integer> newList = allSubSolutions[j];
					newList.add(possibleValues[i]);
					allSolutions[solutionArrayIndex] = newList;
					solutionArrayIndex++;
				}
			}
			return allSolutions;
		}
	}
	
	private ArrayList<int[]>[] getAllCombinationsOfPermutations(int[][] possibleValues, int length) {
		@SuppressWarnings("unchecked")
		ArrayList<int[]> allSolutions[]	 = new ArrayList[(int)Math.pow(possibleValues.length, length)];
		
		if(length == 1) {
			int index = 0;
			for(int[] value : possibleValues) {
				ArrayList<int[]> newList = new ArrayList<int[]>();
				newList.add(value);
				allSolutions[index] = newList;
				index++;
			}
			return allSolutions;
		} else {
			ArrayList<int[]>[] allSubSolutions = getAllCombinationsOfPermutations(possibleValues, length - 1);
			
			int solutionArrayIndex = 0;
			for(int i = 0; i < possibleValues.length; i++) {
				for(int j = 0; j < allSubSolutions.length; j++) {
					ArrayList<int[]> newList = allSubSolutions[j];
					newList.add(possibleValues[i]);
					allSolutions[solutionArrayIndex] = newList;
					solutionArrayIndex++;
				}
			}
			return allSolutions;
		}
	}

	@Override
	public ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		if (k < 1) {
			return getRandomKSwapNeighbour(x, k);
		} else if (k == 1) {
			return getRandomAddDropNeighbour(x);
		} else {
			return getRandomKSwapNeighbour(x, kmax+1-k);
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
