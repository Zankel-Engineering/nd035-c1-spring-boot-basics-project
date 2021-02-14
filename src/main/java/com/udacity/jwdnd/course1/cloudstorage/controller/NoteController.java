package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@Controller()
@RequestMapping("/notes")
public class NoteController {

    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /** Controller method to add notes */
    @PostMapping()
    public String createUpdateNote(@ModelAttribute Note note, Model model) throws AccountNotFoundException {
        if (note.getNoteId() == null) {
            this.noteService.create(note);
        } else {
            this.noteService.update(note);
        }
        return "redirect:/result?lastView=note&state=success";
    }

    /** Controller method to add notes */
    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") String noteId, Model model) throws AccountNotFoundException {
        this.noteService.delete(Integer.parseInt(noteId));
        return "redirect:/result?lastView=note&state=success";
    }
}
