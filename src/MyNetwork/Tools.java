package MyNetwork;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.SocketException;
import java.util.Enumeration;

public class Tools {
	public static InetAddress getBroadcastAddress() {
		String ip;
		try {
			Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				java.net.NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				for(InterfaceAddress ifaceAddr : iface.getInterfaceAddresses()) {
					InetAddress bcast = ifaceAddr.getBroadcast();
					if(bcast == null)
						continue;
					return bcast;
				}

			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		return null;
	}
	
	
}
