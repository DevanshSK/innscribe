package com.innscribe.journalApp.controller;

import com.innscribe.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    // Create a local hashmap to store the journal entries.
    private final Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAllJournalEntries(){
        // Return all journal entries.
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createJournalEntry(@RequestBody JournalEntry journal){
        // Create a new Journal Entry.
        journalEntries.put(journal.getId(), journal);
        return true;
    }

    @GetMapping("id/{myID}")
    public JournalEntry getJournalEntryById(@PathVariable Long myID){
        return journalEntries.get(myID);
    }

    @DeleteMapping("id/{myID}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long myID){
        return journalEntries.remove(myID);
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable Long id, @RequestBody JournalEntry journal){
        return journalEntries.put(id, journal);
    }
}
