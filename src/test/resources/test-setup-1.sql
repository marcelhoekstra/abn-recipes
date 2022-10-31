insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('the instructions', 'hamburger', 4, false, 'marcel', 0, 1000);
insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salt', '1 gram', 0, (NEXTVAL('ingredient_generator')), 1000);

insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('....oven....', 'spagetti', 4, false, 'marcel', 0, 1001);
insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salt', '1 gram', 0, (NEXTVAL('ingredient_generator')), 1001);
insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('mushrooms', '200 gram', 0, (NEXTVAL('ingredient_generator')), 1001);

insert into recipes (instructions, name, serves, vegetarian, author, version, id) values ('....oven', 'lasange', 4, false, 'marcel', 0, 1002);
insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('salmon', '500 gram', 0, (NEXTVAL('ingredient_generator')), 1002);
insert into ingredients (ingredient, quantity, version, id, recipe_id) values ('paprika', '1', 0, (NEXTVAL('ingredient_generator')), 1002);
