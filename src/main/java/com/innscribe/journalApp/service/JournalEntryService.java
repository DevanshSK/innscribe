package com.innscribe.journalApp.service;

import com.innscribe.journalApp.entity.JournalEntry;
import com.innscribe.journalApp.entity.User;
import com.innscribe.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> getAllEntriesByUser(String username) {
        // Search user by username
        User user = userService.findByUsername(username);
        if (user != null) {
            return user.getJournalEntries();
        }
        return null;
    }


    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String username) {
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

        User user = userService.findByUsername(username);
        if (user != null) {
            user.getJournalEntries().add(savedEntry);
            userService.saveUser(user);
        }
        return savedEntry;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String username) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);

        if (entry.isPresent()) {
            // Remove entry reference from User.
            User user = userService.findByUsername(username);
            if (user != null) {
                user.getJournalEntries().removeIf(x -> x.getId().equals(id));
                userService.saveUser(user);
            }
            journalEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
