package com.example.authdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authdemo.entity.Note;
import com.example.authdemo.entity.User;
import com.example.authdemo.repository.NoteRepository;
import com.example.authdemo.repository.UserRepository;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    //  GET MY NOTES
    @GetMapping
    public List<Note> getMyNotes(@AuthenticationPrincipal OAuth2User oauthUser) {

        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseThrow();

        return noteRepository.findByUser(user);
    }

    
 // ADD NOTE (JSON BODY)
    @PostMapping
    public Note addNote(@RequestBody Note incomingNote,
                        @AuthenticationPrincipal OAuth2User oauthUser) {

        if (oauthUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setContent(incomingNote.getContent()); //  ONLY CONTENT
        note.setUser(user);

        return noteRepository.save(note);
    }




    //  DELETE NOTE (ONLY OWNER)
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id,
                             @AuthenticationPrincipal OAuth2User oauthUser) {

        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseThrow();

        Note note = noteRepository.findById(id).orElseThrow();

        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        noteRepository.delete(note);
        return "Note deleted successfully";
    }
}
