package me.skymc.taboocode4.attribute;

import java.util.Random;

import org.bukkit.Bukkit;

import me.skymc.taboocode4.TabooCode4;

public class SpecialAttributeData {
	
	private Double[] Primary;
	private Double Regular;
	private Double Percent;
	
	public SpecialAttributeData(Double[] primary, Double regular, Double percent) {
		Primary = primary;
		Regular = regular;
		Percent = percent;
	}
	
	public Double[] getPrimary() {
		return Primary;
	}
	
	public Double getNeutralPrimary() {
		
		if (Primary[0] != null) {
			return TabooCode4.getFormatedDouble(Primary[1] + TabooCode4.getRandom().nextDouble() * (Primary[0] - Primary[1]));
		}
		return 0D;
	}
	
	public Double getRegular() {
		return Regular;
	}
	
	public Double getPercent() {
		return Percent;
	}
	
	public SpecialAttributeData importData(SpecialAttributeData d) {
		
		Primary[0] += d.getPrimary()[0];
		Primary[1] += d.getPrimary()[1];
		
		Regular += d.getRegular();
		Percent += d.getPercent();
		
		return this;
	}
}