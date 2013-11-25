package serverCatTp;

/**
*
* @author Yue.W
*/

import mainInterface.MainFrame;
import captor.Captor;

public class MainServer {

	private MainFrame page;
	
	public MainServer(MainFrame frame){//pageMainFrame frame
		
		this.page = frame;
	}
	
	public void startServer(){
		
		Captor captor = new Captor();
		captor.getNetworkInterfaces();
		
		Thread serverTh = new Thread(new ServerThread(9876, page));//pagenew Thread(new ServerThread(9876, page));
		serverTh.start();
	}
}