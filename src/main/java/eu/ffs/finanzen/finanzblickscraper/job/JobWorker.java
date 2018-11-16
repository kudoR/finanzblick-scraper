package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.csv.BuchungDTO;
import eu.ffs.finanzen.finanzblickscraper.csv.CSVReader;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperConfig;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import eu.ffs.finanzen.finanzblickscraper.service.BuchungsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

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
    public void performImport() throws Exception {
        try (Stream<Path> paths = Files.walk(Paths.get("/opt/docker_share"))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.endsWith("csv"))
                    .forEach(path -> {
                        try {
                            System.out.println("checking path: " + path);
                            List<BuchungDTO> buchungDTOS = csvReader.doRead(path.toFile());
                            System.out.println("processing " + buchungDTOS.size() + " buchungen");
                            buchungsService.processBuchungen(buchungDTOS);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

}
