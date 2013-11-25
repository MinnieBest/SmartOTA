package mainInterface;

import java.awt.event.*;

public class QuitterListener implements ActionListener{

	public QuitterListener(){
	}
	
	public void actionPerformed(ActionEvent e){
		System.exit(0);
	}
}
