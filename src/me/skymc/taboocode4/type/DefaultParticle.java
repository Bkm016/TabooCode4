package me.skymc.taboocode4.type;

import me.skymc.taboocode4.library.EffLib;

public class DefaultParticle {
	
	public EffLib effect;
	
	public float a;
	public float b;
	public float c;
	public float d;
	public int amount;
	
	public DefaultParticle(EffLib e, float a, float b, float c, float d, int amount) {
		effect = e;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.amount = amount;
	}

}