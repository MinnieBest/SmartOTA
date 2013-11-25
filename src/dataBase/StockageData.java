/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import state.State;
import util.FileToolkit;

/**
 *
 * @author Yue.W
 */
public class StockageData {

    public static String CHL = "15";
    public static String SPI = "1221";
    public static String KIc;
    public static String KID;
    public static String TAR;
    public static String crypChecksum = "0000000000000000";
    public static String CNTR;
    public static String PCNTR = "00";
    public static String DATA ;
    public static String keyOTA;
    public static String phoneNb;
    public static String UDH;
    public static String PID;
    public static String DCS;
    
    public static String Profil;
    public static String TestPhoneNb;
    public static String PDU;
    public static String SDU;
    public static String ICCID;
    
    public static boolean isSecurityMsg = true;
    
    public static boolean pushSent = false;//??timer

    public static List<String> commandList = new LinkedList<String>();
    
    //public static int commandCutted =0;//for cutting the command into SDUs
    		
    public static void init(){
    	CHL = "15";
    	SPI = "1221";
        KIc = null;
        KID = null;
        TAR = null;
        crypChecksum = "0000000000000000";
        CNTR = null;//??
        PCNTR = "00";
        DATA = null;
        keyOTA = null;
        phoneNb = null;
        UDH = null;
        PID = null;
        DCS = null;
        
        Profil = null;
        TestPhoneNb = null;
        PDU = null;
        SDU = null;
        isSecurityMsg = true;
        
        commandList = null;
        
        //commandCutted=0;
    }
    
    public static String calculCPL(String hexStrData) {
        int len = hexStrData.length();
        int length = len / 2;
        String lenStr = Integer.toHexString(length);
        int res = 0;
        if (lenStr.length() < 4) {
            res = 4 - lenStr.length();
            while (res < 4) {
                lenStr = "0" + lenStr;
                res++;
            }
        }
        return lenStr;
    }

    public static String padding(String inputData) {
        String dataPadded = inputData;
        int length = inputData.length() / 2;
        int res = length % 8;
        int i = 0;
        if (res != 0) {
            while (i < (8 - res)) {
                dataPadded = dataPadded + "00";
                i++;
            }
        }
        return dataPadded;
    }

    public static String calculCheckSum(String input) {
        String strCheckSum = "";

        Cipher ecipher=null;
        SecretKey key = null;
        AlgorithmParameterSpec paramSpec = null;
        try {
            if (keyOTA.length() == 16) {
                ecipher = Cipher.getInstance("DES/CBC/NoPadding");
                byte[] iv = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                paramSpec = new IvParameterSpec(iv);
                key = new SecretKeySpec(
                        hexStringToByteArray(keyOTA), "DES");
                iv =null;
            } else if (keyOTA.length() == 32) {
                ecipher = Cipher.getInstance("DESede/CBC/NoPadding");
                byte[] iv = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                paramSpec = new IvParameterSpec(iv);
                key = new SecretKeySpec(
                        hexStringToByteArray(keyOTA+keyOTA.substring(0, 16)), "DESede");
                iv = null;
            }

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] oBuf;
            int len;
            byte[] inBuf = hexStringToByteArray(input);
            oBuf = ecipher.doFinal(inBuf);
            len = oBuf.length;
            byte[] bufCheckSum = new byte[8];
            for (int i = 7; i >= 0; i--) {
                bufCheckSum[i] = oBuf[len + i - 8];
            }
            strCheckSum = ByteArrayToHexString(bufCheckSum);
            oBuf = null;
            inBuf = null;
            bufCheckSum = null;
            
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(StockageData.class.getName()).log(Level.SEVERE, null, ex);
        }
        ecipher=null;
        key = null;
        paramSpec = null;
        return strCheckSum;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String ByteArrayToHexString(byte[] buf) {
        StringBuilder strBuf = new StringBuilder(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            strBuf.append(toHex(buf[i] >> 4));
            strBuf.append(toHex(buf[i]));
        }
        return strBuf.toString();
    }

    private static char toHex(int nibble) {
        final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return hexDigit[nibble & 0xF];
    }

    public String formPhoneNumber() {
        return phoneNb;
    }
    
	public static void changeCNTRTo(String newCNTR) throws IOException{
		changeCNTRInConfig(newCNTR);//Change the value in the database
		StockageData.CNTR = newCNTR;
	}
	
	//Change the value 'CNTR' in the database
	public static void changeCNTRInConfig(String newCNTR) throws IOException{
		FileToolkit.setCNTR(phoneNb, newCNTR);
		/*
		FileReader inPut=null;
		try {
			inPut = new FileReader("C:\\Workspace\\SmartOTA\\config\\configSIM.ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String num, text = "";
		while((in = reader.readLine()) != null){
			if(in.contains("---")||in.trim().equals("")){
				text +=in+"\r\n";
				continue;
			}
			String in2[] = in.split("/");
			num = in2[0].trim();
			if(num.equalsIgnoreCase(phoneNb)){
				in = in.replace(CNTR, newCNTR);//modify the valeur of phoneNb
				text +=in+"\r\n";
			}else {
				text +=in+"\r\n";
			}
		}
		reader.close();
		inPut.close();
		
		FileWriter writer = new FileWriter("C:\\Workspace\\SmartOTA\\config\\configSIM.ini");
        writer.write(text);
        text = null;
        writer.close();
        */
	}

	//*************Method to change the value of the parametres in the package "State"
	public static void changeStateOTACounter(String newOTACounter){
		State.otaCounter = newOTACounter;
	}
	
	public static String increaseCounterInStockageData(){
		String tmp = Long.toHexString(Long.decode("#"+StockageData.CNTR)+1);
		int len = tmp.length();
		for(int i = 0;i<(10 - len);i++){
			tmp = "0" + tmp;
		}
		return tmp.toUpperCase();
	}
}
