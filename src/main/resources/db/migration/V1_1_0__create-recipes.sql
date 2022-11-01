create sequence recipe_generator start with 1 increment by 1;
create sequence ingredient_generator start with 1 increment by 1;
create table ingredients (id bigint not null, ingredient varchar(255), quantity varchar(50) not null, version integer, recipe_id bigint, primary key (id));
create table recipes (id bigint not null, instructions clob, name varchar(255), prep_time varchar(255), serves integer not null, vegetarian boolean not null, author varchar(255), version integer, primary key (id));
alter table ingredients add constraint FK7p08vcyn6wf7fd6qp79yy2jrwg foreign key (recipe_id) references recipes;

ALTER TABLE recipes ADD CONSTRAINT NAME_UNIQUE UNIQUE(name);

