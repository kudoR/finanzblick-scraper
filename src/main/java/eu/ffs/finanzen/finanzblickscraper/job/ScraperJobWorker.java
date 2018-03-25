package eu.ffs.finanzen.finanzblickscraper.job;

import eu.ffs.finanzen.finanzblickscraper.scraper.ScraperJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScraperJobWorker {
    private ScraperJob scraperJob;

    @Autowired
    public ScraperJobWorker(ScraperJob scraperJob) {
        this.scraperJob = scraperJob;
    }

    @Scheduled(fixedRateString = "${job.scheduler.rate}")
    public void performScraping() throws InterruptedException {
        this.scraperJob.getExport("johannes.hinkov@gmail.com", "");
    }

}
