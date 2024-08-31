package com.innscribe.journalApp.controller;

import com.innscribe.journalApp.entity.JournalEntry;
import com.innscribe.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;


    @GetMapping
    public List<JournalEntry> getAllJournalEntries() {
        // Return all journal entries.
        return journalEntryService.getAllEntries();
    }

    @PostMapping
    public JournalEntry createJournalEntry(@RequestBody JournalEntry journal) {
        // Set date of new journal entry.
        journal.setDate(LocalDateTime.now());
        // Create a new Journal Entry.
        journalEntryService.saveEntry(journal);
        return journal;
    }

    @GetMapping("id/{myID}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myID) {
        return journalEntryService.findById(myID).orElse(null);
    }

    @DeleteMapping("id/{myID}")
    public String deleteJournalEntryById(@PathVariable ObjectId myID) {
        journalEntryService.deleteById(myID);
        return "Successfully Deleted";
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
        // Get the old journal entry
        JournalEntry oldEntry = journalEntryService.findById(id).orElse(null);

        // Update the journal entries
        if(oldEntry != null){
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
        }

        // Save the journal entries.
        journalEntryService.saveEntry(oldEntry);
        return oldEntry;
    }
}
