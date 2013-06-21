package lab;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Common extends Remote {
    String say() throws RemoteException;
    
    public Integer registry(String pid, String indexString) throws RemoteException;
    
    public String search(String fileName) throws RemoteException;
    
    public Integer updateStatus(String pid, String strStatus) throws RemoteException;
    
    public Integer updateIndex(String pid, String strIndex) throws RemoteException;
}
