CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250) UNIQUE,
    email VARCHAR(254) UNIQUE
);

create table if not exists locations
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    latitude  DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

create table if not exists categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

create table if not exists events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000),
    category_id        BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT REFERENCES users (id) ON DELETE CASCADE,
    location_id        BIGINT REFERENCES locations (id) ON DELETE CASCADE,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(255),
    title              VARCHAR(120),
    views              BIGINT
);

create table if not exists compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title  VARCHAR(50)
);

create table if not exists compilations_events
(
    event_id       BIGINT REFERENCES events (id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE
);

create table if not exists requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    events_id    BIGINT REFERENCES events (id) ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE,
    requester_id BIGINT REFERENCES users (id),
    status       VARCHAR(255),
    constraint UniqueConstraint unique (events_id, requester_id)
);