package mtps;

import java.io.*;
import java.util.ArrayList;

public class fileOp{
	public int nVertexes;
	public int nCircuits;
	public int nGranularity;
		
	public fileOp(){
	}
	
	public ArrayList<Integer[]> readFile(String fn){
		ArrayList<Integer[]> Graph = new ArrayList<Integer[]>();;
		try{
			BufferedReader br = new BufferedReader(new FileReader(fn));
			String strLine;
			if((strLine = br.readLine()) != null){
				String[] strArr = strLine.split(" ");
				nVertexes = Integer.parseInt(strArr[0]);
				nCircuits = Integer.parseInt(strArr[1]);
				nGranularity = Integer.parseInt(strArr[2]);
			}
			
			int i = nCircuits;
			while(i-- > 0){
				Graph.add(null);
			}
			
			i = 0;
			while((strLine = br.readLine()) != null){
				String[] strArr2 = strLine.split(" ");
				int from = Integer.parseInt(strArr2[0]);
				int to	 = Integer.parseInt(strArr2[1]);
				
				Integer[] tmpArr = {from, to};
				Graph.set(i, tmpArr);
				
				i++;
			}
			br.close();
		}catch(Exception e){
			System.err.println("Error: " + e);
		}
		
		return Graph;
	}
	
	public void writeFile(String fn, String content){
		try {
			File file = new File(fn);
			if(!file.exists()){
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fn));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}