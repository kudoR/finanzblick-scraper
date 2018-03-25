package eu.ffs.finanzen.finanzblickscraper.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public abstract class ScraperBase {
    WebDriver driver;
    Wait wait;

    public ScraperBase(String seleniumAdress) {

        try {
//            System.getProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
            this.driver = new RemoteWebDriver(
//                    new URL("http://selenium:4444/wd/hub"),
//                    new URL("http://localhost:4444/wd/hub"),
                    new URL(seleniumAdress),
                    DesiredCapabilities.chrome());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.wait = new FluentWait<>(driver)
                .withTimeout(60, SECONDS)
                .pollingEvery(5, SECONDS)
                .ignoring(NoSuchElementException.class);

    }

    public abstract void getExport(String user, String pw) throws InterruptedException;


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