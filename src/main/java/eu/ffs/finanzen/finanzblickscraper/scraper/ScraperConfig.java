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

    @Value("${finanzblick.user}")
    private String userName;

    @Value("${finanzblick.pw}")
    private String password;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
