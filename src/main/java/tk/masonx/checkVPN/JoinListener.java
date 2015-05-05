package tk.masonx.checkVPN;

import java.io.File;
import tk.masonx.checkVPN.CheckVPN;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {
    private final CheckVPNPlugin plugin;
	private final CheckVPNConfig config;
	
    public JoinListener(CheckVPNPlugin plugin, CheckVPNConfig config) {
        this.plugin = plugin;
        this.config = config;
    }
    
    private boolean checkVPN(Player player) {
    	String ip = player.getAddress().getAddress().getHostAddress();
		if(CheckVPN.checkVPN(ip, config)){
			if(player.hasPermission("checkVPN.bypassVPNCheck")) {
				plugin.getLogger().info(player.getName() + " was detected as using a VPN, but they bypassed it.");
			} else {
				plugin.getLogger().info(player.getName() + " is using a VPN!");
				player.kickPlayer(config.getCString("kick-message"));
				return true;
			}
		}
		return false;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	//Check if to check joins
    	if(config.joinCheck) {
    		//Check if always check joins
    		if(config.alwaysJoinCheck) {
    			checkVPN(event.getPlayer());
    		} else {
    			//check if player first time joining
    			if(!event.getPlayer().hasPlayedBefore()){
    				String UUID = event.getPlayer().getUniqueId().toString();
    				if(checkVPN(event.getPlayer())) {
    					//Delete both the data file and the json file
    					String name = plugin.getServer().getWorlds().get(0).getName();
    					final File datFile = new File(name+"\\playerdata\\"+UUID+".dat");
    					final File jsonFile = new File(name+"\\stats\\"+UUID+".json");
    					new BukkitRunnable() {
    						public void run() {
    							if(!datFile.delete()){
    								plugin.getLogger().warning("Deleting the data file for the user did not succeed!");
    							}
    							if(!jsonFile.delete()){
    								plugin.getLogger().warning("Deleting the stats file for the user did not succeed!");
    							}
    						}
    						}.runTaskLater(plugin, 10); // In ticks, 20 ticks = 1 second = 1000 ms
    		    		
    				}
    			}
    		}
    		
    	}
    }
}
