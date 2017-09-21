package me.skymc.taboocode4.identifie;

import java.util.ArrayList;
import java.util.List;

import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.type.WeightCategory;
import me.skymc.taboocode4.utils.WeightUtils;

public class IdentifiePack {
	
	private String id;
	private String name;
	private String display;
	private SoundPack sound;
	private List<WeightCategory> item = new ArrayList<>();
	
	public IdentifiePack() {
		
	}
	
	public IdentifiePack(String id, String name, String display, SoundPack sound, List<WeightCategory> list) {
		this.id = id;
		this.name = name;
		this.item = list;
		this.display = display;
		this.sound = sound;
	}
	
	public SoundPack getSound() {
		return sound;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public List<WeightCategory> getItems() {
		return item;
	}
	
	public String getItem() {
		return WeightUtils.getStringByWeight(item);
	}

}