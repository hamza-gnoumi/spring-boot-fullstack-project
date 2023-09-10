
CREATE TABLE customer(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL ,
    password Text NOT NULL,
    gender VARCHAR(50) NOT NULL,
    age INT NOT NULL
);

