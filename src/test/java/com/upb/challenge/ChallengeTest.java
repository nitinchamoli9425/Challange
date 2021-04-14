package com.upb.challenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class ChallengeTest {
    /**
     * Positive test case, it gives unique hyperlinks which have distinct technologies names
     */
    @Test
    public void testRetrieve(){
        Map<String, String[]> mapper= new TreeMap<String, String[]>();
        mapper.put("https://upb.de/site/drupal", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a/b", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a/c", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a/d/e", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/f", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/g", new String[]{"Drupal", "Apache", "PHP5", "RedHat", "AngularJS"});
        mapper.put("https://upb.de/site/jml", new String[]{"Joomla", "Apache", "PHP4", "AngularJS"});
        mapper.put("https://upb.de/site/jml/pdfs", new String[]{"PDF generator"});
        mapper.put("https://upb.de/site/", new String[]{"Apache", "PHP5"});

        Challenge challenge= new Challenge(mapper);

        Map<String, String[]> expectedResult=new HashMap<String, String[]>();
        expectedResult.put("https://upb.de/site/",new String[]{"Apache","PHP5"});
        expectedResult.put("https://upb.de/site/drupal/",new String[]{"Drupal","RedHat"});
        expectedResult.put("https://upb.de/site/drupal/g",new String[]{"AngularJS"});
        expectedResult.put("https://upb.de/site/jml",new String[]{"AngularJS", "Joomla", "PHP4"});
        expectedResult.put("https://upb.de/site/jml/pdfs",new String[]{"PDF generator"});

        Map<String, String[]> result=challenge.checkUnique();

        for (Map.Entry<String, String[]> entry : result.entrySet()) {
            String[] result1=entry.getValue();
            Arrays.sort(result1);
            System.out.println(entry.getKey() + " -> " + Arrays.toString(result1));
        }
        Assertions.assertTrue(challenge.compareMap(expectedResult,result));
    }

    /**
     * Positive Test: accepts link in Uppercase
     * "HTTPS://UPB.DE/SITE", "HTTPS://upb.de/site", "httpS://upb.De/site"
     */
    @Test
    public void linksIgnoreCase(){
        Map<String, String[]> mapper= new TreeMap<String, String[]>(String.CASE_INSENSITIVE_ORDER);
        mapper.put("https://upb.de/site/drupal/a/b", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a/c", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a/d/e", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/f", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/g", new String[]{"Drupal", "Apache", "PHP5", "RedHat", "AngularJS"});
        mapper.put("https://upb.de/site/jml", new String[]{"Joomla", "Apache", "PHP4", "AngularJS"});
        mapper.put("https://upb.de/site/jml/pdfs", new String[]{"PDF generator"});
        mapper.put("HTTPS://upb.De/sitE", new String[]{"Apache", "PHP5"});
        mapper.put("HTTPS://UPB.DE/SITE", new String[]{"Apache", "PHP5"});
        mapper.put("HTTPS://upb.de/site", new String[]{"Apache", "PHP5"});
        mapper.put("https://upb.de/site", new String[]{"Apache", "PHP5"});
        Challenge challenge= new Challenge(mapper);

        Map<String, String[]> expectedResult=new HashMap<String, String[]>();
        expectedResult.put("https://upb.de/site/",new String[]{"Apache","PHP5"});
        expectedResult.put("https://upb.de/site/drupal/",new String[]{"Drupal","RedHat"});
        expectedResult.put("https://upb.de/site/drupal/g",new String[]{"AngularJS"});
        expectedResult.put("https://upb.de/site/jml",new String[]{"AngularJS", "Joomla", "PHP4"});
        expectedResult.put("https://upb.de/site/jml/pdfs",new String[]{"PDF generator"});

        Map<String, String[]> result=challenge.checkUnique();

        for (Map.Entry<String, String[]> entry : result.entrySet()) {
            String[] result1=entry.getValue();
            Arrays.sort(result1);
            System.out.println(entry.getKey() + " -> " + Arrays.toString(result1));
        }
        Assertions.assertTrue(challenge.compareMap(expectedResult,result));
    }

    /**
     * Negative test: A link must begin with "https://"
     */
    @Test
    public void beginsHttps(){

        Map<String, String[]> mapper= new TreeMap<String, String[]>();
        mapper.put("http://upb.de/site/drupal/a/b", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("http://upb.de/site/drupal/a/c", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal/a", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site/drupal", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site", new String[]{"Apache", "PHP5"});
        Challenge challenge= new Challenge(mapper);
        try {
            challenge.checkUnique();
        }catch(Exception e){
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    /**
     * Negative Test: a link cannot be of type https://upb.de//site
     * or https://upb.de///site
     */

    @Test
    public void improperLink(){
        Map<String, String[]> mapper= new TreeMap<String, String[]>();
        mapper.put("https://upb.de//site/drupal/a/b", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de///site/drupal/g", new String[]{"Drupal", "Apache", "PHP5", "RedHat", "AngularJS"});
        mapper.put("https://upb.de/site/jml", new String[]{"Joomla", "Apache", "PHP4", "AngularJS"});
        mapper.put("https://upb.de/site/jml/pdfs", new String[]{"PDF generator"});
        mapper.put("https://upb.de/site", new String[]{"Apache", "PHP5"});
        Challenge challenge= new Challenge(mapper);
        try {
            challenge.checkUnique();
        }catch(Exception e){
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    /**
     * Input link should not contains any special characters except, ":, / "
     * e.g.: https://@#%%upb.de/site/drupal/a/b is invalid
     * "https://@#%%upb.de/si te" invalid
     */
    @Test
    public void specialCharacters(){
        Map<String, String[]> mapper= new TreeMap<String, String[]>();
        mapper.put("https://@#%%upb.de/site/drupal/a/b", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/s ite", new String[]{"Apache", "PHP5"});
        Challenge challenge= new Challenge(mapper);
        try {
            challenge.checkUnique();
        }catch(Exception e){
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    /**
     * Negative test: checks if input passed is empty
     *  -> "", {"Drupal", "Apache", "PHP5", "RedHat"}
     *  -> "https://", {"Drupal", "Apache", "PHP5", "RedHat"}
     *  ->  "https://upb.de/site/drupal/a/d/e", {}
     */
    @Test
    public void inputEmpty(){

        Map<String, String[]> mapper= new TreeMap<String, String[]>();
        mapper.put("https://", new String[]{"Drupal", "Apache", "PHP5", "RedHat"});
        mapper.put("https://upb.de/site", new String[]{});
        Challenge challenge= new Challenge(mapper);
        try {
            challenge.checkUnique();
        }catch(Exception e){
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }
}
