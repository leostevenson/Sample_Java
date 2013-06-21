/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 * -Redistributions of source code must retain the above copyright  
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright 
 *  notice, this list of conditions and the following disclaimer in 
 *  the documentation and/or other materials provided with the 
 *  distribution.
 *  
 * Neither the name of Oracle or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that Software is not designed, licensed or 
 * intended for use in the design, construction, operation or 
 * maintenance of any nuclear facility.
 */
package lab.server;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import lab.Common;
	
public class LabServer implements Common {
	private int numClients = 0;
	private int clientLimit  = 3;
	private String clientFile = "clients.txt";
	private String indexFile = "index.txt";
	
    public LabServer() {
    	try {  
        	File cFile = new File(clientFile); 
        	File iFile = new File(indexFile);
    		if (cFile.exists()){
    			cFile.delete();
    		}
    		if (iFile.exists()){
    			iFile.delete();
    		}
        	cFile.createNewFile();
        	iFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
	@Override
	public String say() throws RemoteException {
		String retString = "Hello Clinet " + numClients;
		// TODO Auto-generated method stub
		return retString;
	}

	@Override
	public Integer registry(String pid, String indexString) throws RemoteException {
    	if (numClients < clientLimit){
    		++numClients;
    		try{
    			// Create file 
    			FileWriter cfstream = new FileWriter(clientFile, true);
    			BufferedWriter cbw = new BufferedWriter(cfstream);
    			
    			cbw.write(pid + ":registed" + System.getProperty("line.separator"));
    			//Close the output stream
    			cbw.close();
    			
				FileWriter ifstream = new FileWriter(indexFile, true);
				BufferedWriter ibw = new BufferedWriter(ifstream);
				ibw.write(pid + ":" + indexString + System.getProperty("line.separator"));
				ibw.close();
    		}catch (Exception e){//Catch exception if any
    			System.err.println("Error: " + e.getMessage());
    		}  
			return 1;
    	}
    	return -1;
	}

    public static void main(String args[]) {
    	//new LabServer();
    	
		try {
		    LabServer obj = new LabServer();
		    Common stub = (Common) UnicastRemoteObject.exportObject(obj, 0);
	
		    // Bind the remote object's stub in the registry
		    Registry registry = LocateRegistry.getRegistry();
		    registry.bind("Server", stub);
	
		    System.err.println("Server ready");
		} catch (Exception e) {
		    System.err.println("Server exception: " + e.toString());
		    e.printStackTrace();
		}
    }

	@Override
	public String search(String fileName) throws RemoteException {
		String res = "";
		try {
			FileInputStream fstream = new FileInputStream(indexFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				if(strLine.indexOf(fileName) > -1){
					String[] tmpStrArr = strLine.split(":");
					res +=  tmpStrArr[0] + "|";
				}
			}
			in.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return res;
	}

	@Override
	public Integer updateStatus(String pid, String strStatus) throws RemoteException {
		try {
			FileInputStream fstream = new FileInputStream(clientFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strContents = "";
			String strLine = "";
			while ((strLine = br.readLine()) != null)   {
				String strId = strLine.split(":")[0];
				if(strId.equals(pid)){
					strLine = strId + ":" + strStatus;
				}
				strContents += strLine + System.getProperty("line.separator");
			}
			br.close();
			
			FileWriter cfstream = new FileWriter(clientFile);
			BufferedWriter bw = new BufferedWriter(cfstream);
			bw.write(strContents);
			bw.close();
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public Integer updateIndex(String pid, String strIndex)	throws RemoteException {
		// TODO Auto-generated method stub
		try {
			FileInputStream fstream = new FileInputStream(indexFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strContents = "";
			String strLine = "";
			while ((strLine = br.readLine()) != null)   {
				String strId = strLine.split(":")[0];
				if(strId.equals(pid)){
					strLine = strId + ":" + strIndex;
				}
				strContents += strLine + System.getProperty("line.separator");
			}
			br.close();
			
			FileWriter cfstream = new FileWriter(indexFile);
			BufferedWriter bw = new BufferedWriter(cfstream);
			bw.write(strContents);
			bw.close();
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
}
