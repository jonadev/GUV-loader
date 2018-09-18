package coop.bancocredicoop.guv.loader.repositories;

import coop.bancocredicoop.guv.loader.models.GUVConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getValueOf(GUVConfig configId) {
        String sql = "SELECT valor FROM guvconfig WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {configId.toString()}, String.class);
    }
}
