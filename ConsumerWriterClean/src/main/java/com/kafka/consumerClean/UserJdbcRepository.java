package com.kafka.consumerClean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public int insert(User user) {
        return jdbcTemplate.update("insert into user (name, birthday, value) " + "values(?, ?,  ?)",
                new Object[]{user.getName(), user.getBirthday(), user.getValue()});
    }


}
