package serverCatTp;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import util.Util;

public class Package0348 {
	// Package for 0348
	public String CPI = "01";
	public String CHL;
	public String SPI;
	public String KIc;
	public String KID;
	public String TAR;
	public String crypChecksum = "0000000000000000";
	public String CNTR;
	public String PCNTR;
	public String DATA;
	public String keyOTA;
	public String UDH;
	public String PID;
	public String DCS;
	public boolean isSecurityMsg = true;

	private byte[] packageByte = null;

	public String calculCPL(String hexStrData) {
		int len = hexStrData.length();
		int length = len / 2;
		String res = "00";
		String lenStr = Integer.toHexString(length);
		if (lenStr.length() % 2 != 0)
			lenStr = "0" + lenStr;

		if (length <= 128) {
			res = lenStr;
		} else if (length > 128 && length <= 256) {
			res = "81" + lenStr;
		} else {
			res = "82" + lenStr;
		}
		return res;
	}

	public String padding(String inputData) {
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

	public String calculCheckSum(String input) {
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
                        Util.hexStringToByteArray(keyOTA), "DES");
            } else if (keyOTA.length() == 32) {
                ecipher = Cipher.getInstance("DESede/CBC/NoPadding");
                byte[] iv = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                paramSpec = new IvParameterSpec(iv);
                key = new SecretKeySpec(
                		Util.hexStringToByteArray(keyOTA+keyOTA.substring(0, 16)), "DESede");
            }

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] oBuf;
            int len;
            byte[] inBuf = Util.hexStringToByteArray(input);
            oBuf = ecipher.doFinal(inBuf);
            len = oBuf.length;
            byte[] bufCheckSum = new byte[8];
            for (int i = 7; i >= 0; i--) {
                bufCheckSum[i] = oBuf[len + i - 8];
            }
            strCheckSum = Util.ByteArrayToHexString(bufCheckSum);
            oBuf = null;
            inBuf =null;
            bufCheckSum =null;
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Package0348.class.getName()).log(Level.SEVERE, null, ex);
        }
        ecipher=null;
        key = null;
        paramSpec = null;
        
        
        return strCheckSum;
    } 

	public void constructPackage() {
		String CPL = calculCPL(CHL + SPI + KIc + KID + TAR + CNTR + PCNTR
				+ crypChecksum + DATA);
		String input = padding(CPI + CPL + CHL + SPI + KIc + KID + TAR + CNTR
				+ PCNTR + DATA);
		crypChecksum = calculCheckSum(input);
		String data0348 = CPI + CPL + CHL + SPI + KIc + KID + TAR + CNTR
				+ PCNTR + crypChecksum + DATA;
		packageByte = Util.hexStringToByteArray(data0348);
	}

	public byte[] getPackage() {
		if (packageByte == null)
			constructPackage();
		return packageByte;
	}

	public byte[] getPartPackage(int pos, int length) {
		byte[] res = new byte[length];
		System.arraycopy(packageByte, pos, res, 0, length);
		return res;
	}
}
