package ad1.ss16.pa;

import java.util.*;

/**
 * Created by PhilippLassnig on 06.05.16.
 */
public class KeyCheck {
    public static void main(String[] args) {
        TreeMap<Integer,Byte> list = new TreeMap<>();
        byte b = 1;
        list.put(3,b);
        list.put(7,b);
        list.put(2,b);
        list.put(25,b);

        ArrayList<Integer> keySet = new ArrayList(list.keySet());

        for(int key : keySet){
            System.out.println(key);
        }
    }
}
