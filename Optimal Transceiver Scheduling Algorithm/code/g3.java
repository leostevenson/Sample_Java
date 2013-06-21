package mtps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import mtps.ultility;

public class g3{
	private ArrayList<Integer[]> graph;	
	//private ArrayList<Integer[]> oGraph;
	private int nVertexes;
	private int nCircuits;
	private int nGranularity;
	private String inputFile;
	private String outputFile;
	private ArrayList<Integer> timeSlot;
	private int objective = 0;
	private int[] timeSlotCount;
	public ArrayList<Integer> minTimeSlot;
	public int minObj = 0;
	private static int loopLimit = 100;
	public int pickUpType = 0;
	//private ArrayList<int[]> gCombination;
	
	public g3(ArrayList<Integer[]> g, int nv, int nc, int ng){
		graph = g;
		//graph = (ArrayList<Integer[]>) oGraph.clone();
		nVertexes = nv;
		nCircuits = nc;
		nGranularity = ng;
		timeSlot = new ArrayList<Integer>();
		timeSlotCount = new int[nGranularity];
		/*
		gCombination = new ArrayList<int[]>();

		for(int i=1; i<nGranularity; i++){
			for(int j=i+1; j<nGranularity; j++){
				gCombination.add(new int[]{i,j});
			}
		}
		*/
	}
	
	private void randomAssign(){
		//Random coloring
		for(int i=0; i<nCircuits; i++){
			timeSlot.add(i%nGranularity+1);
			timeSlotCount[i%nGranularity] += 1;					
		}	
	}
	
	/**
	 * 
	 * @param psp : previous slot pair
	 * @return
	 */
	private int[] pickUpSlotPair(int[] psp){
		int[] slotPair = new int[2];
		if(pickUpType == 1){
			/*
			int mObj = Integer.MAX_VALUE;
			int obj = 0;
			ArrayList<Integer> tmpTimeSlot;
			
			for(int[] gc : gCombination){
				tmpTimeSlot = (ArrayList<Integer>) timeSlot.clone();
				ArrayList<Integer[]> xGraphTimeSlot = pickUpChoosenGraph(gc);
				
				ArrayList<ArrayList<Integer>> allVertexGraph = findSubGraph(xGraphTimeSlot);
		
				for(ArrayList<Integer> avg : allVertexGraph){
					ArrayList<Integer[]> tmpGraphTimeSlot = new ArrayList<Integer[]>();
					for(Integer[] xgts : xGraphTimeSlot){
						for(int v : avg){
							if(v == xgts[0] || v == xgts[1]){
								tmpGraphTimeSlot.add(xgts);
								break;
							}
						}
					}
					if(tmpGraphTimeSlot.size()>0){
						g2 myMethod = new g2(tmpGraphTimeSlot, nVertexes, tmpGraphTimeSlot.size(), 2);
						myMethod.run();
						tmpTimeSlot = updateTimeSlot(tmpTimeSlot, tmpGraphTimeSlot, myMethod.timeSlot, gc);
					}
				}
				obj = computeObjective(tmpTimeSlot);
				System.out.println("TEST6: obj="+obj+" mObj="+mObj);
				if(obj < mObj){
					slotPair = gc;
					mObj = obj;
				}
			}
			*/
			
			int i = 1;
			int maxC = 0;
			for(int tsc : timeSlotCount){
				if(maxC < tsc){
					maxC = tsc;
					slotPair[0] = i;
				}
				i++;
			}
			int minC = Integer.MAX_VALUE;
			i = 1;
			for(int tsc : timeSlotCount){
				if(psp!=null){
					if((slotPair[0]==psp[0] && i==psp[1]) || (slotPair[0]==psp[1] && i==psp[0])){
						i++;
						continue;
					}
				}
				if(minC > tsc){
					minC = tsc;
					slotPair[1] = i;
				}
				i++;
			}
		}
		else{
			slotPair[0] = (int) (Math.random()*nGranularity)+1;
			slotPair[1] = slotPair[0];
			while(slotPair[1]==slotPair[0]){
				slotPair[1] = (int) (Math.random()*nGranularity)+1;
			}
		}
		return slotPair;
	}
	
	private ArrayList<Integer[]> pickUpChoosenGraph(int[] slotPair){
		ArrayList<Integer[]> tmpGraphTimeSlot = new ArrayList<Integer[]>();
		int i = 0;
		for(Integer[] g : graph){
			int ts = timeSlot.get(i);
			if(ts == slotPair[0] || ts == slotPair[1]){
				Integer[] tgts = {g[0], g[1], ts};
				tmpGraphTimeSlot.add(tgts);
			}
			i++;
		}
		return tmpGraphTimeSlot;		
	}
	
	private ArrayList<ArrayList<Integer>> findSubGraph(ArrayList<Integer[]> inputGraph){
		ArrayList<ArrayList<Integer>> allGraph = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer[]> xGraph = (ArrayList<Integer[]>) inputGraph.clone();
		System.out.println("TEST: "+xGraph.get(0)[0]);
		Queue<Integer> qe = new LinkedList<Integer>();
		
		int count = 0;
		while(count < inputGraph.size()){
			int s = allGraph.size();
			System.out.println("TEST" + inputGraph.size() + " count="+count);
			ArrayList<Integer> subGraph = new ArrayList<Integer>();
			for(Integer[] xg : xGraph){
				if(xg != null){
					qe.add(xg[0]);
					break;
				}
			}
			
			while(qe.size()>0){
				int cv = qe.remove();
				int i = 0;
				for(Integer[] e : xGraph){
					if(e != null){
						if(e[0]==cv || e[1]==cv){
							if(e[0] == cv){
								qe.add(e[1]);
							}
							else{
								qe.add(e[0]);
							}
							count++;
							xGraph.set(i,null);
						}
					}
					i++;
				}
	
				subGraph.add(cv);
			}
			allGraph.add(subGraph);
		}
		
		System.out.println("TEST: Sub-Graphing finished!!! size = "+allGraph.size());
		/*
		for(ArrayList<Integer> sg : allGraph){
			System.out.println(sg);
		}
		*/
		
		return allGraph;
	}
	
	private ArrayList<Integer> updateTimeSlot(ArrayList<Integer> ts, ArrayList<Integer[]> tmpGraphTimeSlot, ArrayList<Integer> newTimeSlot, int[] sp){
		ArrayList<Integer> tmpTimeSlot = new ArrayList<Integer>();
		int fts = newTimeSlot.get(0);
		int p = 0;
		for(int nts : newTimeSlot){
			if(nts == fts){
				tmpTimeSlot.add(sp[0]);
			}
			else{
				tmpTimeSlot.add(sp[1]);
			}
			p++;
		}
		
		int j = 0;
		for(Integer[] tgts : tmpGraphTimeSlot){
			int i = 0;
			for(Integer[] g : graph){
				if(tgts[0] == g[0] && tgts[1] == g[1]){
					//System.out.println("TEST2: ORI:"+timeSlot.get(i)+" NOW:"+tmpTimeSlot.get(j));
					timeSlotCount[timeSlot.get(i)-1] -= 1;
					timeSlotCount[tmpTimeSlot.get(j)-1] += 1;
					ts.set(i, tmpTimeSlot.get(j));
				}
				i++;
			}
			j++;
		}
		
		return ts;
	}
	
	private int computeObjective(ArrayList<Integer> ts){
		System.out.println("Compute objective...");
		//Compute # of port for each vertex
		
		int[] portVertex = new int[nVertexes];
		for(int i=1; i<=nGranularity; i++){
			int[] countPV = new int[nVertexes];
			for(int j=0; j<nCircuits; j++){
				int cg = ts.get(j);
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
		
		return objective;	
	}
	
	public void run(){
		randomAssign();
		objective = computeObjective(timeSlot);
		minObj = objective;
		minTimeSlot = (ArrayList<Integer>) timeSlot.clone();
		System.out.println("TEST3: random obj = "+objective);
	
		//Pick up 2 time slots to run algorithm
		int loop = loopLimit;
		int[] psp = null;
		while(true){
			if(loop-- <= 0){
				break;
			}
			
			int[] sp = pickUpSlotPair(psp);
			psp = sp;
			System.out.println("###############TEST3: PAIR: "+ sp[0]+" "+sp[1]+"####################");
			
			ArrayList<Integer[]> xGraphTimeSlot = pickUpChoosenGraph(sp);
	
			ArrayList<ArrayList<Integer>> allVertexGraph = findSubGraph(xGraphTimeSlot);
	
			for(ArrayList<Integer> avg : allVertexGraph){
				System.out.println("#####TEST3:######");
				ArrayList<Integer[]> tmpGraphTimeSlot = new ArrayList<Integer[]>();
				for(Integer[] xgts : xGraphTimeSlot){
					for(int v : avg){
						if(v == xgts[0] || v == xgts[1]){
							tmpGraphTimeSlot.add(xgts);
							break;
						}
					}
				}
				if(tmpGraphTimeSlot.size()>0){
					g2 myMethod = new g2(tmpGraphTimeSlot, nVertexes, tmpGraphTimeSlot.size(), 2);
					myMethod.run();
					timeSlot = updateTimeSlot(timeSlot, tmpGraphTimeSlot, myMethod.timeSlot, sp);
				}
			}
			
			
			System.out.println("TEST3: time slot count:");
			int i = 1;
			for(int tsc : timeSlotCount){
				System.out.println("TIME SLOT "+i+" count:"+tsc);
				i++;
			}
			
			objective = computeObjective(timeSlot);
			System.out.println("TEST3: new obj = "+objective);

			if(objective<minObj){
				minObj = objective;
				minTimeSlot = (ArrayList<Integer>) timeSlot.clone();
			}
		}
		
		System.out.println("#####RESULT: G>=3######");
		System.out.println("MIN obj = "+minObj);
	}
}