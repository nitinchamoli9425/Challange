# Details #
* Programming Language: Java
* Tools & Framework: Gradle, Junit
* Scripts: Challenge.java, ChallengeTest.java
* Inputs to be tested are given via Junit tests

# Test coverage #
* Positive tests: 2, Negative tests: 4

# Assumptions
* Case-Sensitivity is observed in the technologies list, e.g., PHP5 & Php5 are considered two different technolgoies.
* https://upb.de/site & https://upb.de/site/site will be valid links
* A link only has to start with https://, e.g., http://upb.de/site  will be invalid
* No special characters allowed in link, e.g., http://u@pb.de/site is invalid

# Problem description
* The projects takes different site names and the technollogies used in them, the output should simplify the results to report only the differences between the
technologies contained in a directory and its parent.

### Example:

#### Input

https://upb.de/site/drupal -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/a -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/a/b -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/a/c -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/a/d/e -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/f -> Drupal, Apache, PHP5, RedHat
https://upb.de/site/drupal/g -> Drupal, Apache, PHP5, RedHat, AngularJS
https://upb.de/site/jml -> Joomla, Apache, PHP4, AngularJS
https://upb.de/site/jml/pdfs -> PDF generator
https://upb.de/site/ -> Apache, PHP5

#### output

https://upb.de/site/ -> Apache, PHP5
https://upb.de/site/drupal/ -> Drupal, RedHat
https://upb.de/site/drupal/g -> AngularJS
https://upb.de/site/jml -> Joomla, PHP4, AngularJS
https://upb.de/site/jml/pdfs -> PDF generator
