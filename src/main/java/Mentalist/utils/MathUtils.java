package Mentalist.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MathUtils {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return ((Comparable)o2.getValue()).compareTo(o1.getValue());
            }
        });
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    public static int[] getRandomPermutation(int n) {
        List<Integer> ints = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            ints.add(Integer.valueOf(i));
        Collections.shuffle(ints);
        int[] realInts = new int[n];
        for (int j = 0; j < n; j++)
            realInts[j] = ((Integer)ints.get(j)).intValue();
        return realInts;
    }

    public static int[][] getPermutations(int n) {
        return getPermutations(n, 0);
    }

    public static int[][] getPermutations(int n, int offset) {
        int[][] results = new int[(int)factorial(n)][];
        int[] work = new int[n];
        int[] dir = new int[n];
        for (int i = 0; i < n; i++) {
            work[i] = i + offset;
            dir[i] = 0;
        }
        int step = 1;
        results[0] = copy(work);
        while (hasMobile(work, dir)) {
            int curMobile = findLargestMobile(work, dir);
            int movePos = curMobile + ((dir[curMobile] == 0) ? -1 : 1);
            work[movePos] = (work[curMobile] += work[movePos] -= work[curMobile]) - work[movePos];
            dir[movePos] = (dir[curMobile] += dir[movePos] -= dir[curMobile]) - dir[movePos];

            for (int j = 0; j < n; j++) {
                if (work[j] > work[movePos])
                    dir[j] = (dir[j] == 0) ? 1 : 0;
            }
            results[step] = copy(work);
            step++;
        }
        return results;
    }

    private static boolean hasMobile(int[] work, int[] dir) {
        for (int i = 0, n = work.length; i < n; i++) {
            if (isMobile(work, dir, i))
                return true;
        }
        return false;
    }

    private static boolean isMobile(int[] work, int[] dir, int i) {
        if ((i == 0 && dir[i] == 0) || (i == work.length - 1 && dir[i] == 1))
            return false;
        if (i > 0 && dir[i] == 0 && work[i] > work[i - 1])
            return true;
        if (i < work.length - 1 && dir[i] == 1 && work[i] > work[i + 1])
            return true;
        if (i > 0 && i < work.length && ((
                dir[i] == 0 && work[i] > work[i - 1]) || (dir[i] == 1 && work[i] > work[i + 1])))
            return true;
        return false;
    }

    private static int findLargestMobile(int[] work, int[] dir) {
        int largest = -1;
        int pos = -1;
        for (int i = 0, n = work.length; i < n; i++) {
            if (isMobile(work, dir, i) && largest < work[i]) {
                largest = work[i];
                pos = i;
            }
        }
        return pos;
    }

    private static long factorial(int n) {
        if (n <= 1)
            return 1L;
        return n * factorial(n - 1);
    }

    private static int[] copy(int[] src) {
        int[] dest = new int[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    private static void print(int[] work, int[] dir) {
        for (int i = 0, n = work.length; i < n; i++)
            System.out.print(((dir[i] == 0) ? " <" : "  ") + ((dir[i] == 0) ? " <" : "  ") + work[i]);
        System.out.println("");
    }
}