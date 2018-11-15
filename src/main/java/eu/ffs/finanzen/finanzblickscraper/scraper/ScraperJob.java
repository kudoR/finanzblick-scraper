package eu.ffs.finanzen.finanzblickscraper.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class ScraperJob {

    ScraperConfig scraperConfig;
    RemoteWebDriver driver;
    FluentWait wait;

    @Autowired
    public ScraperJob(ScraperConfig scraperConfigBean) {
        this.scraperConfig = scraperConfigBean;
    }

    public void getExport(String user, String pw) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", "/home/seluser/Downloads");
        options.setExperimentalOption("prefs", prefs);
        this.driver = new RemoteWebDriver(
                new URL(scraperConfig.getSeleniumAddress()),
                options
        );

        if (this.wait == null) {
            this.wait = new FluentWait<>(driver)
                    .withTimeout(60, SECONDS)
                    .pollingEvery(5, SECONDS)
                    .ignoring(NoSuchElementException.class);
        }

        driver.get(this.scraperConfig.getFinanzblickAdress());

        // try with default flow
        boolean success = getExportDefault(user, pw);

        // default flow was not successful. either there is a hint to be confirmed ...
        if (!success) {
            success = getExportWithConfirmOldSession();
        }

        // ... or the buttons are minimzed


        // quit sesssion
        //driver.close();
        driver.quit();
        //this.driver = null;
    }

    private boolean getExportDefault(String user, String pw) {
        try {
            System.out.println("Trying default flow...");

            fillInput(By.id("ms-input-uname"), user);
            fillInput(By.id("ms-input-pword"), pw);

            waitUntilClickableAndThenClickOn(By.id("ms-button-login"));

            waitUntilClickableAndThenClickOn(By.id("menu-account"));
            waitUntilClickableAndThenClickOn(By.id("top-container-print-btn"));
            waitUntilClickableAndThenClickOn(By.id("popup-new-statements-date-btn-csv"));

            while (!fileDownloaded()) {
                System.out.println("Waiting for file download to complete...");
                Thread.sleep(1000);
            }

            wait.withTimeout(10, TimeUnit.SECONDS);

            System.out.println("Default flow was successful.");
            return true;
        } catch (Exception e) {
            System.out.println("Default flow was not successful.");
            return false;
        }
    }

    private boolean fileDownloaded() {
        String downloadFilePath = "/opt/docker_share/Buchungsliste.csv";
        File file = new File(downloadFilePath);
        return file.exists();
    }

    private boolean getExportWithConfirmOldSession() {
        try {
            System.out.println("Trying confirm old session flow...");
            String selector = "popup-user-is-online-btn-ok";

            waitUntilClickableAndThenClickOn(By.id(selector));

            waitUntilClickableAndThenClickOn(By.id("menu-account"));
            waitUntilClickableAndThenClickOn(By.id("top-container-print-btn"));
            waitUntilClickableAndThenClickOn(By.id("popup-new-statements-date-btn-csv"));

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    protected void confirmDownloadwindow() throws InterruptedException, AWTException {
        Thread.sleep(10000);

        // Create object of Robot class
        Robot object = new Robot();

        object.keyPress(KeyEvent.VK_DOWN);
        object.keyRelease(KeyEvent.VK_DOWN);

        object.keyPress(KeyEvent.VK_ENTER);
        object.keyRelease(KeyEvent.VK_ENTER);

    }

    protected void fillInput(By by, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.sendKeys(text);
    }

    protected void clickOnElemNotInViewport(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    protected void waitUntilVisibleAndThenClickOn(By by) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).click();
    }

    protected void waitUntilClickableAndThenClickOn(By by) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
    }

}
