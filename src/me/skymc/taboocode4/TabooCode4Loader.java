package me.skymc.taboocode4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.condition.NormalCondition;
import me.skymc.taboocode4.spells.NormalSpells;

public class TabooCode4Loader {
	
	public static void loadAttributes() {
		FileWriter fw = null;
		PrintWriter out = null;
		
		Iterator<NormalCondition> i = TabooCode4.getNormalConditions().iterator();
		while (i.hasNext()) {
			boolean enable = true;
			NormalCondition cond = i.next();
			
			File file = new File(TabooCode4.getConditionFile(), cond.getClass().getSimpleName() + ".yml");
			FileConfiguration conf = null;
			
			if (!file.exists()) {
				try {
					file.createNewFile();
					
					fw = new FileWriter(file); 
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
					out.write("# TabooCode4 Configuration Settings #\n");
					out.write("# Plugin Made by Bkm016 (坏黑) #\n");
					out.flush();
					out.close();
					fw.close();
					
					enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), true);
					conf.set("Settings.Priority", 0);
				} catch (IOException e) {
					continue;
				}
			} else {
				enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), false);
			}
			try {
				conf.save(file);
			} catch (IOException e) {
				continue;
			}
			if (enable) {
				if (TabooCode4.isDebug()) TabooCode4.send("注册条件: &f" + cond.getClass().getSimpleName());
			} else {
				if (TabooCode4.isDebug()) TabooCode4.send("注册失败: &4" + cond.getClass().getSimpleName());
				i.remove();
			}
		}
		
		Iterator<SpecialAttribute> i2 = TabooCode4.getSpecialAttributes().iterator();
		while (i2.hasNext()) {
			boolean enable = true;
			SpecialAttribute cond = i2.next();
			
			File file = new File(TabooCode4.getAttributeFile(), cond.getClass().getSimpleName() + ".yml");
			FileConfiguration conf = null;
			
			if (!file.exists()) {
				try {
					file.createNewFile();
					
					fw = new FileWriter(file); 
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
					out.write("# TabooCode4 Configuration Settings #\n");
					out.write("# Plugin Made by Bkm016 (坏黑) #\n");
					out.flush();
					out.close();
					fw.close();
					
					enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), true);
					conf.set("Settings.Priority", 0);
				} catch (IOException e) {
					continue;
				}
			} else {
				enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), false);
			}
			try {
				conf.save(file);
			} catch (IOException e) {
				continue;
			}
			if (enable) {
				if (TabooCode4.isDebug()) TabooCode4.send("注册属性: &f" + cond.getClass().getSimpleName());
			} else {
				if (TabooCode4.isDebug()) TabooCode4.send("注册失败: &4" + cond.getClass().getSimpleName());
				i2.remove();
			}
		}
		
		Iterator<NormalSpells> i3 = TabooCode4.getNormalSpells().iterator();
		while (i3.hasNext()) {
			boolean enable = true;
			NormalSpells cond = i3.next();
			
			File file = new File(TabooCode4.getSpellsFile(), cond.getClass().getSimpleName() + ".yml");
			FileConfiguration conf = null;
			
			if (!file.exists()) {
				try {
					file.createNewFile();
					
					fw = new FileWriter(file); 
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
					out.write("# TabooCode4 Configuration Settings #\n");
					out.write("# Plugin Made by Bkm016 (坏黑) #\n");
					out.flush();
					out.close();
					fw.close();
					
					enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), true);
				} catch (IOException e) {
					continue;
				}
			} else {
				enable = cond.register(conf = YamlConfiguration.loadConfiguration(file), false);
			}
			try {
				conf.save(file);
			} catch (IOException e) {
				continue;
			}
			if (enable) {
				if (TabooCode4.isDebug()) TabooCode4.send("注册魔法: &f" + cond.getClass().getSimpleName());
			} else {
				if (TabooCode4.isDebug()) TabooCode4.send("注册失败: &4" + cond.getClass().getSimpleName());
				i3.remove();
			}
		}
	}
}
