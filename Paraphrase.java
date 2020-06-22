package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Paraphrase {
    private WebDriver driver;
    private String generatedClue;
    private String clue;
   public Paraphrase(WebDriver driver, String clue) {
       this.driver = driver;
       this.clue = clue;
        goToWebsite();
        enterClue();
    }
    private void goToWebsite() {
       // Driver is being initialized for the first time
        if ( driver == null) {
            driver = new ChromeDriver();
            driver.get("https://codebeautify.org/paraphrasing-tool");  // Go to URL
            driver.manage().window().maximize();  // Maximize Screen

        }
        // Refresh the existing page
        else {
            driver.navigate().refresh();
        }
    }

    private void enterClue() {
       sleep( 1000);
        WebElement textArea = driver.findElement(By.className("ace_text-input"));
        int i = 3;
        textArea.sendKeys(clue);
        sleep(2000);
        while ( i > 0 ) {
            WebElement button = driver.findElement(By.id("defaultaction"));
            button.click();
            sleep(5000);
            generatedClue = driver.findElement(By.id("mainRightDiv")).getText();
            generatedClue = generatedClue.substring(9);
            sleep(2000);
            if ( ! generatedClue.equalsIgnoreCase( clue)) {
                break;
            }
            i--;
        }

        driver.close();
        driver.quit();

    }
    private void sleep(int ms) {
        try {
            Thread.sleep( ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getGeneratedClue() {
        return generatedClue;
    }
}
