package dins.project.repository;

import dins.project.model.Limit;
import dins.project.model.LimitType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
public class LimitRepositoryImpl implements LimitRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Limit> rowMapper = (rs, rowNum) -> Limit.builder()
            .type(LimitType.valueOf(rs.getString("limit_name").toUpperCase()))
            .value(rs.getLong("limit_value"))
            .effectiveDate(Instant.ofEpochSecond(rs.getLong("effective_date")))
            .build();

    @Override
    public boolean addLimit(LimitType type, Long value) {
        return jdbcTemplate.update("insert into traffic_limits.limits_per_hour(limit_name, limit_value, effective_date) values (?, ?, ?)" +
                        " on conflict do nothing", type.getName(), value, Instant.now().getEpochSecond()) == 1;
    }

    @Override
    public Limit getLatestValue(LimitType type) {
        return jdbcTemplate.queryForObject("select * from traffic_limits.limits_per_hour where limit_name = ? and effective_date = " +
                "(select MAX (effective_date) from traffic_limits.limits_per_hour where limit_name = ?)", new Object[]{type.getName(), type.getName()}, rowMapper);
    }
}