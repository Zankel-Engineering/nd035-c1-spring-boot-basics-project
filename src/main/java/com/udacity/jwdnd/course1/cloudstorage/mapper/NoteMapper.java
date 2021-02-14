package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotes(int userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    void insert(String noteTitle, String noteDescription, int userId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE userid = #{userId} AND noteid = #{noteId}")
    void update(String noteTitle, String noteDescription, int userId, int noteId);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    void delete(int noteId, int userId);

    @Delete("truncate table NOTES")
    void truncate();
}
