package mtps;

import java.util.ArrayList;

import mtps.ultility;

public class g2{
	private ArrayList<Integer[]> graph;
	private int nVertexes;
	private int nCircuits;
	private int nGranularity;
	public ArrayList<Integer> timeSlot;
	public int objective;
	
	public g2(ArrayList<Integer[]> g, int nv, int nc, int ng){
		graph = g;
		nVertexes = nv;
		nCircuits = nc;
		nGranularity = ng;
	}
	
	public int pickUpStartVertex(){
		int sv = 0;
		int baseTS = graph.get(0)[2];
		int[][] tmpArr = new int[nVertexes][2];
		
		for(Integer[] g : graph){
			if(g[2] == baseTS){
				tmpArr[g[0]-1][0] += 1;
				tmpArr[g[1]-1][0] += 1;
			}
			else{
				tmpArr[g[0]-1][1] += 1;
				tmpArr[g[1]-1][1] += 1;			
			}
		}
		
		//System.out.println("TEST 5: "+tmpArr)
		int i = 1;
		for(int[] ta : tmpArr){
			if(Math.abs(ta[0]-ta[1])>0){
				sv = i;
				break;
			}
			i++;
		}
		return sv;
	}
	
	private int computeObjective(){
		System.out.println("Compute objective...");
		//Compute # of port for each vertex
		
		int[] portVertex = new int[nVertexes];
		for(int i=1; i<=nGranularity; i++){
			int[] countPV = new int[nVertexes];
			for(int j=0; j<nCircuits; j++){
				int cg = timeSlot.get(j);
				if(cg == i){
					countPV[graph.get(j)[0]-1] += 1;
					countPV[graph.get(j)[1]-1] += 1;
				}
			}
			for(int k=0; k<nVertexes; k++){
				if(portVertex[k] < countPV[k]){
					portVertex[k] = countPV[k];
				}
			}
		}
		
		//Compute Objective
		int objective = 0;
		for(int pv : portVertex){
			objective += pv;
		}

		System.out.println("Compute objective...DONE");
		
		return objective;	
	}
	
	public void run(){
		timeSlot = new ArrayList<Integer>();
		int startVertex = 0;
		if(graph.get(0).length==3){
			startVertex = pickUpStartVertex();
			ArrayList<Integer[]> tmpGraph = new ArrayList<Integer[]>();
			for(Integer[] g : graph){
				tmpGraph.add(new Integer[]{g[0], g[1]});
				timeSlot.add(g[2]);
			}
			graph = tmpGraph;
		}
		
		//Start Algorithm
		ultility ul = new ultility(graph, nVertexes, nCircuits, nGranularity);
		
		ul.findEularTour();
		
		graph = ul.graph;
		ArrayList<Integer[]> t = ul.tourEdge;
		timeSlot = ul.newTimeSlot;
		
		objective = computeObjective();
		
		System.out.println("#####RESULT: G=2######");
		System.out.println("obj = "+objective);
	}
}