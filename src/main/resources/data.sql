-- Crear esquema
CREATE TABLE AppUser (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         age INT,
                         mail VARCHAR(255),
                         password VARCHAR(255)
);

CREATE TABLE Page (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      date_creation TIMESTAMP,
                      title VARCHAR(255) UNIQUE,
                      id_User INT UNIQUE,
                      FOREIGN KEY (id_User) REFERENCES AppUser(id)
);

CREATE TABLE Post (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      date_creation TIMESTAMP,
                      content VARCHAR(255),
                      img VARCHAR(255),
                      id_page INT,
                      FOREIGN KEY (id_page) REFERENCES Page(id)
);

-- Insertar datos en la tabla AppUser
INSERT INTO AppUser (name, age, mail, passwor) VALUES ('User1', 25, 'user1@example.com', 'password1');
INSERT INTO AppUser (name, age, mail, passwor) VALUES ('User2', 30, 'user2@example.com', 'password2');
INSERT INTO AppUser (name, age, mail, passwor) VALUES ('User3', 28, 'user3@example.com', 'password3');
INSERT INTO AppUser (name, age, mail, passwor) VALUES ('User4', 29, 'user4@example.com', 'password4');

-- Insertar datos en la tabla Page para cada usuario
INSERT INTO Page (date_creation, title, id_User) VALUES (CURRENT_TIMESTAMP, 'User1 Page', 1);
INSERT INTO Page (date_creation, title, id_User) VALUES (CURRENT_TIMESTAMP, 'User2 Page', 2);
INSERT INTO Page (date_creation, title, id_User) VALUES (CURRENT_TIMESTAMP, 'User3 Page', 3);

-- Insertar datos en la tabla Post para cada página
-- Posts para la página de User1
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 1 for User1', 'img1.jpg', 1);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 2 for User1', 'img2.jpg', 1);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 3 for User1', 'img3.jpg', 1);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 4 for User1', 'img4.jpg', 1);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 5 for User1', 'img5.jpg', 1);

-- Posts para la página de User2
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 1 for User2', 'img1.jpg', 2);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 2 for User2', 'img2.jpg', 2);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 3 for User2', 'img3.jpg', 2);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 4 for User2', 'img4.jpg', 2);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 5 for User2', 'img5.jpg', 2);

-- Posts para la página de User3
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 1 for User3', 'img1.jpg', 3);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 2 for User3', 'img2.jpg', 3);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 3 for User3', 'img3.jpg', 3);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 4 for User3', 'img4.jpg', 3);
INSERT INTO Post (date_creation, content, img, id_page) VALUES (CURRENT_TIMESTAMP, 'Content 5 for User3', 'img5.jpg', 3);

