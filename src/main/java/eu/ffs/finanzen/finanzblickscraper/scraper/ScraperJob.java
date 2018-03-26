package eu.ffs.finanzen.finanzblickscraper.scraper;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScraperJob extends ScraperBase {

    ScraperConfig scraperConfig;

    @Autowired
    public ScraperJob(ScraperConfig scraperConfigBean) {
        super(scraperConfigBean.getSeleniumAddress());
        this.scraperConfig = scraperConfigBean;
    }

    @Override
    public void getExport(String user, String pw) throws InterruptedException {
        driver.get(this.scraperConfig.getFinanzblickAdress());

        fillInput(By.id("ms-input-uname"),user);
        fillInput(By.id("ms-input-pword"),pw);

        waitUntilClickableAndThenClickOn(By.id("ms-button-login"));

        waitUntilClickableAndThenClickOn(By.id("menu-account"));
        waitUntilClickableAndThenClickOn(By.id("top-container-print-btn"));
        waitUntilClickableAndThenClickOn(By.id("popup-new-statements-date-btn-csv"));


    }
}
