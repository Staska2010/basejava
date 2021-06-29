create table resume
(
    uuid      char(36) not null
        constraint resume_pk
            primary key,
    full_name text     not null
);

create table contact
(
    id          serial primary key,
    type        text     not null,
    resume_uuid char(36) not null references resume on delete cascade
);

create index contact_uuid_type_index
    on contact (resume_uuid, type);

