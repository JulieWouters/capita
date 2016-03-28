package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class VNSCyclic extends VariableNeighborhoodSearch {
	public int timespan;
	public int[] cycle;
	
	@Override
	public int getCost(ArrayList<int[]> x) {
		int numberOfEmployees = x.size();
		if(getDemandShortage(x) > 0) {
			return 999999;
		} else {
			return numberOfEmployees * 100 + getCostOfDemandSurplus(x);
		}
	}
	
	private int getCostOfDemandSurplus(ArrayList<int[]> solution) {
		int[] currentWorkTotals = new int[solution.get(0).length];
		for (int i = 0; i < solution.get(0).length; ++i) {
			int totalValue = 0;
			for(int j = 0; j < solution.size(); j++) {
				totalValue = totalValue + solution.get(j)[i];
			}
		    currentWorkTotals[i] = totalValue;
		}
		int surplusCost = 0;
		for(int k = 0; k < currentWorkTotals.length; k++) {
			if(currentWorkTotals[k] > demand[k]) {
				surplusCost = surplusCost + (currentWorkTotals[k] - demand[k]) * (currentWorkTotals[k] - demand[k]);
			}
		}
		return surplusCost;
	}

	@Override
	public ArrayList<int[]> exploreNeighborhood(ArrayList<int[]> x, int l) {
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		if (l == 1) {
			bestNeighbour = getBestAddDropNeighbour(x);
		} else if (l < 5){
			bestNeighbour = getBestKSwapNeighbour(x, l-1);
		} else {
			System.out.println("actually getting here");
			bestNeighbour = getBestKSwapAndDropNeighbour(x, l - 4);
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
	
	private ArrayList<int[]> getBestKSwapAndDropNeighbour(ArrayList<int[]> x, int l) {
		ArrayList<int[]> bestNeighbour = new ArrayList<int[]>();
		int currentCost = Integer.MAX_VALUE;
		
		int n = x.size();
		int[] possibleIndices = new int[n];
		IntStream.range(0,n).forEach(val -> possibleIndices[val] = val);
		//ArrayList<Integer>[] allCombinationsOfIndices = getAllCombinationsOfIndices(possibleIndices, l);
		ArrayList<ArrayList<Integer>> allCombinationsOfIndices = new ArrayList<ArrayList<Integer>>();
		getAllCombinationsOfIndices2(possibleIndices, l, 0, new ArrayList<Integer>(), allCombinationsOfIndices);
		
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
				
				
				for(int k = 0; k < newNeighbour.size(); k++) {
					ArrayList<int[]> newNeighbour2 = (ArrayList<int[]>) newNeighbour.clone();
					newNeighbour2.remove(k);
					int newCost = getCost(newNeighbour2);
					if(newCost < currentCost) {
						bestNeighbour = newNeighbour2;
						currentCost = newCost;
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
		//ArrayList<Integer>[] allCombinationsOfIndices = getAllCombinationsOfIndices(possibleIndices, l);
		ArrayList<ArrayList<Integer>> allCombinationsOfIndices = new ArrayList<ArrayList<Integer>>();
		getAllCombinationsOfIndices2(possibleIndices, l, 0, new ArrayList<Integer>(), allCombinationsOfIndices);
		
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
	
	private void getAllCombinationsOfIndices2(int[] possibleValues, int lengthLeft, int indexToStart, ArrayList<Integer> result, ArrayList<ArrayList<Integer>> listOfResults) {
		if(lengthLeft == 0) {
			listOfResults.add(result);
		} else {
			for(int i = indexToStart; i <= possibleValues.length-lengthLeft; i++) {
				ArrayList<Integer> newResult = (ArrayList<Integer>) result.clone();
				newResult.add(possibleValues[i]);
				getAllCombinationsOfIndices2(possibleValues, lengthLeft-1, indexToStart+1, newResult, listOfResults);
			}
		}
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
					ArrayList<Integer> newList = (ArrayList<Integer>) allSubSolutions[j].clone();
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
		if (k == 1) {
			return getRandomAddDropNeighbour(x);
		} else if (k < 5) {
			return getRandomKSwapNeighbour(x, k-1);
		} else {
			return getRandomKSwapAndDropNeighbour(x, k - 4);
		}
	}

	private ArrayList<int[]> getRandomAddDropNeighbour(ArrayList<int[]> x) {
		Random generator = new Random(); 
		int index = generator.nextInt(x.size() + 1);
		if(index < x.size()) {
			ArrayList<int[]> newNeighbour = (ArrayList<int[]>) x.clone();
			newNeighbour.remove(index);
			return newNeighbour;
		} else {
			int[] permutation = cycle.clone();
			permutate(permutation, generator.nextInt(timespan-1)+1);
			ArrayList<int[]> newNeighbour = (ArrayList<int[]>) x.clone();
			newNeighbour.add(permutation.clone());
			return newNeighbour;
		}
	}

	private ArrayList<int[]> getRandomKSwapNeighbour(ArrayList<int[]> x, int k) {
		Random generator = new Random();
		int[] permutation = cycle.clone();
		ArrayList<int[]> randomNeighbour = (ArrayList<int[]>) x.clone();
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
	
	private ArrayList<int[]> getRandomKSwapAndDropNeighbour(ArrayList<int[]> x, int k) {
		Random generator = new Random();
		int[] permutation = cycle.clone();
		ArrayList<int[]> randomNeighbour = (ArrayList<int[]>) x.clone();
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
		randomNeighbour.remove(generator.nextInt(randomNeighbour.size()));
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
