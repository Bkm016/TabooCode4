package me.skymc.taboocode4.type;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SoundPack {
	
	private Sound sound;
	private Float a;
	private Float b;
	
	public SoundPack() {
		this.sound = Sound.ENTITY_VILLAGER_NO;
		this.a = 1f;
		this.b = 1f;
	}
	
	public SoundPack(String s) {
		try {
			sound = Sound.valueOf(s.split("-")[0]);
			a = Float.valueOf(s.split("-")[1]);
			b = Float.valueOf(s.split("-")[2]);
		}
		catch (Exception e) {
			this.sound = Sound.ENTITY_VILLAGER_NO;
			this.a = 1f;
			this.b = 1f;
		}
	}
	
	public SoundPack(Sound sound, Float a, Float b) {
		this.sound = sound;
		this.a = a;
		this.b = b;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public Float getA() {
		return a;
	}
	
	public Float getB() {
		return b;
	}
	
	public void parse(String s) {
		try {
			sound = Sound.valueOf(s.split("-")[0]);
			a = Float.valueOf(s.split("-")[1]);
			b = Float.valueOf(s.split("-")[2]);
		}
		catch (Exception e) {
			this.sound = Sound.ENTITY_VILLAGER_NO;
			this.a = 1f;
			this.b = 1f;
		}
	}
	
	public void play(Player p) {
		p.playSound(p.getLocation(), sound, a, b);
	}

}