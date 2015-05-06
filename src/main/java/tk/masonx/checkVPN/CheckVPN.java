package tk.masonx.checkVPN;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class CheckVPN {
	//Check Shroomery - http://www.shroomery.org/ythan/proxyblock.php
	protected static boolean checkS(String ip) {
		try {
			URL url = new URL("http://www.shroomery.org/ythan/proxycheck.php?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			in.close();
			if(line.equals("Y"))
				//VPN detected!
				return true;
			else
				return false;
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking Shroomery, assuming clean.");
		}
		return false;
	}
	//Check getipaddr - http://check.getipaddr.net
	protected static boolean checkGIP(String ip) {
		try {
			URL url = new URL("http://check.getipaddr.net/check.php?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			in.close();
			if(line.equals("1")) //VPN detected!
				//VPN detected!
				return true;
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking getipaddr, assuming clean.");
		}
		return false;
	}
	//check Yet Another Spam Blacklist - http://yasb.intuxication.org/
	protected static boolean checkYASB(String ip) {
		try {
			URL url = new URL("http://yasb.intuxication.org/api/check.xml?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			while ((line += in.readLine()) != null)
				if(line.toLowerCase().contains("true"))
						return true;
			in.close();
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking Yet Another Spam Blacklist, assuming clean.");
		}
		return false;
	}
	//check StopForumSpam - https://stopforumspam.com/usage
	protected static boolean checkSFS(String ip) {
		try {
			URL url = new URL("http://api.stopforumspam.org/api?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			while ((line += in.readLine()) != null)
				if(line.toLowerCase().contains("yes"))
						return true;
			in.close();
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking StopForumSpam, assuming clean.");
		}
		return false;
	}
	
	//check winmxunlimited.net - http://winmxunlimited.net/apis/proxy-detection-api/
	protected static boolean checkWU(String ip) {
		try {
			URL url = new URL("http://winmxunlimited.net/api/proxydetection/v1/query/?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			if(!line.equals("0"))//Tor is tor, public VPN is public
				return true;
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking winmxunlimited.net, assuming clean.");
		}
		return false;
	}
	
	//Main handler
	public static boolean checkVPN(String ip, CheckVPNConfig config) {
		//Localhost?...
		if(ip.equals("127.0.0.1"))
			return false;
		//Slightly more efficient code here - if the required security level is set then use or (or will only run until one of them is true)
		if(config.secLevel>=1&&(checkS(ip)||checkYASB(ip)||false))
			return true;
		if(config.secLevel>=2&&(checkGIP(ip)||checkWU(ip)||false))
			return true;
		if(config.secLevel>=3&&(checkSFS(ip)||false))
			return true;
		//Nothing has reported it yet, so return false
		return false;
	}

}