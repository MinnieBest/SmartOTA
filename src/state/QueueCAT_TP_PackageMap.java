package state;

import java.util.HashMap;
import java.util.Map;

import serverCatTp.CAT_TP_Package;


public class QueueCAT_TP_PackageMap {
	//trace for the last 10 sent packages
	private static Map<Integer, CAT_TP_Package> QueuePackageMap = new HashMap<Integer, CAT_TP_Package>();

	public static void addPackageMap(int seq, CAT_TP_Package sentPackage) {
		/*
		if (QueuePackageMap.containsKey(seq))
			return;
		*/
		int limit = 10;
		QueuePackageMap.put(seq, sentPackage);
		if (QueuePackageMap.size() > limit) {
			QueuePackageMap.remove(seq - limit);
		}
	}

	public static CAT_TP_Package getSentPackage(int seq) {
		return QueuePackageMap.get(seq);
	}

	public static void clearQueue() {
		QueuePackageMap.clear();
	}
	

}
