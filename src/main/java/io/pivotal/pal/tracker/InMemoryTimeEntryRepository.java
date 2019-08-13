package io.pivotal.pal.tracker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> teMap = new ConcurrentHashMap<>();

    private AtomicLong idProvider = new AtomicLong(0);

    public TimeEntry create(TimeEntry timeEntry) {
        //long id, long userId, LocalDate parse, int hours
        TimeEntry te = new TimeEntry(idProvider.incrementAndGet(), timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        teMap.put(te.getId(), te);
        return te;

    }

    public void delete(long id) {
        teMap.remove(id);
    }

    public TimeEntry find(long id) {
        return teMap.get(id);
    }

    public List<TimeEntry> list() {
        return teMap.values().stream().collect(Collectors.toList());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {

        if (!teMap.containsKey(id)) {
            return null;
        }
        TimeEntry te = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        teMap.put(te.getId(), te);

        return te;
    }
}
