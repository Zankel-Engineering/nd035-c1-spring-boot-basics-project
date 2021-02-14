package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFiles(int userId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    File getFile(String fileName, int userId);

    @Delete("DELETE FROM FILES WHERE userid = #{userId} and filename = #{fileName}")
    void deleteFile(String fileName, int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    void insert(String fileName, String contentType, String fileSize, int userId, byte[] fileData);

    @Delete("truncate table FILES")
    void truncate();
}
