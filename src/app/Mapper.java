package app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Mapper {

    private Map<Integer, Integer> mappingMap;

    public Mapper(int n) {
        mappingMap = new ConcurrentHashMap<>();
        mapCreator(n);
    }

    public Map<Integer, Integer> getMappingMap() {
        return mappingMap;
    }

    public void mapCreator ( int n ) {
        int maxServents = 100;
        int powerCounter = 0;
        int[] a = new int[100];

        for(int i = 0; i < 100; ) {
            for (int j = 0; j < (int)Math.pow(n, powerCounter); j++) {
                if ( i == 100 ){
                    break;
                }
                a[i] = j;
                i++;
            }
            powerCounter++;
        }

        int k = 0;
        for( int i = 0; i < maxServents; i++ ) {
            int key = (1 + (n-1)*i);
            if( key % (n-1) != 0 ) {
                mappingMap.put(key, a[k]);
                k++;
            }
        }
    }

}
