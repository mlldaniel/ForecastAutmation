/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Daniel
 */
public class LongestCommonSubsequence {
    public static int lcs(String a, String b) {
        int[][] lengths = new int[a.length()+1][b.length()+1];

        // row 0 and column 0 are initialized to 0 already

        for (int i = 0; i < a.length(); i++)
            for (int j = 0; j < b.length(); j++)
                if (a.charAt(i) == b.charAt(j))
                    lengths[i+1][j+1] = lengths[i][j] + 1;
                else
                    lengths[i+1][j+1] =
                        Math.max(lengths[i+1][j], lengths[i][j+1]);

        // read the substring out from the matrix
        StringBuffer sb = new StringBuffer();
        for (int x = a.length(), y = b.length();
             x != 0 && y != 0; ) {
            if (lengths[x][y] == lengths[x-1][y])
                x--;
            else if (lengths[x][y] == lengths[x][y-1])
                y--;
            else {
                assert a.charAt(x-1) == b.charAt(y-1);
                sb.append(a.charAt(x-1));
                x--;
                y--;
            }
        }

        return sb.reverse().toString().length();
    }
}

        /*
        int max = 0;
        int maxIndex = 0;
        int []dis = new int [packageList.size()];
        
        for(int i = 1 ; i < packageList.size() ; i++ ){
            dis[i] = LongestCommonSubsequence.lcs(modified, packageList.get(i).toLowerCase());
            if(dis[i] > max){
                max = dis[i];
                maxIndex = i;
            }
        }
        return packageList.get(maxIndex);
        */