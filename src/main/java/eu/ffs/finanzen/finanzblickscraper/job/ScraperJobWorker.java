package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.csv.CSVReader;
import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import eu.ffs.finanzen.finanzblickscraper.repository.BuchungRepository;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class ScraperJobWorker {
    private ScraperJob scraperJob;
    private CSVReader csvReader;
    private BuchungRepository buchungRepository;

    @Autowired
    public ScraperJobWorker(ScraperJob scraperJob, CSVReader csvReader, BuchungRepository buchungRepository) {
        this.scraperJob = scraperJob;
        this.csvReader = csvReader;
        this.buchungRepository = buchungRepository;
    }

    @Scheduled(fixedRateString = "${job.scheduler.rate}")
    public void performScraping() throws InterruptedException {
        this.scraperJob.getExport("johannes.hinkov@gmail.com", ""); // TODO pw...
    }

    @Scheduled(fixedRate = 1000L)
    public void performImport() throws FileNotFoundException {
        String downloadFilePath = "/home/seluser/Downloads/Buchungsliste.csv"; // TODO lokaler Pfad passt nicht...
        List<Buchung> buchungen = csvReader.doRead(downloadFilePath);

        this.buchungRepository.saveAll(buchungen);

    }

}
