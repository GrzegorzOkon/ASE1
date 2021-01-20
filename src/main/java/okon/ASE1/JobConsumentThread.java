package okon.ASE1;

import java.util.List;

import static okon.ASE1.ASE1App.extractions;
import static okon.ASE1.ASE1App.jobs;

public class JobConsumentThread extends Thread {
    @Override
    public void run() {
        while (!jobs.isEmpty()) {
            Job job = null;
            synchronized (jobs) {
                if (!jobs.isEmpty()) {
                    job = jobs.poll();
                }
            }
            if (job != null) {
                List<Space> extractedData = JobExecutor.execute(job);
                synchronized (extractions) {
                    for (Space extraction : extractedData)
                        extractions.add(extraction);
                }
            }
        }
    }
}
