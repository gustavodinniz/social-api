CREATE TABLE tb_user (
	id bigserial NOT NULL PRIMARY KEY,
	name varchar(100) NOT NULL,
	age integer NOT null
)

CREATE TABLE tb_posts (
	id bigserial NOT NULL PRIMARY KEY,
	post_text varchar(150)  NOT NULL,
	dateTime timestamp,
	user_id bigint not null references tb_user(id)
)

create table tb_followers (
	id bigserial not null primary key,
	user_id bigint not null references tb_user(id),
	follower_id bigint not null references tb_user(id)
)