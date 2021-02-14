package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    Credential getCredentialById(int userId, int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, password, userid) VALUES(#{url}, #{username}, #{password}, #{userId})")
    void insert(String url, String username, String password, int userId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} WHERE credentialid = #{credentialId} AND userid=#{userId}")
    void update(int credentialId, String url, String username, String password, int userId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid=#{userId}")
    void delete(int credentialId, int userId);

    @Delete("truncate table CREDENTIALS")
    void truncate();

}
