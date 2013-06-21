package mtps;

import java.util.ArrayList;
import mtps.ultility;
import mtps.g2;
import mtps.g3;

public class runme{
	public static void main(String[] args){
		if(args.length<2){
			System.err.println("Not enough arguments, should be runme.java [input_filename] [output_filename] {pick up type}");
			return;
		}
		
		String inputFile = args[0];
		String outputFile = args[1];
		

		fileOp fo = new fileOp();
		ArrayList<Integer[]> graph = fo.readFile(inputFile);
		int nv = fo.nVertexes;
		int nc = fo.nCircuits;
		int ng = fo.nGranularity;
		int minObj;
		ArrayList<Integer> minTimeSlot;
		
		long startTime = System.currentTimeMillis();
		if(ng == 2){
			g2 myMethod = new g2(graph, nv, nc, 2);

			myMethod.run();
			
			minObj = myMethod.objective;
			minTimeSlot = myMethod.timeSlot;
		}
		else{
			g3 myMethod = new g3(graph, nv, nc, ng);

			if(args.length == 3){
				myMethod.pickUpType = Integer.parseInt(args[2]);
			} 
			myMethod.run();
			
			minObj = myMethod.minObj;
			minTimeSlot = myMethod.minTimeSlot;
		}
		long endTime = System.currentTimeMillis();
		
		String resultContent = "";
		resultContent += "Objective = " + minObj + "\n";
		System.out.println("Time cost: "+(endTime-startTime)+"ms");
		
		for(Integer mts : minTimeSlot){
			resultContent += mts+"\n";
		}
		
		//System.out.println(resultContent);
		
		fo.writeFile(outputFile, resultContent);
	}	
}