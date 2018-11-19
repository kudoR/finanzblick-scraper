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
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.By.id;

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
        //driver.quit();
        //this.driver = null;
    }

    private boolean refreshAccounts() throws InterruptedException {
        waitUntilClickableAndThenClickOn(id("menu-dashboard"));
        waitUntilClickableAndThenClickOn(id("dashboard-btn-update"));
        waitUntilClickableAndThenClickOn(id("popup-modal-btn-ok"));
        return true;
    }

    private boolean getExportDefault(String user, String pw) {
        try {
            System.out.println("Trying default flow...");

            fillInput(id("ms-input-uname"), user);
            fillInput(id("ms-input-pword"), pw);

            waitUntilClickableAndThenClickOn(id("ms-button-login"));

            refreshAccounts();

            waitUntilClickableAndThenClickOn(id("menu-account"));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'general-card')]")));
            List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'general-card')]"));
            System.out.println("card-image Elements found: " + elements.size());
            int j = 0;
            for (WebElement element : elements) {
                System.out.println("clicking on element no.: " + j++);
                element.click();
                System.out.println("clicked... searching for print button");
                waitUntilClickableAndThenClickOn(id("top-container-print-btn"));
                System.out.println("clicked on print button");
                waitUntilClickableAndThenClickOn(id("popup-new-statements-date-btn-csv"));
                for (int i = 20; i > 0; i--) {
                    wait.withTimeout(1, SECONDS);
                    System.out.println("Waiting..." + i);
                }
            }

            System.out.println("Default flow was successful.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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

            waitUntilClickableAndThenClickOn(id(selector));

            waitUntilClickableAndThenClickOn(id("menu-account"));
            waitUntilClickableAndThenClickOn(id("top-container-print-btn"));
            waitUntilClickableAndThenClickOn(id("popup-new-statements-date-btn-csv"));

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
