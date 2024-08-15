package com.iw.nems_test_subscriber_top.adapter.out.placeholder;

import java.util.List;

/*
 * DISCLAIMER:
 * 
 * Do NOT use this class's features to validate actual NHI numbers. 
 * 
 * This code is used in an environment where it will deal solely with 
 * testing data in a placeholder application port
 * 
 * The format currently being used (as of 08/2024) for validation is as follows:
 * 
 * AAANNNN
 * 
 * 3 alpha, 3 numeric
 * 
 * All alpha carachters are assumed to be uppercase
 */
public class MockNHIParser {

    private static List<Character> upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
        .mapToObj(c -> (char) c)
        .toList(); 

    private static List<Character> numChars = "1234567890".chars()
        .mapToObj(c -> (char) c)
        .toList();  

    static public boolean isValid(String nhi){

        if(nhi.length() == 7){ //Verify the nhi is 7 chars long 
            for (int i = 0; i < nhi.length(); i++) {
                // verify if first 3 chars are letters
                if(i < 3){
                    if(!upperCaseChars.contains(nhi.toUpperCase().charAt(i))){
                        return false;
                    }
                } 
                else // verify last 4 chars are numbers 
                {
                    if(!numChars.contains(nhi.charAt(i))){
                        return false;
                    }
                }
            }

            return true;
        }

        return false;

    }

}
