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
		// TODO Auto-generated method stub
		return null;
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
				timespan = Integer.parseInt(split[0]);
				cycle = new int[timespan];
				String[] cycleString = split[1].substring(1).split(",");
				for(int i = 0; i < timespan; i++){
					cycle[i] = Integer.parseInt(cycleString[i]);
				}
				demand = new int[timespan];
				String[] demandString = split[2].substring(1).split(",");
				for(int i = 0; i < timespan; i++){
					demand[i] = Integer.parseInt(demandString[i]);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
