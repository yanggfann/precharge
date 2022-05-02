create table precharge
(
    id          uuid        not null,
    contract_id uuid        not null,
    amount      decimal     not null,
    created_at  timestamptz not null DEFAULT CURRENT_TIMESTAMP,
    updated_at  timestamptz not null DEFAULT CURRENT_TIMESTAMP,
    primary key (id)
);
