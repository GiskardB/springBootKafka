package com.kafka.consumerDirty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public int insert(User user) {
        return jdbcTemplate.update("insert into user (id, value) " + "values(?,  ?)",
                new Object[]{user.getId(), user.getValue()});
    }


}
