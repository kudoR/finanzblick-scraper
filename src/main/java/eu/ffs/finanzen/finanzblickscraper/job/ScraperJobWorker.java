package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.csv.CSVReader;
import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperConfig;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import eu.ffs.finanzen.finanzblickscraper.repository.BuchungRepository;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class ScraperJobWorker {
    private ScraperJob scraperJob;
    private CSVReader csvReader;
    private BuchungRepository buchungRepository;
    private ScraperConfig scraperConfig;

    @Autowired
    public ScraperJobWorker(ScraperJob scraperJob, CSVReader csvReader, BuchungRepository buchungRepository, ScraperConfig scraperConfig) {
        this.scraperJob = scraperJob;
        this.csvReader = csvReader;
        this.buchungRepository = buchungRepository;
        this.scraperConfig = scraperConfig;
    }

    @Scheduled(fixedRateString = "${job.scheduler.rate}")
    public void performScraping() throws InterruptedException, MalformedURLException {
        String userName = scraperConfig.getUserName();
        String password = scraperConfig.getPassword();
        this.scraperJob.getExport(userName, password);
    }

    @Scheduled(fixedRate = 1000L)
    public void performImport() throws FileNotFoundException {
        String downloadFilePath = "/opt/docker_share/Buchungsliste.csv";
        List<Buchung> buchungen = csvReader.doRead(downloadFilePath);

        this.buchungRepository.saveAll(buchungen);

    }

}
