package capita;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String type = args[0];
		VariableNeighborhoodSearch vns;
		if(type.equals("g")){
			vns = new VNSGeneral();
		}
		else {
			vns = new VNSCyclic();
		}
		File file = new File(args[1]);
		vns.parseFile(file);
		vns.kmax = Integer.parseInt(args[2]);

		ArrayList<int[]> solution = vns.runVNSAlgorithm();
		//comment out when using irace
		System.out.println("FINAL SOLUTION:");
		vns.printSolution(solution);
		System.out.println("WITH COST:");
		
		int cost = vns.getCost(solution);
		System.out.println(cost);
		File output = new File(args[3]);
		FileWriter out;
		try {
			out = new FileWriter(output);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(String.valueOf(cost));
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
