package state;

import java.nio.ByteBuffer;
import serverCatTp.Package0348;
import util.Util;

public class State {
	//Max data length in each PDU
	//static public int maxPDUDataLen = 256; 
	static public int maxPDUDataLen = 96;
	//static public int maxPDUDataLen = 64; 
	//Max length of each SDU
	static public int maxSDULen = 1000;
	
	// server's sequence
	static public int sequenceNb = 0;
	static public int ackNb = 0;

	// state for the commands
	static public int idxCommandList = 0;
	static public boolean hasCommand = true;

	// state for the segment package
	static public int segPartsNeeded = 0;
	static public int segNb = 0;
	static public int lastPartLength = 0;
	static public boolean isSendingSeq = false;
	static public Package0348 wholePackage0348 = null;
	static public String otaCounter = "0000000000";

	static public void increaseCounter() {
		for (int i = otaCounter.length(); i < 16; i++)
			otaCounter = '0' + otaCounter;

		byte[] counter = Util.hexStringToByteArray(otaCounter);
		ByteBuffer sbuf = ByteBuffer.wrap(counter);
		ByteBuffer dbuf = ByteBuffer.allocate(8);
		long counterValue = sbuf.getLong();
		counterValue++;
		dbuf.putLong(counterValue);
		counter = dbuf.array();
		otaCounter = Util.ByteArrayToHexString(counter);
		otaCounter = otaCounter.substring(6, otaCounter.length());
		counter = null;
		dbuf = null;
		sbuf = null;
	}

	static public void clearState() {
		maxPDUDataLen = 96;
		maxSDULen = 1000;
		sequenceNb = 0;
		ackNb = 0;
		idxCommandList = 0;
		hasCommand = true;
		// rstSeq = 0;
		segPartsNeeded = 0;
		segNb = 0;
		lastPartLength = 0;
		isSendingSeq = false;
		wholePackage0348 = null;
	}
}
