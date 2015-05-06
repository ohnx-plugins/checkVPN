package tk.masonx.checkVPN;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CheckVPNPlugin extends JavaPlugin {
    private final CheckVPNConfig config = new CheckVPNConfig(this);
    private final CheckVPNCommand commandHandler = new CheckVPNCommand(this, config);
    private final JoinListener playerListener = new JoinListener(this, config);
    PluginDescriptionFile pdfFile;
    
    @Override
    public void onDisable() {
        getLogger().info("Goodbye world!");
    }

    @Override
    public void onEnable() {
    	//Load config
    	config.loadConfiguration();
    	
        //Register player join listener
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);

        //Register vpn command
        getCommand("vpn").setExecutor(commandHandler);
        getCommand("vpn").setTabCompleter(new CheckVPNTabCompleter());
        
        //Say loaded!
        pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " sucessfully loaded!");
    }
}