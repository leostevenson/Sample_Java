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
package lab.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOException;

import lab.Common;
import lab.FileImpl;
import lab.FileInterface;

public class LabClient{
//	public String id = null;
//	private String indexFile = "";
//	private List<String> indexList = new ArrayList(); 
	private Common stub;
	
	public LabClient(String host) {
		/*
	    if (System.getSecurityManager() == null) {
	    	System.setSecurityManager(new SecurityManager()); 
	    } 	    
		 */
		
		try {
		    Registry registry = LocateRegistry.getRegistry(host);
		    stub = (Common) registry.lookup("Server");
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
	}

	private Integer registry(String pid, String iFilePath){
		try {
	    	System.out.println("Client: This is a client, I wanna registry.");
	    	
	    	String indexString = readIndexFile("Client-"+pid+ "\\index.txt");
	    	
	    	if (!indexString.isEmpty()){
	    		System.out.println("Client: This is a client, I have files:" + indexString);	    		
	    	}
	    	else{
	    		System.out.println("Client: This is a client, I have no file.");
	    	}
	    	
	    	int ret = stub.registry(pid, indexString);
	    	
	    	if(ret > 0){
	    		System.out.println("Client: Got id successfully, my id is " + pid + ".");
	    	}
	    	else{
	    		System.out.println("Client: Error occured, Abort.");
	    		return -1;
	    	}
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
		return 1;
	}
	
	private String searchFile(String pid, String fileName){
		String retString = "";
		try {
			retString = stub.search(fileName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (retString.length() > 0){
			System.out.println("Client: found file in client:" + retString);
		}
		else{
			System.out.println("Client: file can not be found!");
		}
		return retString;
	}
	
	private void startFileServer(String pid){
		try{
			FileInterface fi = new FileImpl("client-"+pid);
			Naming.rebind("Client-"+pid, fi);
		}
		catch(Exception e)
		{
			System.out.println("Client-" + pid + ": FileServer: "+e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("Client-" + pid + ": FileServer is running!");
	}

	private String readIndexFile(String iFilePath){
		String indexString = "";
		try {
	    	File iFile = new File(iFilePath);
			if (!iFile.exists()){
				iFile.createNewFile();
			}
			FileInputStream fstream = new FileInputStream(iFilePath);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				indexString += strLine + "|";
			}
			in.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return indexString;
	}
	
	private Integer downloadFile(String pid, String fileName){
		String tmpResStr = searchFile(pid, fileName); 
		
		if(tmpResStr.length() <= 0){
			return 0;
		}
		
		String[] resIds = tmpResStr.split("\\|");
				
		try
		{
			for (String resId : resIds){
				System.out.println("Client-" +pid+ ": start downloading from client-" +resId);
				stub.updateStatus(pid, "start downloading from client-" +resId);
				
				//Thread.currentThread().sleep(5000);//sleep for 5000 ms
				
				FileInterface fi = (FileInterface)Naming.lookup("Client-"+resId);
				
				String tmpFileName = "Client-" + resId + "\\" + fileName;
				byte[] filedata = fi.downloadFile(tmpFileName);
				
				//System.out.println("Enter the new name to file");
				//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				//String newname = br.readLine();
				
				tmpFileName = "Client-" + pid + "\\" + fileName;
				File file = new File(tmpFileName);
				BufferedOutputStream Output = new BufferedOutputStream(new FileOutputStream(tmpFileName));
				Output.write(filedata, 0, filedata.length);
				Output.flush();
				Output.close();
				
				System.out.println("Client-" +pid+ ": finish downloading from client-" +resId);
				stub.updateStatus(pid, "finish downloading from client-" +resId);
				
				String indexString = readIndexFile("Client-"+pid+ "\\index.txt");
				System.out.println("path:"+"Client-"+pid+ "\\index.txt");
				indexString += fileName + "|";
				System.out.println("Update:"+indexString);
				stub.updateIndex(pid, indexString);
				
				//Update index file in client
    			FileWriter fstream = new FileWriter("Client-" + pid + "\\index.txt", true);
    			BufferedWriter bw = new BufferedWriter(fstream);
    			bw.write(System.getProperty("line.separator") + fileName);
    			bw.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("FileServer exception:"+e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	
    public static void main(String[] args){
		String host = (args.length < 1) ? null : args[0];
		if (args.length != 4){
			//Operation code: 0 for registry, 1 for search, 2 for download
			System.out.println("Usage: client <host> <operationcode> <peerid> <directory\filename>\n");
			return;
		}
		
		LabClient obj = new LabClient(host);

		switch(args[1]) {
			case "0":
				int ret = obj.registry(args[2], args[3]);
				if (ret > 0){
					obj.startFileServer(args[2]);
				}
				break;
			
			case "1":
				obj.searchFile(args[2], args[3]);
				break;
				
			case "2":
				obj.downloadFile(args[2], args[3]);
				break;
				
			default:
				System.out.println("Client: operation code error!");
				return;
		}
    }
    
}
