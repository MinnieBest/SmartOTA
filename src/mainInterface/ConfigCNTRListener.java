package mainInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class ConfigCNTRListener implements ActionListener{
	public ConfigCNTRListener(){
	}
	
	public void actionPerformed(ActionEvent e){
		ConfigCNTRFrame configFrame = new ConfigCNTRFrame(new JFrame());
		configFrame.setVisible(true);
	}
}
