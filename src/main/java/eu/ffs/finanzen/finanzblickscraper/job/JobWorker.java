package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.csv.BuchungDTO;
import eu.ffs.finanzen.finanzblickscraper.csv.CSVReader;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperConfig;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import eu.ffs.finanzen.finanzblickscraper.service.BuchungsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import eu.ffs.finanzen.finanzblickscraper.repository.BuchungRepository;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class JobWorker {
    private ScraperJob scraperJob;
    private CSVReader csvReader;
    private ScraperConfig scraperConfig;
    private Boolean jobRunning;
    private BuchungsService buchungsService;

    @Autowired
    public JobWorker(ScraperJob scraperJob, CSVReader csvReader, ScraperConfig scraperConfig, BuchungsService buchungsService) {
        this.buchungsService = buchungsService;
        this.jobRunning = false;
        this.scraperJob = scraperJob;
        this.csvReader = csvReader;
        this.scraperConfig = scraperConfig;
    }

    @Scheduled(fixedRateString = "${job.scheduler.rate}")
    public void performScraping() throws InterruptedException, MalformedURLException {
        synchronized (jobRunning) {
            if (!jobRunning) {
                jobRunning = true;
                String userName = scraperConfig.getUserName();
                String password = scraperConfig.getPassword();
                this.scraperJob.getExport(userName, password);
                jobRunning = false;
            }
        }
    }

    @Scheduled(fixedRate = 10000L)
    public void performImport() {
        String downloadFilePath = "/opt/docker_share/Buchungsliste.csv";
        List<BuchungDTO> buchungen = null;
        try {
            buchungen = csvReader.doRead(downloadFilePath);
            buchungsService.processBuchungen(buchungen);
        } catch (FileNotFoundException e) {
            System.out.println("No file found for import. Will try again later.");
        }
    }

}
