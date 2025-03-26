package edu.uis.csc478.sp25.jobtracker.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://db.neuurudvqouzefhvugzt.supabase.co:5432/postgres");
        dataSource.setUsername("postgres"); // Supabase DB user
        dataSource.setPassword("a0SckOnMjadA170J"); // Supabase DP pass

        // Add important PostgreSQL settings
        dataSource.addDataSourceProperty("currentSchema", "public");
        dataSource.addDataSourceProperty("ApplicationName", "JobTracker");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
