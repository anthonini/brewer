INSERT INTO permission (id_permission, name) VALUES (1, 'ROLE_REGISTER_CITY');
INSERT INTO permission (id_permission, name) VALUES (2, 'ROLE_REGISTER_USER');

INSERT INTO user_group_permission (id_user_group, id_permission) VALUES (1,1);
INSERT INTO user_group_permission (id_user_group, id_permission) VALUES (1,2);

INSERT INTO user_group_user (id_user_group, id_user) 
VALUES (1, (SELECT id_user FROM user WHERE email = 'admin@brewer.com'));