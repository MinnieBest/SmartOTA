package serverSMS;

import mainInterface.MainFrame;

public class MainServerSMS {

	static MainServerSMS serverSMS;
	private MainFrame page;
	
	public MainServerSMS(MainFrame frame){
		this.serverSMS = this;
		this.page = frame;
	}
	
	public void startServer(){
		Thread serverTh = new Thread(new ServerSMSThread(8900, serverSMS));
		serverTh.start();
	}

	public void showRightLN(String Text){
		page.addTextRightLN(Text);
		page.refresh();
	}
}
