package tk.masonx.checkVPN;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckVPNCommand implements CommandExecutor {
	private final CheckVPNPlugin plugin;
	private final CheckVPNConfig config;
    public CheckVPNCommand(CheckVPNPlugin plugin, CheckVPNConfig config) {
        this.plugin = plugin;
        this.config = config;
    }
    
    private void getHelp(String name, CommandSender sender){
    	switch(name) {
    	case "join-check":
    		sender.sendMessage(ChatColor.GOLD+"join-check\n"+ChatColor.WHITE+"    Check if a user is using a VPN on join. If set to false, always-join-check will be ignored. \n    Default: "+ChatColor.DARK_RED+"true\n"+ChatColor.WHITE+"    Can be set to true or false."+"\n    Current value: "+ChatColor.DARK_RED+config.joinCheck);
    		break;
    	case "security-level":
    		sender.sendMessage(ChatColor.GOLD+"security-level\n"+ChatColor.WHITE+"    Change the security level. If you have issues with false postives, try turning down the level.\n    Default: "+ChatColor.DARK_RED+"2\n"+ChatColor.WHITE+"\n    Can be set to 1, 2 or 3."+"\n    Current value: "+ChatColor.DARK_RED+config.secLevel);
    		break;
    	case "always-join-check":
    		sender.sendMessage(ChatColor.GOLD+"always-join-check\n"+ChatColor.WHITE+"    Check on every join or just for new players. true will check for every player and false will check for just new players. If join-check is set to false, this value is ignored.\n    Default: "+ChatColor.DARK_RED+"false\n"+ChatColor.WHITE+"    Can be set to true or false."+"\n    Current value: "+ChatColor.DARK_RED+config.alwaysJoinCheck);
    		break;
    	case "kick-message":
    		sender.sendMessage(ChatColor.GOLD+"kick-message\n"+ChatColor.WHITE+"    The kick message for players that have been detected as using a VPN.\n    Default: "+ChatColor.DARK_RED+"You have been detected as using a VPN! Please get off it."+ChatColor.WHITE+"\n    Current value: "+ChatColor.DARK_RED+config.kickMessage);
    		break;
    	default:
    		sender.sendMessage(ChatColor.DARK_RED+"Invalid config key value!");
    	}
    }
    
	@SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if(!sender.hasPermission("checkVPN.runCommand")) {
			sender.sendMessage(ChatColor.RED+"Oops! You don't have the required permissions to access this command.");
			return true;
		}
		if(split.length == 0)
			return false;
        if(split[0].equals("help")) {
        	sender.sendMessage(ChatColor.AQUA+plugin.pdfFile.getName()+" version "+plugin.pdfFile.getVersion()+" help");
        	sender.sendMessage(ChatColor.DARK_AQUA+"A plugin to check if a player is using a VPN. Made by ohnx");
        	try {
        		if(split[1].equals("config")) {
                	if(split.length==3) {	
                		getHelp(split[2], sender);
                	} else {
                		sender.sendMessage("These are the possible config key values.\nUse \"/vpn help config <key>\" for more help:");
                		sender.sendMessage(ChatColor.GOLD+"join-check\n"+ChatColor.GOLD+"always-join-check\n"+ChatColor.GOLD+"security-level\n"+ChatColor.GOLD+"kick-message");
                		
                	}
        			return true;
        		}
        	} catch (Exception e) {}
        	sender.sendMessage(ChatColor.GOLD+"/vpn help\n"+ChatColor.WHITE+"    Gets help");
        	sender.sendMessage(ChatColor.GOLD+"/vpn config\n"+ChatColor.WHITE+"    Edit the config file ingame");
       		sender.sendMessage(ChatColor.GOLD+"/vpn <player name>\n"+ChatColor.WHITE+"    Check if a player is using a VPN");
        	sender.sendMessage(ChatColor.WHITE+"Use "+ChatColor.GOLD+"/vpn help config"+ChatColor.WHITE+" to get more help on configuring checkVPN.");
        	return true;
       	} else if(split[0].equals("config")) {
       		if(!sender.hasPermission("checkVPN.editConfig")) {
    			sender.sendMessage(ChatColor.RED+"Oops! You don't have the required permissions to access this command.");
    			return true;
       		}
       		try {
       			switch (split[1]) {
       	    	case "join-check":
       	    		config.setCBool("join-check", split[2].equals("true"));
       	    		config.saveConfig();
       	    		sender.sendMessage("Set key "+ChatColor.GOLD+"join-check"+ChatColor.WHITE+" to "+ChatColor.DARK_RED+split[2]+ChatColor.WHITE+"!");
       	    		break;
       	    	case "security-level":
       	    		if(split[2].equals("1")||split[2].equals("2")) {
       	    			config.setCInt("security-level", Integer.parseInt(split[2]));
       	    			sender.sendMessage("Set key "+ChatColor.GOLD+"security-level"+ChatColor.WHITE+" to "+ChatColor.DARK_RED+split[2]+ChatColor.WHITE+"!");
           	    		config.saveConfig();
       	    		} else {
       	    			sender.sendMessage("Invalid value! Use /vpn help config security-level more more information.");
       	    		}
       	    		break;
       	    	case "always-join-check":
       	    		config.setCBool("always-join-check", split[2].equals("true"));
       	    		config.saveConfig();
       	    		sender.sendMessage("Set key "+ChatColor.GOLD+"always-join-check"+ChatColor.WHITE+" to "+ChatColor.DARK_RED+split[2]+ChatColor.WHITE+"!");
       	    		break;
       	    	case "kick-message":
       	    		//convert array of strings to string with spaces
       	    		String tempstr="";
       	    		for(int i=2;i<split.length;i++) tempstr+=" "+split[i];
       	    		//get rid of leading space
       	    		tempstr=tempstr.substring(1,tempstr.length());
       	    		config.setCString("kick-message", tempstr);
       	    		config.saveConfig();
       	    		sender.sendMessage("Set key "+ChatColor.GOLD+"kick-message"+ChatColor.WHITE+" to \""+ChatColor.DARK_RED+tempstr+ChatColor.WHITE+"\"!");
       	    		break;
       	    	default:
       				sender.sendMessage("Invalid config option! Need help? Use /vpn help config.");
       				break;
       			}
       		} catch (Exception e) {
       			sender.sendMessage("Please specify a key AND a value! Need help? Use /vpn help config");
       		}
       		return true;
        } else {
       	    try {
            	if(CheckVPN.checkVPN(Bukkit.getPlayer(split[0]).getAddress().getHostString(), config)){
            		sender.sendMessage(ChatColor.RED+split[0]+" appears to be using a VPN!");
            	} else {
            		sender.sendMessage(ChatColor.GREEN+split[0]+" is good.");
            	}
       	    } catch (NullPointerException e) {
       	    	sender.sendMessage(ChatColor.DARK_RED+"That player does not appear to be online right now.");
       	    }
            return true;
        }
    }

}
