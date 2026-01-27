package com.example.authdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authdemo.entity.Note;
import com.example.authdemo.entity.User;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);
}
