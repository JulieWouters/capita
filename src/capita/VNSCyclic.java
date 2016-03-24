package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<int[]> createInitialSolution() {
		ArrayList<int[]> solution = new ArrayList<int[]>();
		int[] perm = cycle;
		for(int i = 0; i < timespan; i++){
			while(perm[i] == 0){
				permutate(perm);
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

	private void permutate(int[] perm) {
		int temp = perm[0];
		for(int i = timespan - 1; i > 0; i--){
			perm[(i+1) % timespan] = perm[i];
		}
		perm[1]=temp;
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
