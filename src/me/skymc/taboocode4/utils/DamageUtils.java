package me.skymc.taboocode4.utils;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class DamageUtils {
	
	public static void damage(Player damager, LivingEntity victim, double damage)
	{
		dmg(damager, victim, damage);
	}
	
	public static void damage(Player damager, Entity victim, double damage)
	{
		if (victim instanceof LivingEntity) {
			dmg(damager, (LivingEntity) victim, damage);
		}
	}
  
	public static void dmg(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2, double paramDouble)
	{
		if ((paramLivingEntity2.hasMetadata("NPC")) || (paramLivingEntity1.hasMetadata("NPC"))) {
			return;
		}
		
		Object localObject1 = null;
		try
		{
			localObject1 = paramLivingEntity1.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(paramLivingEntity1, new Object[0]);
		}
		catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException localIllegalAccessException1)
		{
			return;
		}
		
	    Object localObject2 = null;
	    try
	    {
	    	localObject2 = paramLivingEntity2.getClass().getDeclaredMethod("getHandle", new Class[0]).invoke(paramLivingEntity2, new Object[0]);
	    }
	    catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException localIllegalAccessException2)
	    {
	    	return;
	    }
	    
	    try
	    {
	    	Class<?> DamageSource = nmsClass("DamageSource");
	    	Object localObject3 = DamageSource.getDeclaredMethod("playerAttack", new Class[] { nmsClass("EntityHuman") }).invoke(DamageSource, new Object[] { localObject1 });
      
	    	localObject2.getClass().getDeclaredMethod("damageEntity", new Class[] { DamageSource, Float.TYPE }).invoke(localObject2, new Object[] { localObject3, Float.valueOf((float) paramDouble) });
	    }
	    catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException localIllegalAccessException3)
	    {
	    	return;
	    }
	}
  
	private static Class<?> nmsClass(String paramString)
	{
		String str = "net.minecraft.server." + ReflectionUtils.getVersion() + "." + paramString;
		try {
			return Class.forName(str);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}