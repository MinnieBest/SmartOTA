package util;

public class Util {

	public static String ByteArrayToHexString(final byte[] buf) {
		StringBuilder strBuf = new StringBuilder(buf.length * 2);
		for (int i = 0; i < buf.length; i++) {
			strBuf.append(toHex(buf[i] >> 4));
			strBuf.append(toHex(buf[i]));
		}
		return strBuf.toString();
	}

	private static char toHex(int nibble) {
		final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		return hexDigit[nibble & 0xF];
	}

	public static short calculChecksum(final byte[] data, int dataLength) {
		long checksum = 0;
		int i = 0;
		while (dataLength > 1) {
			long word = (short) (data[i] & 0xFF);
			word = (((word << 8) & 0xFF00) + (short) (data[i + 1] & 0xFF));
			checksum += word;
			i += 2;
			dataLength -= 2;
		}
		if (dataLength > 0) {
			short word = (short) (data[i] & 0xFF);
			checksum += (word << 8) & 0xFF00;
		}
		while (checksum >> 16 > 0) {
			checksum = (checksum & 0xFFFF) + (checksum >> 16);
		}
		return (short) ~checksum;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
