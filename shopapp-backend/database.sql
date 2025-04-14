CREATE database shopapp;
Use shopapp;

--users
CREATE TABLE users(
    id INT PRIMARY KEY auto_increment,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '', -- after encoding
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
 );

--add role_id to users
ALTER TABLE users ADD COLUMN role_id INT;

 --roles
 CREATE TABLE roles(
    id INT PRIMARY KEY,,
    name VARCHAR(20) NOT NULL,
 );

--add foreign key fo role_id in user table
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);


 CREATE TABLE tokens(
    id INT PRIMARY KEY auto_increment,
    token VARCHAR(100) UNIQUE NOT NULL,
    token_type VARCHAR(100) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT, 
    FOREIGN KEY (user_id) REFERENCES users(id)
 );

 --support logn in by facebook and google
 CREATE TABLE social_accounts(
    id INT PRIMARY KEY auto_increment,
    provider VARCHAR(20) NOT NULL COMMENT 'Name of social network',
    privider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'email of the account',
    name VARCHAR(100) NOT NULL COMMENT 'name of user',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
 );

--category
CREATE TABLE categories(
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Name of category'
);

--products
CREATE TABLE products(
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(400) NOT NULL DEFAULT '' COMMENT 'Name of product',
    price FLOAT NOT NULL CHECK(price >= 0),
    url VARCHAR(400) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_product_image_product_id
            FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
   image_url VARCHAR(300)
)

-- orders
CREATE TABLE orders(
   id INT PRIMARY KEY auto_increment,
   user_id INT,
   fullname varchar(100) NOT NULL DEFAULT '',
   email VARCHAR(100) NOT NULL DEFAULT '',
   phone_number VARCHAR(10) NOT NULL,
   address VARCHAR(200) NOT NULL,
   note VARCHAR(100) DEFAULT '',
   order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
   status VARCHAR(20) DEFAULT '',
   total_money float CHECK(total_money >= 0),
   FOREIGN KEY (user_id) REFERENCES users(id)
);


ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_date VARCHAR(100);
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100);
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN active TINYINT(1);

--Status of the order only get a specific value
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending', 'processing', 'shipping', 'delivered', 'canceled')
COMMENT 'Status of the order';

CREATE TABLE order_details(
   id INT PRIMARY KEY auto_increment,
   order_id INT,
   product_id INT,
   price FLOAT NOT NULL CHECK(price >= 0),
   number_of_products INT NOT NULL CHECK(number_of_products > 0),
   total_money FLOAT CHECK(total_money >= 0),
   color VARCHAR(20) DEFAULT '',
   FOREIGN KEY (order_id) REFERENCES orders(id),
   FOREIGN KEY (product_id) REFERENCES products(id)
);