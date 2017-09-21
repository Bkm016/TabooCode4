package me.skymc.taboocode4;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.mana.ManaManager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceholderHook
    extends EZPlaceholderHook
{
    public PlaceholderHook(TabooCode4 plugin) 
    {
    	super(plugin, "TabooCode4");
    }

    @Override
    public String onPlaceholderRequest(Player p, String s) {
        
    	if (s.toLowerCase().startsWith("primary_")) {
    		try {
	    		SpecialAttributeData data = AttributesManager.getSpecialAttributeData(p, s.split("_")[1]);
	    		return data.getPrimary()[0] + "-" + data.getPrimary()[1];
    		}
	    	catch (Exception e) {
	    		TabooCode4.send("错误的变量: &4" + s);
	    		return "0-0";
	    	}
    	}
    	if (s.toLowerCase().startsWith("regular_")) {
    		try {
	    		SpecialAttributeData data = AttributesManager.getSpecialAttributeData(p, s.split("_")[1]);
	    		return data.getRegular() + "";
    		}
	    	catch (Exception e) {
	    		TabooCode4.send("错误的变量: &4" + s);
	    		return "0";
	    	}
    	}
    	if (s.toLowerCase().startsWith("percent_")) {
    		try {
	    		SpecialAttributeData data = AttributesManager.getSpecialAttributeData(p, s.split("_")[1]);
	    		return data.getPercent() + "";
    		}
	    	catch (Exception e) {
	    		TabooCode4.send("错误的变量: &4" + s);
	    		return "0";
	    	}
    	}
    	if (s.toLowerCase().equals("manabar")) {
    		return ManaManager.getManaString(p);
    	}
    	if (s.toLowerCase().equals("mana")) {
    		return ManaManager.getPlayerMana(p) + "";
    	}
    	if (s.toLowerCase().equals("maxmana")) {
    		return ManaManager.getMaxMana(p) + "";
    	}
        return null;
    }
    
}