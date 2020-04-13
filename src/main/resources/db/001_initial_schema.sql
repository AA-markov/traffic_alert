drop schema if exists "traffic_limits" cascade;
create schema traffic_limits;

set search_path = traffic_limits;

CREATE TABLE limits_per_hour
(
    limit_name            text NOT NULL,
    limit_value           bigint NOT NULL,
    effective_date        bigint NOT NULL,
    PRIMARY KEY (limit_name, effective_date)
);

INSERT INTO limits_per_hour VALUES ('min', 1024, 1586000000),
                                   ('max', 1073741824, 1586000000);