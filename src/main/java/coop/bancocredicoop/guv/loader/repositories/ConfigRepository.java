package coop.bancocredicoop.guv.loader.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getCronTime() {
        String sql = "SELECT valor FROM guvconfig WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {"LOADER_CRON_VALUE"}, String.class);
    }
}
