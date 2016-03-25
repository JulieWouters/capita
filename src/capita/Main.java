package capita;

import java.io.File;
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

		//ArrayList<int[]> solution = vns.createInitialSolution();//vns.runVNSAlgorithm();
		ArrayList<int[]> solution = vns.runVNSAlgorithm();
		System.out.println("FINAL SOLUTION:");
		vns.printSolution(solution);
		System.out.println("WITH COST:");
		System.out.println(vns.getCost(solution));
	}

}
