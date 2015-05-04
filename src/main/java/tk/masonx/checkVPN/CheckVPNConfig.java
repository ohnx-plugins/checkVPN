package tk.masonx.checkVPN;

public class CheckVPNConfig {
	private final CheckVPNPlugin plugin;
	boolean joinCheck;
	boolean alwaysJoinCheck;
	int secLevel;
	String kickMessage;
	
    public CheckVPNConfig(CheckVPNPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfiguration(){
    	plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    public void saveConfig(){
    	plugin.saveConfig();
    	reloadConfig();
    }
    
    public void reloadConfig(){
    	plugin.reloadConfig();
        joinCheck = getCBool("join-check");
        alwaysJoinCheck = getCBool("always-join-check");
        secLevel = getCInt("security-level");
        kickMessage = getCString("kick-message");
    }
    
    public void setCString(String path, String value){
    	plugin.getConfig().set(path, value);
    }
    
    public void setCInt(String path, int value){
    	plugin.getConfig().set(path, value);
    }
    
    public void setCBool(String path, boolean value){
    	plugin.getConfig().set(path, value);
    }
    
    public boolean getCBool(String path){
    	return plugin.getConfig().getBoolean(path);
    }
    
    public String getCString(String path){
    	return plugin.getConfig().getString(path);
    }
    
    public int getCInt(String path) {
    	return plugin.getConfig().getInt(path);
    }
}
