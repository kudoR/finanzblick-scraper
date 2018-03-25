package eu.ffs.finanzen.finanzblickscraper.scraper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class ScraperConfig {

    @Value("${selenium.address}")
    private String seleniumAddress;

    @Value("${finanzblick.address}")
    private String finanzblickAdress;

    public String getSeleniumAddress() {
        return seleniumAddress;
    }

    public void setSeleniumAddress(String seleniumAddress) {
        this.seleniumAddress = seleniumAddress;
    }

    public String getFinanzblickAdress() {
        return finanzblickAdress;
    }

    public void setFinanzblickAdress(String finanzblickAdress) {
        this.finanzblickAdress = finanzblickAdress;
    }
}
