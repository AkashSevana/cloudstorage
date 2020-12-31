package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteService {

    private UserService userService;
    private NoteMapper noteMapper;

    public NoteService(UserService userService, NoteMapper noteMapper) {
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(String userName) {
        User user = userService.getUser(userName);
        log.info("Getting stored notes for {}", user.getUserId());
        return noteMapper.getNotes(user.getUserId());
    }

    public String createNote(Note note, String userName) {
        User user = userService.getUser(userName);
        note.setUserId(user.getUserId());
        return String.valueOf(noteMapper.insertNote(note));
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public String updateNote(Note note, String userName) {
        User user = userService.getUser(userName);
        note.setUserId(user.getUserId());
        return String.valueOf(noteMapper.updateNote(note));
    }
}
