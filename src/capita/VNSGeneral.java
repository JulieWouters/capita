package capita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VNSGeneral extends VariableNeighborhoodSearch {
	public int timespan;
	public int workDayMin;
	public int workDayMax;
	public int offDayMin;
	public int offDayMax;
	public int[] demand;

	public int getCost(ArrayList<int[]> x) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<int[]> exploreNeighborhood(ArrayList<int[]> x, int l) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<int[]> shake(ArrayList<int[]> x, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<int[]> createInitialSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public void parseFile(File file) {
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
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
