package com.company;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class ClueFinder {


    // These two arrays include solution of the clues in the "word" format.
    private String[] acrossWords;
    private String[] downWords;

    // These two arrays hold new generated clues.
    private String[] newAcrossClues;
    private String[] newDownClues;
    int[] acrossClueNumbers;
    int[] downClueNumbers;
    int[][] littleNumbers;
    String[][] solution;
    String[] acrossClues;
    String[] downClues;


    public ClueFinder(String[] acrossClues, String[] downClues, int[] acrossClueNumbers, int[] downClueNumbers, int[][] littleNumbers, String[][] solution) throws InterruptedException, JWNLException {

        this.acrossClues = acrossClues;
        this.downClues = downClues;

        acrossWords = new String[acrossClues.length];
        downWords = new String[downClues.length];

        this.acrossClueNumbers = acrossClueNumbers;
        this.downClueNumbers = downClueNumbers;
        this.littleNumbers = littleNumbers;
        this.solution = solution;

        takeWords();

        newAcrossClues = findClues( acrossWords, true);
        newDownClues = findClues( downWords, false);

    }

    /*
    This method finds solution of every clue (as a word) by using letters in the original solution.
     */
    public void takeWords() {

        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++){

                // For across words
                for( int k = 0; k < acrossClueNumbers.length; k++){
                    if( littleNumbers[i][j] == acrossClueNumbers[k]){
                        String str = "";
                        for( int m = j; m < solution[i].length && solution[i][m] != null; m++){
                            str += solution[i][m];
                        }
                        acrossWords[k] = str;
                    }
                }

                // For down words
                for( int k = 0; k < downClueNumbers.length; k++){
                    if( littleNumbers[i][j] == downClueNumbers[k]){
                        String str = "";
                        for( int m = i; m < solution.length && solution[m][j] != null; m++){
                            str += solution[m][j];
                        }
                        downWords[k] = str;
                    }
                }

            }
        }
    }

    public String[] findClues(String[] answers, boolean isAcross) throws JWNLException {
        String[] newClues = new String[ answers.length];

        Dictionary dictionary = null;
        try {
            dictionary = Dictionary.getDefaultResourceInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        for( int i = 0; i < answers.length; i++) {
            Scanner scan = new Scanner( System.in);
//            if(!isAcross && i == 1)
//                answers[i] = "XMEN";
            System.out.println( "Answer is : " + answers[i]);
            answers[i] = answers[i].toLowerCase(Locale.ENGLISH);
            System.out.println( "Press a key to continue");
            scan.next();
            boolean isDescription = false;
            Analyzer analyzer = new Analyzer(dictionary, answers[i].toUpperCase());
            if (analyzer.isValid()) {
                System.out.println( "Searching the answer in WordNet");
                Set<POS> available = analyzer.getPOSSet();
                WordNet wordNet = new WordNet(available, answers[i].toUpperCase(), analyzer);
                HashMap<String, ArrayList<String>> entry = wordNet.getGeneratedSolutions();


                if (entry.get("participle").size() != 0) {
                    System.out.println( "Participle is found");
                    newClues[i] = "" + entry.get("participle").get(0) + " participle";
                } else if (entry.get("cause").size() != 0) {
                    System.out.println( "Cause is found");
                    String clue = "Caused by " + entry.get( "cause").get(0);
                    newClues[i] = clue;
                } else if (entry.get("entailments").size() != 0) {
                    System.out.println( "Entailment is found");
                    String clue = "It implies ";
                    newClues[i] = clue + entry.get("entailments").get(0);
                        } else if (entry.get("substance_holonyms").size() != 0) {
                            System.out.println( "Substance holonym is found");
                            String clue = "A substance of ";
                            newClues[i] = clue + entry.get("substance_holonyms").get(0);

                        } else if (entry.get("substance_meronyms").size() != 0) {
                            System.out.println( "Substance meronym is found");
                            String clue = "It consists of ";
                            for ( int j = 0; j < entry.get("substance_meronyms").size(); j++) {
                                if (j == entry.get("substance_meronyms").size() - 1) {
                                    clue += entry.get("substance_meronyms").get(j) + ".";
                                } else {
                            clue += entry.get("substance_meronyms").get(j) + " and ";
                        }
                    }
                    newClues[i] = clue;
                } else if (entry.get("part_holonyms").size() != 0) {
                    System.out.println( "Part holonym is found");
                    String clue = "A part of ";
                    newClues[i] = clue + entry.get("part_holonyms").get(0);

                } else if (entry.get("part_meronyms").size() != 0) {
                    System.out.println( "Part Meronym is found");
                    String clue = "It consists of ";
                    int count = 3;
                    for ( int j = 0; j < entry.get("part_meronyms").size() && j < count; j++) {
                        if (!(entry.get("part_meronyms").get(j).toLowerCase().contains( answers[i].toLowerCase())) ) {
                            if (j == entry.get("part_meronyms").size() - 1 || j == count -1) {
                                clue += entry.get("part_meronyms").get(j) + ".";
                            } else {
                                if (!(entry.get("part_meronyms").get(j).toLowerCase().contains(answers[i].toLowerCase())))
                                    clue += entry.get("part_meronyms").get(j) + " and ";
                            }
                        }
                        else
                            count++;

                    }
                    newClues[i] = clue;

                }
                else if (entry.get("attributes").size() != 0) {
                    System.out.println( "Attribute is found");
                    String clue = "It can be ";
                    for ( int j = 0; j < entry.get("attributes").size(); j++) {
                        if ( j == entry.get("attributes").size() - 1) {
                            clue += entry.get("attributes").get(j) + ".";
                        }
                        else {
                            clue += entry.get("attributes").get(j) + " or ";
                        }
                    }
                    newClues[i] = clue;

                } else if (entry.get("sees").size() != 0) {
                    System.out.println( "Also see is found ");
                    newClues[i] =  entry.get("sees").get(entry.get("sees").size()-1);

           /*     } else if (entry.get("coordinateTerm").size() != 0) {
                    newClues[i] = "" + entry.get("coordinateTerm");
                    System.out.println(answers[i] + " (coordinateTerm) :" + entry.get("coordinateTerm"));
*/
                } else if (entry.get("antonyms").size() != 0) {
                    System.out.println( "Antonym is found");
                    String clue = "Antonym of " + entry.get( "antonyms").get(0);
                    newClues[i] = clue;

                } else if (entry.get("synonyms").size() != 0) {
                    System.out.println( "Synonym is found");
                    String clue = "Synonym of " + entry.get("synonyms").get(0);
                    newClues[i] = clue;

         /*       } else if (entry.get("parents").size() != 0) {
                    newClues[i] = "" + entry.get("parents");
                    System.out.println(answers[i] + " (parents) :" + entry.get("parents"));
*/
                } else if (entry.get("children").size() != 0) {
                    System.out.println( "A child is found");
                    newClues[i] = "" + entry.get("children").get(0)+ " is a kind of it";

                } else if (entry.get("senses").size() != 0) {
                    System.out.println( "Senses are being searched");
                    boolean found = false;
                    for (int j = 0; j<  entry.get("senses").size(); j++) {
                        String clue = entry.get( "senses").get( j);
                        if ( !clue.equalsIgnoreCase( answers[i]) && !(clue.toLowerCase().contains( answers[i].toLowerCase())) )  {
                            newClues [i] = clue;
                            System.out.println( "A sense is found");
                            found = true;
                            break;
                        }
                    }
                    String clue = "";
                    if (!found) {
                        System.out.println( "Description is found.");
                        clue = entry.get("description").get(0);
                        newClues[i] = clue.substring(0, 2).toUpperCase() + clue.substring(2);
                        isDescription = true;
                    }
                }
                if (!isDescription) {
                    Stanford nlPpos = new Stanford(answers[i]);
                    if (nlPpos.isPlural)
                        newClues[i] += " (plural)";
                    if (nlPpos.isPast)
                        newClues[i] += " (past form)";
                }
                newClues[i] = newClues [i].substring(0,1).toUpperCase() + newClues[i].substring( 1);
                if(newClues[i].length() > 35)
                    newClues[i] = newClues[i].substring(0, 20) + newClues[i].substring(20).replaceFirst(" ", "\n");

            }
            else {
                System.out.println( "Answer is not found in WordNet. Checking StanfordCoreNLP");
                boolean found = false;
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                Stanford nlPpos = new Stanford( answers[i].toUpperCase());

             if ( nlPpos.isPerson) {
                 System.out.println( "Answer is a Person. Checking Google");
                    String clue = "";
                 ChromeOptions options = new ChromeOptions();
                 options.addArguments("--lang=en-GB");
                 WebDriver driver = new ChromeDriver(options);
                    driver.get( "https://www.google.com");
                    driver.manage().window().maximize();
                    WebElement browser = driver.findElement( By.name("q"));
                    browser.sendKeys( answers[i] + "\n");
                 //   browser.submit();
                 WebElement myDynamicElement = (new WebDriverWait(driver, 10))
                         .until(ExpectedConditions.presenceOfElementLocated(By.id("result-stats")));

                 try {

                     Thread.sleep( 4000);

                 WebElement job = driver.findElement(By.className("SPZz6b"));
                        clue = job.getText() ;
                     System.out.println(clue);
                       answers[i] =  answers[i].toLowerCase();
                     String first =  answers[i].substring(0,1).toUpperCase();
                     first = first + answers[i].substring(1);
                     answers[i] = first.substring(1);
//                  clue =  clue.replace( answers[i], " _____ is: ");
                     clue =  clue.replace(clue.substring(0, clue.indexOf('\n') + 1), "_____ is: ");
                     clue = clue.substring(clue.indexOf("Member(s) List") + 13, clue.substring(clue.indexOf("Member(s) List")).indexOf("."));

                        newClues[i] = clue;
                        found = true;
                        driver.close();
                        driver.quit();
                    }catch ( Exception e) {
                        found = false;
                        driver.close();
                        driver.quit();
                    }
                }
             else if ( nlPpos.isPlace) {
                 System.out.println( "Answer is a Place. Checking Google");
                 ChromeOptions options = new ChromeOptions();
                 options.addArguments("--lang=en-GB");
                 WebDriver driver = new ChromeDriver(options);
                 driver.get( "https://www.google.com");
                 driver.manage().window().maximize();
                 WebElement browser = driver.findElement( By.name("q"));
                 browser.sendKeys( answers[i] + "\n");
                 //   browser.submit();
                 WebElement myDynamicElement = (new WebDriverWait(driver, 10))
                         .until(ExpectedConditions.presenceOfElementLocated(By.id("result-stats")));

                 try {
                     WebElement element = driver.findElement(By.className("wwUB2c PZPZlf E75vKf"));
                     String clue = element.getText();
                     int index = clue.indexOf("\n");
                     clue = clue.substring(index);
                     newClues[i] = clue;
                     driver.close();
                     driver.quit();
                     found = true;
                 }catch ( Exception e) {
                     driver.close();
                     driver.quit();
                     found = false;
                 }
             }
             if (!found) {
                 System.out.println("Nothing is found. Paraphrasing the clue");
                 System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                 WebDriver driver = null;

                 Paraphrase paraphrase;
                 if (isAcross) {
                     paraphrase = new Paraphrase(driver, acrossClues[i]);
                     newClues[i] = paraphrase.getGeneratedClue();
                 } else {
                     paraphrase = new Paraphrase(driver, downClues[i]);
                     newClues[i] = paraphrase.getGeneratedClue();
                 }

             }
            }
            System.out.println( "----------------------------------------------------------------------------");
        }


        return newClues;
    }

    public String[] getAcrossWords() {
        return acrossWords;
    }

    public String[] getDownWords() {
        return downWords;
    }

    public String[] getNewAcrossClues() {
        return newAcrossClues;
    }

    public String[] getNewDownClues() {
        return newDownClues;
    }
}
