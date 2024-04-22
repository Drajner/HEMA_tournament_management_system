CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    isAdmin BOOLEAN
);

CREATE TABLE Sessions (
    session_id VARCHAR(256) PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);