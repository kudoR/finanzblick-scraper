package eu.ffs.finanzen.finanzblickscraper.scraper;

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

    }
}
