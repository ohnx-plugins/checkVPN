package tk.masonx.checkVPN;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class CheckVPN {
	//Check Shroomery
	protected static boolean checkS(String ip) {
		try {
			URL url = new URL("http://www.shroomery.org/ythan/proxycheck.php?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			in.close();
			if(line.equals("Y")){
				//VPN detected!
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking Shroomery, assuming clean.");
		}
		return false;
	}
	//Check getipaddr
	protected static boolean checkGIP(String ip) {
		try {
			URL url = new URL("http://check.getipaddr.net/check.php?ip="+ip);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = in.readLine();
			in.close();
			if(line.equals("1")){
				//VPN detected!
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Logger.getLogger("CheckVPN").warning("Error checking getipaddr, assuming clean.");
		}
		return false;
	}
	
	//Main handler
	public static boolean checkVPN(String ip, CheckVPNConfig config) {
		boolean isVPN=false;
		if(config.secLevel>=1)
			isVPN = checkS(ip)||false;//Add more later
		if(config.secLevel==2)
			isVPN = checkGIP(ip)||false;//Add more later
		//Other NYI ones
		//TODO: Implement these ones
		/*getipaddr = new URL("http://api.stopforumspam.org/api?ip="+ip);
		in = new BufferedReader(new InputStreamReader(getipaddr.openStream()));
		while ((inputLine = in.readLine()) != null)
		    System.out.println(inputLine);
		in.close();
		
		getipaddr = new URL("http://yasb.intuxication.org/api/check.xml?ip="+ip);
		in = new BufferedReader(new InputStreamReader(getipaddr.openStream()));
		while ((inputLine = in.readLine()) != null)
		    System.out.println(inputLine);
		in.close();*/
		return isVPN;
	}

}