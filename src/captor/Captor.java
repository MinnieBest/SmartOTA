package captor;

import java.io.IOException;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

public class Captor {
	
	public void getNetworkInterfaces() {
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		for (int i = 0; i < devices.length; i++) {
			System.out.println(i + ":" + devices[i].name + "("
					+ devices[i].description + ")");

			System.out.println("datalink" + devices[i].datalink_name + "("
					+ devices[i].datalink_description + ")");

			System.out.print("MAC address:");
			for (byte b : devices[i].mac_address) {
				System.out.print(Integer.toHexString(b & 0xFF) + ":");
			}
			System.out.println();

			for (NetworkInterfaceAddress a : devices[i].addresses)
				System.out.println("address:" + a.address + " " + a.subnet
						+ a.broadcast);
		}
	}
	
	public void openDevice(int idx,NetworkInterface device){
		try {
			JpcapCaptor jcCaptor=JpcapCaptor.openDevice(device, 65535, false, 20);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
