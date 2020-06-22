package com.company;

import java.awt.GraphicsEnvironment;

import net.sf.extjwnl.JWNLException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import javax.swing.JFrame;

public class AIDemo {

    static final int CELL_SIZE = 5;
    static String[] acrossClues = new String[CELL_SIZE];
    static String[] downClues = new String[CELL_SIZE];
    static int[] acrossClueNumbers = new int[CELL_SIZE];
    static int[] downClueNumbers = new int[CELL_SIZE];
    static boolean[][] blackCells = new boolean[CELL_SIZE][CELL_SIZE];
    static int[][] littleNumbers = new int[CELL_SIZE][CELL_SIZE];
    static String[][] solution = new String[CELL_SIZE][CELL_SIZE];

    static String date;

    public static void main(String[] args) throws InterruptedException, JWNLException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.nytimes.com/crosswords/game/mini");
        Thread.sleep(1000);
        WebElement okButton = driver.findElement(By.xpath("//span[text() = 'OK']"));
        okButton.click();
        Thread.sleep(2000);

        String x = "";
        ArrayList<WebElement> temp = (ArrayList<WebElement>) driver.findElements(By.className("ClueList-wrapper--3m-kd"));
        for (WebElement clue : temp) {
            //System.out.println(clue.getText());
            x += clue.getText();
        }

        String clues[] = x.split("DOWN");
        String cluesAcross = clues[0];
        String cluesDown = clues[1];

        String cluesAcrossArray[] = cluesAcross.split("\\r?\\n");
        String cluesDownArray[] = cluesDown.split("\\r?\\n");

        int clueCount = 0;
        int numberCount = 0;
        for (int i = 1; i < cluesAcrossArray.length; i++) {
            //System.out.println(cluesAcrossArray[i] + " at number: " + i);
            if (i % 2 == 0) {
                acrossClues[clueCount] = cluesAcrossArray[i];
                clueCount++;
            } else {
                acrossClueNumbers[numberCount] = Integer.parseInt(cluesAcrossArray[i]);
                numberCount++;
            }
        }

        clueCount = 0;
        numberCount = 0;
        for (int i = 1; i < cluesDownArray.length; i++) {
            //System.out.println(cluesAcrossArray[i] + " at number: " + i);
            if (i % 2 == 0) {
                downClues[clueCount] = cluesDownArray[i];
                clueCount++;
            } else {
                downClueNumbers[numberCount] = Integer.parseInt(cluesDownArray[i]);
                numberCount++;
            }
        }

        /*for (int i = 0; i < acrossClueNumbers.length; i++) {
            System.out.println(acrossClueNumbers[i] + " at number: " + i);
        }*/
        WebElement revealButton = driver.findElement(By.xpath("//button[text() = 'reveal']"));
        revealButton.click();
        Thread.sleep(1000);
        WebElement puzzleButton = driver.findElement(By.linkText("Puzzle"));
        puzzleButton.click();
        Thread.sleep(1000);
        WebElement revealButton2 = driver.findElement(By.xpath("//span[text() = 'Reveal']"));
        revealButton2.click();
        Thread.sleep(1000);
        WebElement closeButton = driver.findElement(By.xpath("//span[@class = 'ModalBody-closeX--2Fmp7']"));
        closeButton.click();
        Thread.sleep(1000);

        ArrayList<WebElement> answers = (ArrayList<WebElement>) driver.findElements(By.tagName("g"));
        answers.remove(0);
        answers.remove(0);
        answers.remove(0);
        answers.remove(0);
        answers.remove(answers.size() - 1);

        ArrayList<Integer> blackCellIndexes = new ArrayList<Integer>();
        ArrayList<Integer> solutionIndexes = new ArrayList<Integer>();
        ArrayList<Integer> littleNumberIndexes = new ArrayList<Integer>();
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getText().equals("")) {
                blackCellIndexes.add(i);
            } else {
                String str = answers.get(i).getText();
                if (str.length() == 1) {
                    solutionIndexes.add(i);
                } else {
                    littleNumberIndexes.add(i);
                }
            }
        }
        blackCells = new boolean[CELL_SIZE][CELL_SIZE];
        littleNumbers = new int[CELL_SIZE][CELL_SIZE];
        solution = new String[CELL_SIZE][CELL_SIZE];

        for (int i = 0; i < blackCellIndexes.size(); i++) {
            int mod = blackCellIndexes.get(i) % CELL_SIZE;
            int division = blackCellIndexes.get(i) / CELL_SIZE;
            blackCells[division][mod] = true;
        }

        for (int i = 0; i < solutionIndexes.size(); i++) {
            int mod = solutionIndexes.get(i) % CELL_SIZE;
            int division = solutionIndexes.get(i) / CELL_SIZE;
            solution[division][mod] = answers.get(solutionIndexes.get(i)).getText();
        }

        for (int i = 0; i < littleNumberIndexes.size(); i++) {
            int mod = littleNumberIndexes.get(i) % CELL_SIZE;
            int division = littleNumberIndexes.get(i) / CELL_SIZE;
            String temp2[] = answers.get(littleNumberIndexes.get(i)).getText().split("\\r?\\n");
            solution[division][mod] = temp2[1];
            littleNumbers[division][mod] = Integer.parseInt(temp2[0]);
        }
        /*
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(solution[i][j] + ",");
            }
            System.out.println();
        }*/

        date = driver.findElement(By.className("PuzzleDetails-date--1HNzj")).getText();
        Thread.sleep(2000);
        driver.close();
        driver.quit();
        
       
        Cells cell = new Cells(blackCells, littleNumbers, solution, acrossClues, downClues, acrossClueNumbers, downClueNumbers);
        Puzzle puzzle = new Puzzle(cell, date, acrossClues, downClues, acrossClueNumbers, downClueNumbers);
        JFrame frame = new JFrame( "AIPLUS");
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        //frame.setState( JFrame.ICONIFIED);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.setExtendedState( frame.getExtendedState() | frame.MAXIMIZED_BOTH);

        frame.add( puzzle);
        frame.setLocationRelativeTo( null);

        frame.pack();
        Thread.sleep(1000);
        frame.setVisible(true);
        
        
    }

}
