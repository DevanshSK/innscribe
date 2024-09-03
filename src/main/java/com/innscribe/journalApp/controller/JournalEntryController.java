package com.innscribe.journalApp.controller;

import com.innscribe.journalApp.entity.JournalEntry;
import com.innscribe.journalApp.entity.User;
import com.innscribe.journalApp.service.JournalEntryService;
import com.innscribe.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("/user/{username}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(@PathVariable String username) {
        try{
            List<JournalEntry> allEntries = journalEntryService.getAllEntriesByUser(username);
            if(allEntries != null && !allEntries.isEmpty()){
                return new ResponseEntity<>(allEntries, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // No entries found
        } catch (Exception e) {
            // Handle unexpected errors.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/user/{username}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journal, @PathVariable String username) {
        try{
            // Create a new Journal Entry.
            JournalEntry savedEntry = journalEntryService.saveEntry(journal, username);
            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
        }catch(Exception e){
            System.out.println("Main exception : " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        try{
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/user/{username}")
    public ResponseEntity<String> deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String username) {
        try{
            boolean isDeleted = journalEntryService.deleteById(id, username);
            if(isDeleted){
                return new ResponseEntity<>("Journal entry was deleted successfully.", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("Journal Entry not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting journal entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/user/{username}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(
            @PathVariable ObjectId id,
            @PathVariable String username,
            @RequestBody JournalEntry newEntry
    ) {
        try {
            Optional<JournalEntry> optionalOldEntry = journalEntryService.findById(id);
            if(optionalOldEntry.isPresent()){
                JournalEntry oldEntry = optionalOldEntry.get();
                if (newEntry.getTitle() != null && !newEntry.getTitle().equals("")) {
                    oldEntry.setTitle(newEntry.getTitle());
                }
                if (newEntry.getContent() != null && !newEntry.getContent().equals("")) {
                    oldEntry.setContent(newEntry.getContent());
                }
                JournalEntry savedEntry = journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(savedEntry, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
