package org.noneorone.net.socket;

import java.io.File;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class GetIpAddress {
	public static void main(String[] args) {
		if(args.length==0){
			printLocalInfo();
		}else{
			printInputInfo(args);
		}
	}
	/**
	 *获取本地IP 
	 */
	static void printLocalInfo(){
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if(interfaces == null){
				System.out.println("There isn't network interface!");
			}else{
				while(interfaces.hasMoreElements()){
					NetworkInterface interInfo = interfaces.nextElement();
					System.out.println("Interface:"+interInfo.getName()+"--");
					Enumeration<InetAddress> addressEnum = interInfo.getInetAddresses();
					if(!addressEnum.hasMoreElements()){
						System.out.println("\t(No address for this interface)");
					}
					while(addressEnum.hasMoreElements()){
						InetAddress address = addressEnum.nextElement();
						String str =null;
						if(address instanceof Inet4Address){
							str = "(IPv4)";
						}else if(address instanceof Inet6Address){
							str = "(IPv6)";
						}else{
							str = "(?)";
						}
						System.out.print("\tAddress "+str);
						System.out.println(File.separator+address.getHostAddress());
					}
				}
			}
		} catch (SocketException se) {
			System.out.println("NetWork interface error:"+se.getMessage());
		}
	}
	/**
	 *获取输入信息的IP 
	 */
	static void printInputInfo(String[] args){
		for(String host:args){
			try {
				System.out.println(host+":");
				InetAddress[] addressArray = InetAddress.getAllByName(host);
				for(InetAddress address:addressArray){
					System.out.println("\t"+address.getHostName()+File.separator
							+address.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("\tUnable to find address for "+host);
			}
		}
	}
}