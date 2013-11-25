package mainInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class ConfigDeclareSIMFrame extends JDialog implements Runnable {
	private static final long serialVersionUID = 5266223577576745271L;
	//private static String fileNameA = "C:\\Workspace\\SmartOTA\\config\\configSIM.ini";
	private JTextField textFieldsnumber = null;
	private JTextField textFieldsICCID = null;
	private JTextField textFieldsKeyOTA = null;
	private JTextField textFieldsCounter = null;
	private JComboBox textFieldsPE = null;

	private String textNumber = null;
	private String textICCID = null;
	private String textKeyOTA = null;
	private String textCounter = null;
	private String textPE = null;

	private boolean flagDone = false;

	public ConfigDeclareSIMFrame(JFrame frame) {
		super(frame, "Configuration-Declare a new SIM in SmartOTA");

		//String[] nums = readInfoFromFile(fileNameA);
		String[] nums = FileToolkit.getFirstSIM();
		
		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimensions.width / 4, dimensions.height / 4+100);

		setLocation(dimensions.width / 2 - dimensions.width / 4,
				dimensions.height / 2 - dimensions.height / 4);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panelText = new JPanel();
		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		panelText.setBorder(paneEdge);
		panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
		
		JLabel label0 = new JLabel("Telephone number", JLabel.TRAILING);
		panelText.add(label0);
		textFieldsnumber = new JTextField(nums[0], 11);
		label0.setLabelFor(textFieldsnumber);
		panelText.add(textFieldsnumber);
		
		JLabel label1 = new JLabel("ICCID", JLabel.TRAILING);
		panelText.add(label1);
		textFieldsICCID = new JTextField(nums[1], 11);
		label1.setLabelFor(textFieldsICCID);
		panelText.add(textFieldsICCID);
		
		JLabel label2 = new JLabel("OTA Key", JLabel.TRAILING);
		panelText.add(label2);
		textFieldsKeyOTA = new JTextField(nums[2], 11);
		label2.setLabelFor(textFieldsKeyOTA);
		panelText.add(textFieldsKeyOTA);
		
		JLabel label3 = new JLabel("Counter in the SIM", JLabel.TRAILING);
		panelText.add(label3);
		textFieldsCounter = new JTextField(nums[3], 11);
		label3.setLabelFor(textFieldsCounter);
		panelText.add(textFieldsCounter);
		
		JLabel label4 = new JLabel("Please select a Profil Electric", JLabel.TRAILING);
		panelText.add(label4);
		//String[] PEs = readPEFromFile(fileNameA);
		String[] PEs = FileToolkit.getAllPE();
		
		textFieldsPE = new JComboBox(PEs);
		PEs = null;
		//textFieldsPE.setSelectedIndex(0);
		textFieldsPE.setSelectedItem(FileToolkit.getValeurSIMFromNumber(nums[0], 4));
		label4.setLabelFor(textFieldsPE);
		panelText.add(textFieldsPE);
		textFieldsPE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				textPE = (String)cb.getSelectedItem();
				cb = null;
			}
		});

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInfos();
				if (!isInfosEmpty()&&isInfosRight()) {
					try {
						//outputIntoFile();
						FileToolkit.addNewSIM(textNumber, textICCID, textKeyOTA, textCounter, textPE);
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
	
	/*
	private String[] readInfoFromFile(String fileName) {
		String[] res = null;
		InputStream inS=null;
		try {
			inS = new FileInputStream(fileName);
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				
				tmp = line.split("/");
				if(tmp[0] != null){
					res = tmp;
					break;
				}
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	*/
	private boolean isInfosEmpty() {
		if (this.textNumber.equals("")) {
			String msg = "Please enter the telephone number";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} else if (this.textICCID.equals("")) {
			String msg = "Please enter the ICCID";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} else if (this.textKeyOTA.equals("")) {
			String msg = "Please enter the OTA Key";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} else if (this.textCounter.equals("")) {
			String msg = "Please enter the counter";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} else if (this.textPE.equals("")) {
			String msg = "Please enter the Profil Electic";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		} 
		else
			return false;
	}
	
	private boolean isInfosRight(){
		if (this.textCounter.length() != 10) {
			String msg = "Please enter the counter SIM with 10 digits";
			String title = "Warning : the counter is not ok";
			msg_box(msg, title);
			return false;
		} else if (!this.textNumber.startsWith("+33")) {
			String msg = "Please enter the telephone number started with '+33'";
			String title = "Warning : the format of number is not ok";
			msg_box(msg, title);
			return false;
		}else if (FileToolkit.isNumberExist(this.textNumber)) {
			String msg = "Please enter a new number";
			String title = "Warning : this number exists in SmartOTA";
			msg_box(msg, title);
			return false;
		}
		else
			return true;
		
	}

	private void setInfos() {
		this.textNumber = this.textFieldsnumber.getText();
		this.textICCID = this.textFieldsICCID.getText();
		this.textKeyOTA = this.textFieldsKeyOTA.getText();
		this.textCounter = this.textFieldsCounter.getText();
		this.textPE = (String)this.textFieldsPE.getSelectedItem();
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
