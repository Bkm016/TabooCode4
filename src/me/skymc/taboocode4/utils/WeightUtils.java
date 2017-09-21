package me.skymc.taboocode4.utils;

import java.util.List;
import java.util.Random;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.type.WeightCategory;

public class WeightUtils {  
	
    public static String getStringByWeight(List<WeightCategory> categorys) {
    	
    	int weightSum = 0;    
        
        for (WeightCategory wc : categorys) {    
            weightSum += wc.getWeight();    
        }    
          
        if (weightSum <= 0) {    
           return null;    
        }    
        
        Integer n = TabooCode4.getRandom().nextInt(weightSum);
        Integer m = 0;    
        
        for (WeightCategory wc : categorys) {    
        	if (m <= n && n < m + wc.getWeight()) {    
            	return wc.getCategory();
        	}    
            m += wc.getWeight();    
        }    
        return null;
    }
 
}