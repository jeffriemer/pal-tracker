package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping(path="/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        TimeEntry te = timeEntryRepository.create(timeEntryToCreate);
        ResponseEntity<TimeEntry> response = new ResponseEntity<TimeEntry>(te, HttpStatus.CREATED);
        return response;
    }

    @GetMapping(path="/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry te = timeEntryRepository.find(id);
        if(te == null){
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity<TimeEntry>(te, HttpStatus.OK);
    }

    @GetMapping(path="/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        ResponseEntity<List<TimeEntry>> response = new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
        return response;
    }
    @PutMapping(path="/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry te = timeEntryRepository.update(timeEntryId,expected);
        if(te == null){
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        ResponseEntity<TimeEntry> response = new ResponseEntity<TimeEntry>(te, HttpStatus.OK);
        return response;
    }

    @DeleteMapping(path="/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
