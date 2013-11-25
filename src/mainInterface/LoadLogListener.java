package mainInterface;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadLogListener implements ActionListener{

	ServerOTA server;
	public LoadLogListener(ServerOTA server){
		this.server = server;
		
	}
	public void actionPerformed(ActionEvent e){
		FileDialog fileDialog = new FileDialog(new Frame(),"Open");
		fileDialog.setVisible(true);
		if(fileDialog.getFile() != null){
			String dir = fileDialog.getDirectory();
			if(!dir.endsWith(File.separator)){
				dir +=File.separator;
			}
			String file = fileDialog.getFile();
			server.loadLog(dir+file);
		}
	}
}
