package net.winneonsword.JM;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.winneonsword.JM.utils.ConfigUtils;
import net.winneonsword.JM.utils.DataUtils;
import net.winneonsword.JM.utils.LogUtils;

public class JM extends JavaPlugin {
	
	public YamlConfiguration config;
	
	private PluginManager pm;
	
	private LogUtils logUtils;
	private ConfigUtils configUtils;
	private DataUtils dataUtils;
	
	private String[] configs;
	
	@Override
	public void onEnable(){
		
		pm = getServer().getPluginManager();
		new Utils(this);
		
		logUtils = new LogUtils(this);
		configUtils = new ConfigUtils(this);
		dataUtils = new DataUtils(this);
		
		this.configs = new String[] {
				
				"config"
				
		};
		
		for (String c : configs){
			
			getData().registerFile(c);
			
		}
		
		this.config = getData().getConfig("config");
		
		getCommand("jm").setExecutor(new CommandJm(this));
		pm.registerEvents(new JoinListener(this), this);
		
		List<String> users = config.getStringList("users");
		getUtils().setUsers(users);
		
		getLogging().info('e', "Loading " + (users.size() + Bukkit.getOnlinePlayers().length) + " users.");
		
		for (String n : getUtils().getUsers()){
			
			getUtils().setupPlayer(n);
			
		}
		
		for (Player p : Bukkit.getOnlinePlayers()){
			
			if (!(getUtils().getUsers().contains(p.getName()))){
				
				getUtils().setupPlayer(p.getName());
				
			}
			
		}
		
		getUtils().setMessageColour(config.getString("colours.message"));
		getUtils().setPlayerColour(config.getString("colours.playername"));
		
		getUtils().setJoinMessages(config.getStringList("global.join"));
		getUtils().setLeaveMessages(config.getStringList("global.leave"));
		
		getLogging().info("JoinMessages has been enabled.");
		
	}
	
	@Override
	public void onDisable(){
		
		List<String> users = getUtils().getUsers();
		
		for (String n : users){
			
			getUtils().savePlayer(n);
			
		}
		
		config.set("users", users);
		
		config.set("colours.message", getUtils().getMessageColour());
		config.set("colours.playername", getUtils().getPlayerColour());
		
		config.set("global.join", getUtils().getJoinMessages());
		config.set("global.leave", getUtils().getLeaveMessages());
		
		for (String c : configs){
			
			getData().saveConfig(c);
			getLogging().info('e', "Saved the config '" + c + "'.");
			
		}
		
		getLogging().info('c', "JoinMessages has been disabled.");
		
	}
	
	public PluginManager getPM(){
		
		return pm;
		
	}
	
	public ConfigUtils getData(){
		
		return configUtils;
		
	}
	
	public LogUtils getLogging(){
		
		return logUtils;
		
	}
	
	public DataUtils getUtils(){
		
		return dataUtils;
		
	}
	
}
