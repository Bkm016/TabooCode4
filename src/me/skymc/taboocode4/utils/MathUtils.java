package me.skymc.taboocode4.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtils {
	
    public static Vector rotAxisX(Vector vector, double n) {
        return vector.setY(vector.getY() * Math.cos(n) - vector.getZ() * Math.sin(n)).setZ(vector.getY() * Math.sin(n) + vector.getZ() * Math.cos(n));
    }
    
    public static Vector rotAxisY(Vector vector, double n) {
        return vector.setX(vector.getX() * Math.cos(n) + vector.getZ() * Math.sin(n)).setZ(vector.getX() * -Math.sin(n) + vector.getZ() * Math.cos(n));
    }
    
    public static Vector rotAxisZ(Vector vector, double n) {
        return vector.setX(vector.getX() * Math.cos(n) - vector.getY() * Math.sin(n)).setY(vector.getX() * Math.sin(n) + vector.getY() * Math.cos(n));
    }
	
    public static Vector rotateFunc(Vector vector, Location location) {
        double n = location.getYaw() / 180.0f * 3.14;
        vector = rotAxisX(vector, location.getPitch() / 180.0f * 3.14);
        vector = rotAxisY(vector, -n);
        return vector;
    }

}