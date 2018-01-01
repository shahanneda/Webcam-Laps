package com.shahan.webcamlaps;

/**
 * Created by shahan on 12/31/2017.
 */
public  class Helper {

    public static boolean isSimilar(int r1,int g1,int b1, int r2, int g2, int b2){
        int amount = 35;
        if (   r2+amount >= r1 && r2-amount <= r1
            && g2+amount >= g1 && g2-amount <= g1
            && b2+amount >= b1 && b2-amount <= b1 ){
            return true;
        }

        return  false;
    }


}
