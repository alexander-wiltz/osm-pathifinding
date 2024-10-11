CREATE DATABASE IF NOT EXISTS wegfinder;

create table if not exists public.ways (
    id           bigint not null primary key,
    amenity      varchar(255),
    city         varchar(255),
    country      varchar(255),
    denomination varchar(255),
    highway      varchar(255),
    housenumber  varchar(255),
    is_building  boolean,
    is_garage    boolean,
    junction     varchar(255),
    name         varchar(255),
    postcode     varchar(255),
    ref_node     bigint,
    religion     varchar(255),
    sport        varchar(255),
    street       varchar(255),
    surface      varchar(255)
    );

alter table public.ways
    owner to postgres;

create table if not exists public.nodes
(
    id  bigint           not null primary key,
    lat double precision not null,
    lon double precision not null
);

alter table public.nodes
    owner to postgres;

create table public.node_way_relation (
  way_id  bigint not null
      constraint fkffhki95vbwgli1s55log99gfc
          references public.ways,
  node_id bigint not null
      constraint fklcehakh8jv2r2fwfvdde0hxfi
          references public.nodes,
  primary key (way_id, node_id)
);

alter table public.node_way_relation
    owner to postgres;

create table public.streets (
    id          bigint not null primary key,
    housenumber varchar(255),
    is_building boolean,
    street      varchar(255),
    parent      bigint
        constraint fkcmu12wovco4tql8lac1b264vv
            references public.streets
);

alter table public.streets
    owner to postgres;

create table public.street_node_relation (
     street_id bigint not null
         constraint fkspnig28h8d4s771e8od1qr6cg
             references public.streets,
     node_id   bigint not null
         constraint fkhi02pnyurp2hwy2tod7n3uib0
             references public.nodes,
     primary key (street_id, node_id)
);

alter table public.street_node_relation
    owner to postgres;

