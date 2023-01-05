package com.company.sdet_programs.twodarrays;

import java.util.Arrays;

public class _01_Convert1DArrayTo2DArray {

    public static void main(String [] args) {
        int row = 2;
        int col = 5;
        String [] inputFileAsList = { "r1a", "r1b", "r1c", "r1d", "r1e", "r2a", "r2b", "r2c", "r2d", "r2e" };
        String [][] inputFileAsTwoDimArr = new String[row][col];

        int k = 0;

        for(int r = 0; r<row; r++) {
            if(r % 2 == 0){
                for(int c = 0;  c < col; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList[k];
                    k++;
                }
            }
            else {
                for (int c = 0;  c < col; c++){
                    inputFileAsTwoDimArr[r][c]= inputFileAsList[k];
                    k++;
                }
            }
        }
        System.out.println(Arrays.deepToString(inputFileAsTwoDimArr));
    }
}
