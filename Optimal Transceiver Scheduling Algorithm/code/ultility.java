package mtps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ultility{
	public boolean fake = false;
	public ArrayList<Integer[]> oGraph;
	public ArrayList<Integer[]> graph;
	public ArrayList<Integer> tourVertex;
	public ArrayList<Integer[]> tourEdge;
	private ArrayList<Integer[]> fakeEdges;
	//public ArrayList<Integer> timeSlot;
	public ArrayList<Integer> newTimeSlot;
	private Integer[] timeSlotCombination;
	public int startVertex;
	private int nCircuits;
	private int nGranularity;
	private int nVertexes;

	/**
	 * 
	 * @param g : graph
	 * @param nv : # of Vertexes
	 * @param nc : # of circuits
	 * @param ng : # of granularity
	 * @param tsc : all possible of combination, could be null
	 */
	public ultility(ArrayList<Integer[]> g, int nv, int nc, int ng){
		oGraph = g;
		tourVertex = new ArrayList<Integer>();
		tourEdge = new ArrayList<Integer[]>();
		fakeEdges = new ArrayList<Integer[]>();
		newTimeSlot = new ArrayList<Integer>(); 
		nCircuits = nc;
		nVertexes = nv;
		nGranularity = ng;
		
	}
	
	/**
	 * int nv: number of vertexes in graph
	 * @param nv
	 */
	public ArrayList<Integer> findEularTour(){
		//findSubGraph();
		
		//System.out.println("TEST: " +timeSlotCombination.length);

		graph = (ArrayList<Integer[]>) oGraph.clone();
		beforeEular();
		
		
		//startEdges = (ArrayList<Integer[]>) graph.clone(); 
				
		//DECIDE START FROM WHICH EDGE		
		if(fake==true){
			startVertex = graph.get(nCircuits)[0];
		}
		
		if(startVertex == 0){
			startVertex = graph.get(0)[0]; 
		}
		System.out.println("TEST: startVertex="+startVertex);
		//Integer[] se = startEdges.get((int) (Math.random()*startEdges.size()));
		//System.out.println("TEST: startEdge="+se[0]+" "+se[1]);
		ArrayList<Integer[]> tmpGraph = (ArrayList<Integer[]>) graph.clone();
		tourVertex = new ArrayList<Integer>();
		newTimeSlot = new ArrayList<Integer>();
					
				
		findTour(startVertex);
		
		findTourEdge();
		System.out.println("TEST: TOUR EDGE, size="+tourEdge.size());
		
		//System.out.println("TEST: newGraph.size="+newGraph.size()+" tourEdge.size="+tourEdge.size());

		////////////////////
		/*
		graph = (ArrayList<Integer[]>) oGraph.clone();
		for(Integer[] e1 : graph){
			//if(e1[0] == 42 || e1[1] == 42){
				//System.out.println("###TEST !!!###"+ e1[0] + " " + e1[1]);
			//}
			boolean mark = false;
			for(Integer[] e2 : tourEdge){
				if(((e1[0] == e2[0]) && (e1[1]== e2[1])) || ((e1[0] == e2[1]) && (e1[1]== e2[0]))){
					mark = true;
					break;
				}
			}
			if(!mark){
				graph.remove(e1);
				System.out.println("TEST: REMOVE EDGE=["+e1[0]+","+e1[1]+"]");
			}
		}
		*/
		//////////////////
		
		assignTimeSlot();
		/*
		System.out.println("TEST: TIME SLOT size = "+newTimeSlot.size());

		int obj = computeObjective();
		
		System.out.println("TEST: objctive = "+obj);
		 */
		/*		
		System.out.println("TEST: TIME SLOT");
		for(Integer e : newTimeSlot){
			System.out.println(e);
		}

		System.out.println("TEST: TOUR EDGE, size="+tourEdge.size());
		for(Integer[] e : tourEdge){
			System.out.println(e[0]+" "+e[1]);
		}
		
		System.out.println("TEST: tourVertex size="+tourVertex.size());
		for(Integer e : tourVertex){
			System.out.println(e);
		}	
		
		System.out.println("TEST: TOUR EDGE, size="+tourEdge.size());
		for(Integer[] e : tourEdge){
			System.out.println(e[0]+" "+e[1]);
		}
	 	*/
		
		return newTimeSlot;
	}
	

	
	public void beforeEular(){
		
		ArrayList<Integer> oddDegreeVertexes = new ArrayList<Integer>();
		for(int i=1; i<=nVertexes; i++){
			int count = 0;
			for(Integer[] e : graph){
				if(e[0]==i || e[1]==i){
					count++;
				}
			}
			//System.out.println("TEST: "+i+" count="+count);
			if(count%2 == 1){
				oddDegreeVertexes.add(i);
			}
		}
		
		if(oddDegreeVertexes.size()==0){
			System.out.println("TEST: NO FAKE!");
			return;
		}
		if(oddDegreeVertexes.size()%2 == 1){
			System.err.println("Error: Can not find Eular tour!");
			return;
		}
		
		fake = true;
		int i = 0;
		while(i < oddDegreeVertexes.size()/2){
			Integer[] fakeEdge = {oddDegreeVertexes.get(i*2), oddDegreeVertexes.get(i*2+1)};
			System.out.println("ADD FAKE IN ["+fakeEdge[0]+","+fakeEdge[1]+"]");
			graph.add(fakeEdge);
			fakeEdges.add(fakeEdge);
			i++;
		}
		
		return;
	}
	
	/*
	private void findTour(int c){
		//System.out.println("TEST: TMPGRAPH.SIZE="+tmpGraph.size());
		int i = 0;
		for(Integer[] e : tmpGraph){
			if(e != null){
				int u = e[0];
				int v = e[1];				
				
				if(u == c){
					tmpGraph.set(i, null);
					//tmpGraph.remove(i);
					findTour(v);
				}
				else if(v == c){
					//tmpGraph.remove(i);
					tmpGraph.set(i, null);
					findTour(u);
				}
			}
			i++;
		}
		tourVertex.add(c);
		
		return;
	}
	*/
	
	private void findTour(int sv){
		Integer[][] arrGraph = new Integer[graph.size()][2];
		
		System.out.println("TEST: START");
		int k = 0;
		for(Integer[] g : graph){
			arrGraph[k][0] = g[0];
			arrGraph[k][1] = g[1];
			k++;
		}
		System.out.println("TEST: DONE");
		
		System.out.println("FIND TOUR...");
		Stack<Integer> s = new Stack();
		s.push(sv);
		//tourVertex.add(c);
		int v = 0;
		while(!s.isEmpty()){	
			/*
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			*/
			int cv = (int) s.peek();
			int i = 0;
			boolean mark = false;
			for(Integer[] e : arrGraph){
				if(e!=null){
					if(e[0]==cv || e[1]==cv){
						if(e[0]==cv){
							v = e[1];
						}
						else{
							v = e[0];
						}
						mark = true;
						arrGraph[i] = null;
						s.push(v);
						break;
					}
				}
				i++;
			}
			if(!mark){
				tourVertex.add((Integer) s.pop());
			}
		}
		System.out.println("FIND TOUR... DONE");
	}
		
	private void findTourEdge(){
		int cv = 0;
		for(int v : tourVertex){
			if(cv != 0){
				Integer[] e = {cv, v};
				tourEdge.add(e);
			}
			cv = v;
		}
		return;
	}
	
	/**
	 * Assgin time slot based on Eular tour
	 */
	private void assignTimeSlot(){
		ArrayList<Integer[]> arrGraph = (ArrayList<Integer[]>) oGraph.clone();

		System.out.println("TEST2: START ASSIGNING...");
		int i =0;
		for(Integer[] e : arrGraph){
			int k = 0;
			for(Integer[] te: tourEdge){
				if(te != null){
					//System.out.println("TEST = "+ te[0]+" "+te[1]+" vs "+e[0]+" "+e[1]);
					if(((int)te[0]==(int)e[0] && (int)te[1]==(int)e[1]) || ((int)te[0]==(int)e[1] && (int)te[1]==(int)e[0])){
						newTimeSlot.add(k%nGranularity+1);
						tourEdge.set(k, null);
						break;
					}
				}
				k++;
			}
		}
		System.out.println("TEST2: START ASSIGNING...DONE");
	}
	

}