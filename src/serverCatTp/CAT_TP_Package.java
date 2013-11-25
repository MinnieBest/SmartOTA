package serverCatTp;

import util.Util;

public class CAT_TP_Package implements MsgType {
	// Package for CAT-TP

	private byte[] packageBytes;

	public void Init(){
		packageBytes = null;
	}
	public CAT_TP_Package() {
	}

	public CAT_TP_Package(int size) {
		this.packageBytes = new byte[size];
		for (int i = 0; i < packageBytes.length; i++) {
			packageBytes[i] = 0;
		}
	}

	public CAT_TP_Package(final byte[] packageBytes) {
		this.packageBytes = packageBytes;
	}

	public CAT_TP_Package(final byte[] packageBytes, int pos, short length) {
		this.packageBytes = new byte[length];
		for (int i = 0; i < length; i++) {
			this.packageBytes[i] = packageBytes[i + pos];
		}
	}

	// encapsulate a 0348 Package in a package CAT-TP
	public void encapsulate(byte msgType, Package0348 package0348, int seqNb,
			int ackNb, short sourcePort, short destPort) {
		byte[] bufHeaderCatTP = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x01, (byte) 0x00, (byte) 0x00 };
		package0348.constructPackage();
		byte data[] = package0348.getPackage();
		bufHeaderCatTP[0] = msgType;
		byte[] bufPackage = new byte[bufHeaderCatTP.length + data.length];
		System.arraycopy(bufHeaderCatTP, 0, bufPackage, 0,
				bufHeaderCatTP.length);
		System.arraycopy(data, 0, bufPackage, bufHeaderCatTP.length,
				data.length);
		this.packageBytes = bufPackage;
		setSeqNb(seqNb);
		setACKNb(ackNb);
		setSourcePort(sourcePort);
		setDestinationPort(destPort);
		setDataLength();
		setCheckSum();
		bufHeaderCatTP = null;
		data= null;
		bufPackage=null;
	}

	// encapsulate a byte array in a package CAT-TP
	// Test
	public void encapsulate(byte msgType, byte[] data, int seqNb,
			int ackNb, short sourcePort, short destPort) {
		byte[] bufHeaderCatTP = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x01, (byte) 0x00, (byte) 0x00 };

		bufHeaderCatTP[0] = msgType;
		byte[] bufPackage = new byte[bufHeaderCatTP.length + data.length];
		System.arraycopy(bufHeaderCatTP, 0, bufPackage, 0,
				bufHeaderCatTP.length);
		System.arraycopy(data, 0, bufPackage, bufHeaderCatTP.length,
				data.length);
		this.packageBytes = bufPackage;
		setSeqNb(seqNb);
		setACKNb(ackNb);
		setSourcePort(sourcePort);
		setDestinationPort(destPort);
		setDataLength();
		setCheckSum();
		bufHeaderCatTP =null;
		bufPackage = null;
	}

	//encapsulate a simple ACK OR SYN in a package CAT-TP
	// Test
	public void encapsulate(byte msgType, short seqNb, int ackNb,
			short sourcePort, short destPort) {
		byte[] bufHeaderCatTP;
		if ((msgType & SYN_PDU) == SYN_PDU) {
			byte[] buf = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x17,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x77,
					(byte) 0x05, (byte) 0xDC, (byte) 0x00 };
			bufHeaderCatTP = buf;
			buf = null;
		} else if ((msgType & RST_PDU) == RST_PDU) {
			byte[] buf = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
					(byte) 0x00, (byte) 0x00, (byte) 0x00 };
			bufHeaderCatTP = buf;
			buf=null;
		} else {
			byte[] buf = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
					(byte) 0x00, (byte) 0x00 };
			bufHeaderCatTP = buf;
			buf = null;
		}

		bufHeaderCatTP[0] = msgType;
		this.packageBytes = bufHeaderCatTP;

		setSeqNb(seqNb);
		setACKNb(ackNb);
		setSourcePort(sourcePort);
		setDestinationPort(destPort);
		setCheckSum();
		bufHeaderCatTP= null;
	}

	public byte[] getPackageBytes() {
		return this.packageBytes;
	}

	public void setType(byte type) {
		packageBytes[0] = type;
	}

	public void addType(byte type) {
		packageBytes[0] += type;
	}

	public void setSourcePort(short port) {
		packageBytes[4] = (byte) ((port >> 8) & 0xFF);
		packageBytes[5] = (byte) (port & 0xFF);
	}

	public void setDestinationPort(short port) {
		packageBytes[6] = (byte) ((port >> 8) & 0xFF);
		packageBytes[7] = (byte) (port & 0xFF);
	}

	public void setDataLength(short length) {
		packageBytes[8] = (byte) ((length >> 8) & 0xFF);
		packageBytes[9] = (byte) (length & 0xFF);
	}

	public void setDataLength() {
		short length = (short) (packageBytes.length - getHeaderLength());
		packageBytes[8] = (byte) ((length >> 8) & 0xFF);
		packageBytes[9] = (byte) (length & 0xFF);
	}

	// Test
	public void setSeqNb(int seqNb) {
		packageBytes[10] = (byte) ((seqNb >> 8) & 0xFF);
		packageBytes[11] = (byte) (seqNb & 0xFF);
	}

	public void setACKNb(int ackNb) {
		packageBytes[12] = (byte) ((ackNb >> 8) & 0xFF);
		packageBytes[13] = (byte) (ackNb & 0xFF);
	}

	public byte getHeaderLength() {
		return packageBytes[3];
	}

	public short getSourcePort() {
		short port = (short) (packageBytes[4] & 0xFF);
		port = (short) (((port << 8) & 0xFF00) + (packageBytes[5] & 0xFF));
		return port;
	}

	public short getDesPort() {
		short port = (short) (packageBytes[6] & 0xFF);
		port = (short) (((port << 8) & 0xFF00) + (packageBytes[7] & 0xFF));
		return port;
	}

	public short getDataLength() {
		short lenght = (short) (packageBytes[8] & 0xFF);
		lenght = (short) (((lenght << 8) & 0xFF00) + (packageBytes[9] & 0xFF));
		return lenght;
	}

	// Test
	public int getSequenceNb() {
		int nb = (int) (packageBytes[10] & 0xFF);
		nb = (int) (((nb << 8) & 0xFF00) + (packageBytes[11] & 0xFF));
		return nb;
	}

	public int getACKNb() {
		int nb = (int) (packageBytes[12] & 0xFF);
		nb = (int) (((nb << 8) & 0xFF00) + (packageBytes[13] & 0xFF));
		return nb;
	}

	public short getWindowSize() {
		short size = (short) (packageBytes[14] & 0xFF);
		size = (short) (((size << 8) & 0xFF00) + (packageBytes[15] & 0xFF));
		return size;
	}

	public short getCheckSum() {
		short cs = (short) (packageBytes[16] & 0xFF);
		cs = (short) (((cs << 8) & 0xFF00) + (packageBytes[17] & 0xFF));
		return cs;
	}

	public short getMaxPDU() {
		short maxPDU = (short) (packageBytes[18] & 0xFF);
		maxPDU = (short) (((maxPDU << 8) & 0xFF00) + (packageBytes[19] & 0xFF));
		return maxPDU;
	}

	public short getMaxSDU() {
		short maxSDU = (short) (packageBytes[20] & 0xFF);
		maxSDU = (short) (((maxSDU << 8) & 0xFF00) + (packageBytes[21] & 0xFF));
		return maxSDU;
	}

	public boolean isSYN() {
		if ((packageBytes[0] & SYN_PDU) == SYN_PDU)
			return true;
		else
			return false;
	}

	public boolean isACK() {
		if ((packageBytes[0] & ACK_PDU) == ACK_PDU)
			return true;
		else
			return false;
	}

	public boolean isEACK() {
		if ((packageBytes[0] & EACK_PDU) == EACK_PDU)
			return true;
		else
			return false;
	}

	public boolean isRST() {
		if ((packageBytes[0] & RST_PDU) == RST_PDU)
			return true;
		else
			return false;
	}

	public boolean isNUL() {
		if ((packageBytes[0] & NUL_PDU) == NUL_PDU)
			return true;
		else
			return false;
	}

	public boolean isSEG() {
		if ((packageBytes[0] & SEG_PDU) == SEG_PDU)
			return true;
		else
			return false;
	}

	public void setCheckSum() {
		packageBytes[16] = 0;
		packageBytes[17] = 0;
		short cs = Util.calculChecksum(packageBytes, this.getHeaderLength()
				+ this.getDataLength());
		packageBytes[16] = (byte) ((cs >> 8) & 0xFF);
		packageBytes[17] = (byte) (cs & 0xFF);
	}

}
