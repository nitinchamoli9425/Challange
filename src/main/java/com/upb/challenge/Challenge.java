package com.upb.challenge;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Challenge {
    private Map<String, String[]> mapper;
    private Map<String, String[]> output = new TreeMap<String, String[]>(String.CASE_INSENSITIVE_ORDER);

    //challenge class constructor
    Challenge(Map<String, String[]> mapper) {
        this.mapper = mapper;
    }

    /**
     * function to assign "output" map with distinct links and technologies unique to the child-link
     * @return output: the value which is printed as the result required
     */

    public Map<String, String[]> checkUnique() {
        exceptionHandler();
        for (Map.Entry<String, String[]> entry : mapper.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            for (Map.Entry<String, String[]> entry1 : mapper.entrySet()) {
                String key1 = (entry1.getKey());
                String[] value1 = entry1.getValue();

                //call directoryChecker to find if key1 link is a child of the key link
                if (directoryChecker(Paths.get(key1.toLowerCase(Locale.ROOT).replace("https:/", "")).toAbsolutePath(), Paths.get(key.toLowerCase(Locale.ROOT).replace("https:/", "")).toAbsolutePath())) {
                    if (!output.containsKey(key1.toLowerCase(Locale.ROOT))) {
                        if (key.equalsIgnoreCase(key1)) {
                            output.put(key1, value1);
                        } else {
                            //activeUIDs contain the difference in the technologies of the child links
                            Set<String> activeUIDs = Sets.difference(new HashSet<>(Arrays.asList(value1)), new HashSet<>(Arrays.asList(value)));
                            output.put(key1, activeUIDs.toArray(new String[0]));
                        }

                    } else {
                        if (!key.equalsIgnoreCase(key1)) {
                            Set<String> activeUIDs = Sets.difference(new HashSet<>(Arrays.asList(value1)), new HashSet<>(Arrays.asList(value)));
                            output.put(key1, activeUIDs.toArray(new String[0]));
                        }
                    }
                }
            }
        }
        output.entrySet().removeIf(entry -> !ArrayUtils.isNotEmpty(entry.getValue()));
        return output;
    }


    /**
     * function to compare each link with all other links in the map, if they are child link.
     *
     * @param child      : is the key1 passed without "https://"
     * @param parentPath : is the key passed without "https://"
     * @return value = true, if value child begins with parentpath, else false.
     */
    public boolean directoryChecker(Path child, Path parentPath) {
        boolean value = false;
        if (child.startsWith(parentPath))
            value = true;
        return value;
    }

    /**
     * Compares the expected output with the results obtaineds from checkUnique function.
     * @param expected : expected output
     * @param result   : the obtained results from the function checkUnique.
     */
    public boolean compareMap(Map<String, String[]> expected, Map<String, String[]> result) {
        boolean flag = true;
        for (Map.Entry<String, String[]> entry : expected.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            for (Map.Entry<String, String[]> entry1 : result.entrySet()) {
                String key1 = entry1.getKey();
                String[] value1 = entry1.getValue();
                if (key == key1) {
                    if (!Arrays.equals(value, value1)) {
                        System.out.println(Arrays.toString(value));
                        System.out.println(Arrays.toString(value1));
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * Performs conditions on the input mapper and throws appropriate exception messages.
     */
    public void exceptionHandler() {

        //check if no input is given
        if(mapper.isEmpty()){
            throw new NullPointerException("No input entries");
        }

        for (Map.Entry<String, String[]> entry : mapper.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();

            //throws error if the link does not starts with "https://"
            if (!key.toLowerCase(Locale.ROOT).startsWith("https://")) {
                throw new IllegalArgumentException("Site name " + key + " must start with \'https://\' ");
            }

            //throws error for hyperlink type "https://upb.de//site"
            String improperLink = key.toLowerCase(Locale.ROOT).replace("https://", "");
            if (improperLink.contains("//")) {
                throw new IllegalArgumentException("Site name " + improperLink + " is illegal, for e.g. site name should be of type \' https://upb.de//site \'");
            }

            //throws error if link contains special characters
            Pattern my_pattern = Pattern.compile("[^a-z0-9:/.]", Pattern.CASE_INSENSITIVE);
            Matcher my_match = my_pattern.matcher(key);
            boolean check = my_match.find();
            if (check) {
                throw new IllegalArgumentException("Site name " + key + " should not contain special characters");
            }

            /* throws error if input passed is empty
            e.g. "", {"Drupal", "Apache", "PHP5", "RedHat"}
            e.g. "https://upb.de/site/drupal/a/d/e", {}
            */
            if(improperLink.isEmpty() || improperLink == null){
                throw  new IllegalArgumentException("One of the input link is empty!");
            }
            if(value == null || value.length == 0){
                throw new IllegalArgumentException("One of the input technologies is empty");
            }
            for(String s: value){
                if(s == null || s.isEmpty()){
                    throw new IllegalArgumentException("One of the input technologies contains empty string or null elements");
                }
            }
        }
    }
}