package com.okta.examples.repository;

import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.model.User;
import org.apache.ibatis.annotations.*;

public interface MyBatisRepository {

    final String login = "select * from users where email = #{email} and password = #{password}";
    @Select(login)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "telephone", column = "telephone")
    })
    User login(@Param("email") String email, @Param("password") String password);

    final String findByTelephone = "select * from users where telephone = #{telephone}";
    @Select(findByTelephone)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "first_name", column = "first_name"),
            @Result(property = "last_name", column = "last_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "is_active", column = "is_active")
    })
    User findByTelephone(@Param("telephone") String telephone);

    final String register = "INSERT INTO users (id, email, telephone, first_name, last_name, password)" +
                            " VALUES( #{id}, #{email}, #{telephone}, #{first_name}, #{last_name}, #{password}) ";
    @Insert(register)
    void register(RegisterRequest registerRequest);

    final String checkEmailandTelephone = " SELECT count(*) from users where email = #{email} or telephone = #{telephone}";
    @Select(checkEmailandTelephone)
    int checkEmailandTelephone(@Param("email") String email, @Param("telephone") String telephone);

    final String setActiveId = "UPDATE users set is_active = 1 where id = #{id}";
    @Update(setActiveId)
    void setActiveId(@Param("id") String id);

    final String setActiveTelephone = "UPDATE users set is_active = 1 where telephone = #{telephone}";
    @Update(setActiveTelephone)
    void setActiveTelephone(@Param("telephone") String telephone);

    final String saveToken = "INSERT INTO tokens (id_user, token, created_at, updated_at) VALUES" +
                            " (#{id}, #{token}, NOW(), NOW())";
    @Insert(saveToken)
    void saveToken (@Param("id") String id, @Param("token") String token);

    final String updateToken = "UPDATE tokens set token = #{token} ,updated_at = NOW() where id_user = #{id}";
    @Update(updateToken)
    void updateToken(@Param("id") String id, @Param("token") String token);

    final String checkToken = "SELECT token from tokens where id_user = #{id} ORDER BY created_at DESC LIMIT 1";
    @Select(checkToken)
    @Results(value = {
        @Result(property = "token", column = "token")
    })
    String checkToken (@Param("id") String id);

    final String setTokenInvalid = "UPDATE tokens set is_valid = 0 where id_user = #{id}";
    @Update(setTokenInvalid)
    void setTokenInvalid (@Param("id") String id);

    final String logoutToken = "UPDATE tokens set is_valid = 0 where id = {id} and token = #{token}";
    @Update(logoutToken)
    void logoutToken (@Param("id") String id, @Param("token") String token);
}
