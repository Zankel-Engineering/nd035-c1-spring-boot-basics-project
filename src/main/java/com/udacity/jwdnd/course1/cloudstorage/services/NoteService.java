package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteService {

    private final UserService userService;
    private final NoteMapper noteMapper;

    public NoteService(UserService userService, NoteMapper noteMapper) {
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    public void create(Note note) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.noteMapper.insert(note.getNoteTitle(), note.getNoteDescription(), userId);
    }

    public void update(Note note) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.noteMapper.update(note.getNoteTitle(), note.getNoteDescription(), userId, note.getNoteId());
    }

    public void delete(int noteId) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.noteMapper.delete(noteId, userId);
    }

    public List<Note> getNotes() throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        return this.noteMapper.getNotes(userId);
    }
}
