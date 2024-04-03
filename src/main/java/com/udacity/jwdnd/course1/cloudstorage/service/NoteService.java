package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    @Autowired
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotesByUserId(userId);
    }

    public Note getNoteByIdAndUserId(Integer noteId, Integer userId) {
        return noteMapper.getNoteByIdAndUserId(noteId, userId);
    }

    public int createOrUpdateNote(Note note) {
        if (note.getNoteId() != null) {
            return noteMapper.update(note);
        } else {
            return noteMapper.insert(note);
        }
    }

    public int deleteNoteById(Integer noteId, Integer userId) {
        return noteMapper.delete(noteId, userId);
    }
}
