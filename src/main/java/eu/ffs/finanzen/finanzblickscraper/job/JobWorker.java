package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.csv.BuchungDTO;
import eu.ffs.finanzen.finanzblickscraper.csv.CSVReader;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperConfig;
import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import eu.ffs.finanzen.finanzblickscraper.service.BuchungsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
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
        System.out.println("starting scraping...");
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
    public void performInitImports() throws Exception {
        performImport(Paths.get("/opt/docker_share/init"), true);
    }

    @Scheduled(fixedRate = 100000L)
    public void scheduledImporter() throws Exception {
        performImport(Paths.get("/opt/docker_share"), false);
    }

    public void performImport(Path filesPath, boolean init) throws Exception {
        System.out.println("called performImport");
        try (Stream<Path> paths = Files.walk(filesPath)) {
            System.out.println("starting to process...");
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            System.out.println("checking path: " + path);
                            File file = path.toFile();
                            List<BuchungDTO> buchungDTOS = csvReader.doRead(file);
                            System.out.println("processing " + buchungDTOS.size() + " buchungen");
                            buchungsService.processBuchungen(buchungDTOS, init);
                            file.renameTo(new File("/opt/docker_share/archive/" + file.getName()));
                            file.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

}
