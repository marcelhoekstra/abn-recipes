create sequence recipe_generator start with 1 increment by 1;
create sequence ingredient_generator start with 1 increment by 1;
create table ingredients (id bigint not null, ingredient varchar(255), quantity varchar(50) not null, version integer, recipe_id bigint, primary key (id));
create table recipes (id bigint not null, instructions clob, name varchar(255), prep_time varchar(255), serves integer not null, vegetarian boolean not null, author varchar(255), version integer, primary key (id));
alter table ingredients add constraint FK7p08vcyn6wf7fd6qp79yy2jrwg foreign key (recipe_id) references recipes;

ALTER TABLE recipes ADD CONSTRAINT NAME_UNIQUE UNIQUE(name);


--insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('the instructions', 'ç', 4, false, 'marcel', 0, 1000);
--insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salt', '1 gram', 0, (NEXTVAL('ingredient_generator')), 1000);
--
--insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('....oven....', 'spagetti', 4, false, 'marcel', 0, 1001);
--insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salt', '1 gram', 0, (NEXTVAL('ingredient_generator')), 1001);
--insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('mushrooms', '200 gram', 0, (NEXTVAL('ingredient_generator')), 1001);
--
--insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('....oven', 'lasange', 4, false, 'marcel', 0, 1002);
--insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salmon', '500 gram', 0, (NEXTVAL('ingredient_generator')), 1002);
--insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('paprika', '1', 0, (NEXTVAL('ingredient_generator')), 1002);