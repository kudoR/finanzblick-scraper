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
public class JobWorker {
    private ScraperJob scraperJob;
    private CSVReader csvReader;
    private BuchungRepository buchungRepository;
    private ScraperConfig scraperConfig;
    private Boolean jobRunning;

    @Autowired
    public JobWorker(ScraperJob scraperJob, CSVReader csvReader, BuchungRepository buchungRepository, ScraperConfig scraperConfig) {
        this.jobRunning = false;
        this.scraperJob = scraperJob;
        this.csvReader = csvReader;
        this.buchungRepository = buchungRepository;
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
        List<Buchung> buchungen = null;
        try {
            buchungen = csvReader.doRead(downloadFilePath);
            this.buchungRepository.saveAll(buchungen);
        } catch (FileNotFoundException e) {
            System.out.println("No file found for import. Will try again later.");
        //    e.printStackTrace();
        }
    }

}
