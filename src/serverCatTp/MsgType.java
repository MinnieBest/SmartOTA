package serverCatTp;

public interface MsgType {
	final byte SYN_PDU = (byte) 0x80;
	final byte ACK_PDU = (byte) 0x40;
	final byte EACK_PDU = (byte) 0x20;
	final byte RST_PDU = (byte) 0x10;
	final byte NUL_PDU = (byte) 0x08;
	final byte SEG_PDU = (byte) 0x04;
}
