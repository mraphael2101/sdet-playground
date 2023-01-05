package com.company.sdet_programs.algorithms.misspelled_exercise_solved;

public class Kamal {

    public static void main(String[] args) {
        System.out.println(Misspelled("versed", "xersed"));
        System.out.println(Misspelled("versed", "applb")); // returns false
        System.out.println(Misspelled("versed", "v5rsed")); // returns true
        System.out.println(Misspelled("1versed", "versed")); // returns true
    }

    private static Boolean Misspelled(String word1, String word2)
    {
        var numberOfDifferences = 0;
        var aChar = word1.toCharArray();
        var bChar = word2.toCharArray();

        if (aChar.length - bChar.length > 1)
        {
            return false;
        }

        for (var index = 0; index < aChar.length; index++)
        {
            if (aChar[index] != bChar[index])
            {
                numberOfDifferences++;
            }
        }
        return numberOfDifferences == 1;

    }
}
