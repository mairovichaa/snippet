Start mysql image:

```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:latest
```

Connect to it:

```
docker exec -it mysql bash 
```

Db code:

```
CREATE DATABASE `library`;

USE `library`;

CREATE TABLE `author` (
  `id` int NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO author (id, first_name, last_name) VALUES (1, 'f_name_1', 'l_name_1');
INSERT INTO author (id, first_name, last_name) VALUES (2, 'f_name_2', 'l_name_2');
INSERT INTO author (id, first_name, last_name) VALUES (3, 'f_name_3', 'l_name_3');
```

Run `generateJooqClasses` gradle task.

Run `com.amairovi.Main`.