package mainInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import util.FileToolkit;

public class ConfigCNTRFrame extends JDialog implements Runnable {
	private static final long serialVersionUID = 5266223577576745271L;
	private JTextField counter = null;
	private JComboBox comboBox =null;
	private String numberCounter = null;
	private String numberNum = null;

	private boolean flagDone = false;

	public ConfigCNTRFrame(JFrame frame) {
		super(frame, "Configuration-Counter in SIM");

		//String[] nums = readCNTRFromFile("C:\\Workspace\\SmartOTA\\config\\configSIM.ini");
		String[] nums = FileToolkit.getAllCNTR();

		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimensions.width / 4, dimensions.height / 4);

		setLocation(dimensions.width / 2 - dimensions.width / 4,
				dimensions.height / 2 - dimensions.height / 4);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panelText = new JPanel();
		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		panelText.setBorder(paneEdge);
		panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Please select a mobile number", JLabel.TRAILING);
		panelText.add(label);
		comboBox = new JComboBox(nums);
		comboBox.setSelectedIndex(0);
		label.setLabelFor(comboBox);
		panelText.add(comboBox);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String num = (String)cb.getSelectedItem();
				//String tmp = getCounter(num);
				String tmp = FileToolkit.getValeurSIMFromNumber(num,3);
				counter.setText(tmp);
				cb = null;
				num = null;
				tmp = null;
			}
		});
		
		
		JLabel label1 = new JLabel("Please enter the new counter for this SIM", JLabel.TRAILING);
		panelText.add(label1);
		//get the first counter in the file "configSIM.ini"
		//String tmpCounter=getFirstCounter();
		String tmpCounter = FileToolkit.getFirstCNTR();
		counter = new JTextField(tmpCounter, 11);
		label1.setLabelFor(counter);
		panelText.add(counter);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInfos();
				if (!isInfosEmpty()&&isInfosRight()) {
					try {
						//outputCNTRIntoFile(numberNum,numberCounter);
						FileToolkit.setCNTR(numberNum,numberCounter);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setVisible(false);
					flagDone = true;
				}
			}
		});
		panelMain.add(panelText, BorderLayout.NORTH);
		panelMain.add(okButton, BorderLayout.SOUTH);
		this.add(panelMain);
	}

	private boolean isInfosEmpty() {
		if (this.numberCounter.equals("")) {
			String msg = "Please enter the counter SIM";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} else
			return false;
	}
	
	private boolean isInfosRight(){
		if (this.numberCounter.length() != 10) {
			String msg = "Please enter the counter SIM with 10 digits";
			String title = "Warning : the counter is not ok";
			msg_box(msg, title);
			return false;
		} else
			return true;
		
	}

	private void setInfos() {
		this.numberCounter = this.counter.getText();
		this.numberNum = (String)this.comboBox.getSelectedItem();//this.textFieldsSetUpCall.getText();
	}

	private void msg_box(String msg, String title) {
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setVisible(true);
		while (!flagDone) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
