package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class VNSGeneral extends VariableNeighborhoodSearch {
	public int timespan;
	public int workDayMin;
	public int workDayMax;
	public int offDayMin;
	public int offDayMax;
	public int[] demand;

	public int getCost(ArrayList<int[]> x) {
		int demandShortage = getDemandShortage(x);
		int demandSurplus = getDemandSurplus(x);
		int numberOfEmployees = x.size();
		
		return demandShortage * 9999999 + demandSurplus * 10 + numberOfEmployees * 100;
	}

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
		for(int i = 0; i < x.size() + 1; i++) {
			if(i < x.size()) {
				x.remove(i);
				allNeighbours.add(x);
			} else {
				int[] newWorkerSchedule = getRandomValidWorkerSchedule(x.get(0).length);
				x.add(newWorkerSchedule);
				allNeighbours.add(x);
			}
		}
		
		return allNeighbours;
	}

	private ArrayList<ArrayList<int[]>> getAllKSwapNeighbours(ArrayList<int[]> x, int l) {
		ArrayList<ArrayList<int[]>> allNeighbours = new ArrayList<ArrayList<int[]>>();
		
		for(int rowIndex1 = 0; rowIndex1 < x.size(); rowIndex1++) {
			for(int rowIndex2 = 0; rowIndex2 < x.size(); rowIndex2++) {
				for(int columnIndex1 = 0; columnIndex1 < x.get(0).length + 1 - l; columnIndex1++) {
					for(int columnIndex2 = 0; columnIndex2 < x.get(0).length + 1 - l; columnIndex2++) {
						int workerScheduleSize = x.get(0).length;
						ArrayList<int[]> nextNeighbour = new ArrayList<int[]>();
						for (int i = 0; i < x.size(); i++) {
							int[] newWorkerSchedule = new int[workerScheduleSize];
							for (int j = 0; j < workerScheduleSize; j++) {
								if(i == rowIndex1 && j >= columnIndex1 && j < columnIndex1 + l) {
									int offset = j - columnIndex1;
									newWorkerSchedule[j] = x.get(rowIndex2)[columnIndex2 + offset];
								} else if(i == rowIndex2 && j >= columnIndex2 && j < columnIndex2 + l) {
									int offset = j - columnIndex2;
									newWorkerSchedule[j] = x.get(rowIndex1)[columnIndex1 + offset];
								} else {
									newWorkerSchedule[j] = x.get(i)[j];
								}
							}
							nextNeighbour.add(newWorkerSchedule);
						}
						
						allNeighbours.add(nextNeighbour);
					}
				}
			}
		}
		
		return allNeighbours;
	}

	public ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		if (k < 3) {
			return getRandomKSwapNeighbour(x, k);
		} else if (k == 3) {
			return getRandomAddDropNeighbour(x);
		} else {
			return getRandomKSwapNeighbour(x, k-1);
		}
	}

	private ArrayList<int[]> getRandomKSwapNeighbour(ArrayList<int[]> x, int k) {
		Random generator = new Random(); 
		int rowIndex1 = generator.nextInt(x.size());
		int rowIndex2 = generator.nextInt(x.size());
		while (rowIndex2 == rowIndex1) {
			rowIndex2 = generator.nextInt(x.size());
		}
		int columnIndex1 = generator.nextInt(x.get(0).length + 1 - k);
		int columnIndex2 = generator.nextInt(x.get(0).length + 1 - k);
		
		int workerScheduleSize = x.get(0).length;
		ArrayList<int[]> randomNeighbour = new ArrayList<int[]>();
		for (int i = 0; i < x.size(); i++) {
			int[] newWorkerSchedule = new int[workerScheduleSize];
			for (int j = 0; j < workerScheduleSize; j++) {
				if(i == rowIndex1 && j >= columnIndex1 && j < columnIndex1 + k) {
					int offset = j - columnIndex1;
					newWorkerSchedule[j] = x.get(rowIndex2)[columnIndex2 + offset];
				} else if(i == rowIndex2 && j >= columnIndex2 && j < columnIndex2 + k) {
					int offset = j - columnIndex2;
					newWorkerSchedule[j] = x.get(rowIndex1)[columnIndex1 + offset];
				} else {
					newWorkerSchedule[j] = x.get(i)[j];
				}
			}
			randomNeighbour.add(newWorkerSchedule);
		}
		
		return randomNeighbour;
	}

	private ArrayList<int[]> getRandomAddDropNeighbour(ArrayList<int[]> x) {
		Random generator = new Random(); 
		int index = generator.nextInt(x.size() + 1);
		if(index < x.size()) {
			x.remove(index);
			return x;
		} else {
			int[] newWorkerSchedule = getRandomValidWorkerSchedule(x.get(0).length);
			x.add(newWorkerSchedule);
			return x;
		}
	}
	
	public int[] getRandomValidWorkerSchedule(int size) {
		int[] newWorkerSchedule = new int[size];
		Random generator = new Random();
		ArrayList<Integer> lastSequence = new ArrayList<Integer>();
		for(int i = 0; i < size; i++) {
			int nextNumber = generator.nextInt(2);
			if(lastSequence.size() == 0) {
				newWorkerSchedule[i] = nextNumber;
				lastSequence.add(nextNumber);
			} else if (lastSequence.get(0) == 0) {
				if(lastSequence.size() < offDayMin) {
					newWorkerSchedule[i] = 0;
					lastSequence.add(new Integer(0));
				} else if(lastSequence.size() == offDayMax) {
					newWorkerSchedule[i] = 1;
					lastSequence = new ArrayList<Integer>();
					lastSequence.add(1);
				} else if(nextNumber == 0) {
					newWorkerSchedule[i] = 0;
					lastSequence.add(0);
				} else {
					newWorkerSchedule[i] = 1;
					lastSequence = new ArrayList<Integer>();
					lastSequence.add(1);
				}
			} else if (lastSequence.get(0) == 1) {
				if(lastSequence.size() < workDayMin) {
					newWorkerSchedule[i] = 1;
					lastSequence.add(1);
				} else if(lastSequence.size() == workDayMax) {
					newWorkerSchedule[i] = 0;
					lastSequence = new ArrayList<Integer>();
					lastSequence.add(0);
				} else if(nextNumber == 1) {
					newWorkerSchedule[i] = 1;
					lastSequence.add(1);
				} else {
					newWorkerSchedule[i] = 0;
					lastSequence = new ArrayList<Integer>();
					lastSequence.add(0);
				}
			}
		}
		
		return newWorkerSchedule;
	}

	public ArrayList<int[]> createInitialSolution() {
		ArrayList<int[]> initialSolution = new ArrayList<int[]>();
		while(!fulfillsDemand(initialSolution)) {
			int[] newWorkerSchedule = getRandomValidWorkerSchedule(demand.length);
			initialSolution.add(newWorkerSchedule);
		}
		
		return initialSolution;
	}
	
	public int getDemandSurplus(ArrayList<int[]> solution) {
		int[] currentWorkTotals = new int[solution.get(0).length];
		for (int i = 0; i < solution.get(0).length; ++i) {
			int totalValue = 0;
			for(int j = 0; j < solution.size(); j++) {
				totalValue = totalValue + solution.get(j)[i];
			}
		    currentWorkTotals[i] = totalValue;
		}
		int surplus = 0;
		for(int k = 0; k < currentWorkTotals.length; k++) {
			if(currentWorkTotals[k] > demand[k]) {
				surplus = surplus + currentWorkTotals[k] - demand[k];
			}
		}
		return surplus;
	}
	
	public int getDemandShortage(ArrayList<int[]> solution) {
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
	
	public boolean fulfillsDemand(ArrayList<int[]> solution) {
		int[] currentWorkTotals = new int[solution.get(0).length];
		for (int i = 0; i < solution.get(0).length; ++i) {
			int totalValue = 0;
			for(int j = 0; j < solution.size(); j++) {
				totalValue = totalValue + solution.get(j)[i];
			}
		    currentWorkTotals[i] = totalValue;
		}
		for(int k = 0; k < currentWorkTotals.length; k++) {
			if(currentWorkTotals[k] < demand[k]) {
				return false;
			}
		}
		return true;
	}

	public void parseFile(File file) {
		FileReader in;
		try {
			in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			String line = null;
			while( (line = reader.readLine()) != null) {
				String[] split = line.split("-");
				timespan = trimAndParse(split[0]);
				workDayMin = trimAndParse(split[1]);
				workDayMax = trimAndParse(split[2]);
				offDayMin = trimAndParse(split[3]);
				offDayMax = trimAndParse(split[4]);
				demand = new int[timespan];
				String[] array = split[5].substring(1,split[5].length()-1).split(",");
				for(int i = 0; i < timespan; i++){
					demand[i] = trimAndParse(array[i]);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
