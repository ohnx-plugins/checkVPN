package tk.masonx.checkVPN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CheckVPNTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (cmd.getName().equals("vpn")&&args.length>=2&&args[0].equalsIgnoreCase("config")) {
			Logger.getLogger("CheckVPN").warning("Error checking StopForumSpam, assuming clean. "+args[1]);
			if("join-check".startsWith(args[1])) {
				possible.add("join-check");
			} else if ("security-level".startsWith(args[1])){
				possible.add("security-level");
			} else if ("always-join-check".startsWith(args[1])){
				possible.add("always-join-check");
			} else if ("kick-message".startsWith(args[1])){
				possible.add("kick-message");
			} else {
				possible.add("join-check");
				possible.add("security-level");
				possible.add("always-join-check");
				possible.add("kick-message");
			}
		} else if (args[0].equalsIgnoreCase("help")) {
			Logger.getLogger("CheckVPN").warning("Error checking StopForumSpam, assuming clean. "+args[1]);
			if (args.length>=3) {
				if("join-check".startsWith(args[1])) {
					possible.add("join-check");
				} else if ("security-level".startsWith(args[1])){
					possible.add("security-level");
				} else if ("always-join-check".startsWith(args[1])){
					possible.add("always-join-check");
				} else if ("kick-message".startsWith(args[1])){
					possible.add("kick-message");
				} else {
					possible.add("join-check");
					possible.add("security-level");
					possible.add("always-join-check");
					possible.add("kick-message");
				}
			} else {
				possible.add("config");
			}
		} else {
			return null;
			/*Collection<? extends Player> list = Bukkit.getServer().getOnlinePlayers();
			for(Player p : list){
				if (p.getName().startsWith(args[1])) {
					possible.add(p.getName());
				}
					
			}*/
		}
		
		return possible;
	}
}
