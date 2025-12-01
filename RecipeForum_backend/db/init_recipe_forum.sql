-- SQL initialization script for Recipe Forum (recreated)
-- Creates tables and inserts dummy data for development/testing.
-- WARNING: Drops tables if they exist (development helper). Adjust before using in production.

CREATE DATABASE IF NOT EXISTS `afp_team` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `afp_team`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS recipe_user;

SET FOREIGN_KEY_CHECKS = 1;

-- Users table
CREATE TABLE recipe_user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  user_name VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_encoded VARCHAR(255) NOT NULL,
  profile_image_ref VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Categories
CREATE TABLE category (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Materials (master data)
CREATE TABLE material (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  calories DOUBLE,
  protein DOUBLE,
  carbohydrate DOUBLE,
  fat DOUBLE,
  unit_of_measure VARCHAR(64)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Recipes
CREATE TABLE IF NOT EXISTS recipe (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  image_ref VARCHAR(255),
  prep_time INT,
  cook_time INT,
  `portion` INT,
  description TEXT,
  preparation_steps TEXT,
  user_id INT NOT NULL,
  category_id INT,
  CONSTRAINT fk_recipe_user FOREIGN KEY (user_id) REFERENCES recipe_user(id) ON DELETE CASCADE,
  CONSTRAINT fk_recipe_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Ingredients (join table)
CREATE TABLE ingredient (
  id INT AUTO_INCREMENT PRIMARY KEY,
  recipe_id INT NOT NULL,
  material_id INT NOT NULL,
  quantity VARCHAR(100),
  CONSTRAINT fk_ing_recipe FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
  CONSTRAINT fk_ing_material FOREIGN KEY (material_id) REFERENCES material(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------
-- Dummy data
-- --------------------------------------------------

-- Users
INSERT INTO recipe_user (id, name, user_name, email, password_encoded, profile_image_ref) VALUES
(1, 'Alice Kovacs', 'alice', 'alice@example.com', 'pw_alice_encoded', NULL),
(2, 'Bela Nagy', 'bela', 'bela@example.com', 'pw_bela_encoded', NULL),
(3, 'Csilla Toth', 'csilla', 'csilla@example.com', 'pw_csilla_encoded', NULL),
(4, 'Dora Horvath', 'dora', 'dora@example.com', 'pw_dora_encoded', NULL),
(5, 'Erik Molnar', 'erik', 'erik@example.com', 'pw_erik_encoded', NULL),
(6, 'Fanni Szabo', 'fanni', 'fanni@example.com', 'pw_fanni_encoded', NULL),
(7, 'Gabor Farkas', 'gabor', 'gabor@example.com', 'pw_gabor_encoded', NULL),
(8, 'Helga Varga', 'helga', 'helga@example.com', 'pw_helga_encoded', NULL),
(9, 'Istvan Kiss', 'istvan', 'istvan@example.com', 'pw_istvan_encoded', NULL),
(10, 'Judit Major', 'judit', 'judit@example.com', 'pw_judit_encoded', NULL),
(11, 'Kata Balogh', 'kata', 'kata@example.com', 'pw_kata_encoded', NULL),
(12, 'Laci Fodor', 'laci', 'laci@example.com', 'pw_laci_encoded', NULL);

-- Categories (common cuisines/types)
INSERT INTO category (id, name) VALUES
(1, 'Breakfast'),
(2, 'Lunch'),
(3, 'Dinner'),
(4, 'Dessert'),
(5, 'Soup'),
(6, 'Salad'),
(7, 'Snack'),
(8, 'Beverage'),
(9, 'Appetizer'),
(10, 'Sauce'),
(11, 'Baking'),
(12, 'Vegan'),
(13, 'Gluten-Free'),
(14, 'Paleo');

-- Materials: a wide variety
INSERT INTO material (id, name, calories, protein, carbohydrate, fat, unit_of_measure) VALUES
(1, 'Egg', 155, 13, 1.1, 11, 'pcs'),
(2, 'Flour', 364, 10, 76, 1, 'g'),
(3, 'Sugar', 387, 0, 100, 0, 'g'),
(4, 'Butter', 717, 0.9, 0.1, 81, 'g'),
(5, 'Milk', 42, 3.4, 5, 1, 'ml'),
(6, 'Salt', 0, 0, 0, 0, 'g'),
(7, 'Black Pepper', 255, 10, 64, 3.3, 'g'),
(8, 'Chicken Breast', 165, 31, 0, 3.6, 'g'),
(9, 'Olive Oil', 884, 0, 0, 100, 'ml'),
(10, 'Tomato', 18, 0.9, 3.9, 0.2, 'pcs'),
(11, 'Onion', 40, 1.1, 9.3, 0.1, 'pcs'),
(12, 'Garlic', 149, 6.4, 33, 0.5, 'clove'),
(13, 'Potato', 77, 2, 17, 0.1, 'pcs'),
(14, 'Carrot', 41, 0.9, 10, 0.2, 'pcs'),
(15, 'Beef Mince', 250, 26, 0, 15, 'g'),
(16, 'Pasta', 131, 5, 25, 1.1, 'g'),
(17, 'Cheddar Cheese', 403, 25, 1.3, 33, 'g'),
(18, 'Baking Powder', 53, 0, 27, 0, 'g'),
(19, 'Cocoa Powder', 228, 19.6, 57.9, 13.7, 'g'),
(20, 'Yeast', 325, 40, 41, 7, 'g'),
(21, 'Honey', 304, 0.3, 82.4, 0, 'g'),
(22, 'Lemon', 29, 1.1, 9.3, 0.3, 'pcs'),
(23, 'Brown Sugar', 380, 0, 98, 0, 'g'),
(24, 'Yogurt', 59, 10, 3.6, 0.4, 'ml'),
(25, 'Cucumber', 16, 0.7, 3.6, 0.1, 'pcs'),
(26, 'Spinach', 23, 2.9, 3.6, 0.4, 'g'),
(27, 'Salmon', 208, 20, 0, 13, 'g'),
(28, 'Lime', 30, 0.7, 10, 0.2, 'pcs'),
(29, 'Basil', 23, 3.2, 2.7, 0.6, 'g'),
(30, 'Parsley', 36, 3, 6, 0.8, 'g'),
(31, 'Mushroom', 22, 3.1, 3.3, 0.3, 'pcs'),
(32, 'Bell Pepper', 31, 1, 6, 0.3, 'pcs'),
(33, 'Corn', 86, 3.4, 19, 1.2, 'g'),
(34, 'Black Beans', 341, 21, 62, 1.4, 'g'),
(35, 'Tofu', 76, 8, 1.9, 4.8, 'g'),
(36, 'Soy Sauce', 53, 8, 4.9, 0.6, 'ml'),
(37, 'Vinegar', 18, 0, 0, 0, 'ml'),
(38, 'Brown Rice', 123, 2.6, 25.6, 1, 'g'),
(39, 'Oats', 389, 17, 66, 7, 'g'),
(40, 'Walnuts', 654, 15.2, 13.7, 65.2, 'g');

-- Recipes insert (use `portion` to match entity)
INSERT INTO recipe (id, name, image_ref, prep_time, cook_time, `portion`, description, preparation_steps, user_id, category_id) VALUES
(1, 'Classic Pancakes', NULL, 10, 10, 4, 'Fluffy pancakes for breakfast.', 'Mix flour, milk, eggs, sugar, baking powder. Fry on medium heat.', 1, 1),
(2, 'Tomato Pasta', NULL, 15, 20, 2, 'Simple tomato pasta.', 'Cook pasta. Make sauce with tomatoes, garlic, onion, olive oil.', 2, 3),
(3, 'Chicken Salad', NULL, 20, 0, 2, 'Light chicken salad.', 'Grill chicken. Mix with lettuce, cucumber, tomato and dressing.', 3, 6),
(4, 'Chocolate Cake', NULL, 20, 30, 8, 'Rich chocolate cake.', 'Mix flour, cocoa, sugar, eggs, butter and bake.', 4, 11),
(5, 'Vegetable Soup', NULL, 15, 40, 6, 'Hearty vegetable soup.', 'Saute onion, add vegetables and broth, simmer.', 5, 5),
(6, 'Guacamole-ish', NULL, 10, 0, 4, 'Simple avocado-like dip.', 'Mash lime, add onion, salt and pepper.', 6, 7),
(7, 'Beef Burger', NULL, 15, 15, 2, 'Juicy beef burger.', 'Form patties, grill, assemble with bun and toppings.', 7, 3),
(8, 'Mushroom Risotto', NULL, 20, 25, 4, 'Creamy mushroom risotto.', 'Saute mushrooms, add rice and stock, stir until creamy.', 8, 3),
(9, 'Lemonade', NULL, 10, 0, 6, 'Refreshing lemonade.', 'Mix lemon juice, water and sugar. Serve cold.', 9, 8),
(10, 'Stir-fry Tofu', NULL, 15, 10, 2, 'Quick tofu stir-fry.', 'Stir-fry tofu with vegetables and soy sauce.', 10, 12),
(11, 'Cheese Omelette', NULL, 5, 5, 1, 'Quick cheesy omelette.', 'Beat eggs, add cheese, fry.', 1, 1),
(12, 'Pesto Pasta', NULL, 15, 12, 2, 'Pasta with basil pesto.', 'Blend basil, olive oil, garlic, cheese; mix with pasta.', 2, 3),
(13, 'Salmon with Rice', NULL, 20, 20, 2, 'Pan-seared salmon with rice.', 'Season and sear salmon, serve with rice and veggies.', 3, 3),
(14, 'Chocolate Brownies', NULL, 15, 25, 8, 'Fudgy brownies.', 'Mix chocolate, butter, sugar, eggs and flour, bake.', 4, 11),
(15, 'Greek Salad', NULL, 10, 0, 2, 'Classic Greek salad.', 'Mix tomato, cucumber, onion, olive oil and herbs.', 5, 6),
(16, 'Hummus-ish', NULL, 10, 0, 6, 'Creamy bean dip.', 'Blend beans, olive oil, lemon and garlic.', 6, 10),
(17, 'Veggie Stir Fry', NULL, 15, 10, 3, 'Stir-fried mixed vegetables.', 'Stir fry assorted vegetables with sauce.', 7, 12),
(18, 'Banana Smoothie', NULL, 5, 0, 2, 'Healthy banana smoothie.', 'Blend bananas, milk or yogurt, and honey.', 8, 8),
(19, 'Mushroom Soup', NULL, 10, 25, 4, 'Creamy mushroom soup.', 'Saute mushrooms and onion, add stock and cream, blend.', 9, 5),
(20, 'Walnut Oat Cookies', NULL, 15, 12, 12, 'Crunchy oat cookies with walnuts.', 'Mix oats, flour, sugar, butter, walnuts; bake.', 10, 11);

-- Ingredients
INSERT INTO ingredient (recipe_id, material_id, quantity) VALUES
(1, 2, '200 g'),
(1, 5, '300 ml'),
(1, 1, '2 pcs'),
(1, 3, '30 g'),
(1, 18, '1 tsp'),

(2, 16, '200 g'),
(2, 10, '3 pcs'),
(2, 12, '2 cloves'),
(2, 11, '1 pcs'),
(2, 9, '2 tbsp'),

(3, 8, '200 g'),
(3, 25, '1 pcs'),
(3, 10, '1 pcs'),
(3, 11, '1 pcs'),

(4, 2, '250 g'),
(4, 3, '200 g'),
(4, 4, '100 g'),
(4, 1, '3 pcs'),
(4, 19, '50 g'),

(5, 13, '3 pcs'),
(5, 14, '2 pcs'),
(5, 11, '1 pcs'),
(5, 10, '2 pcs'),

(6, 28, '1 pcs'),
(6, 11, '1 pcs'),
(6, 6, '1 tsp'),

(7, 15, '200 g'),
(7, 6, '1 tsp'),
(7, 9, '1 tbsp'),

(8, 31, '200 g'),
(8, 38, '200 g'),
(8, 11, '1 pcs'),

(9, 22, '3 pcs'),
(9, 3, '50 g'),
(9, 5, '500 ml'),

(10, 35, '200 g'),
(10, 32, '1 pcs'),
(10, 36, '2 tbsp'),

(11, 1, '2 pcs'),
(11, 17, '50 g'),

(12, 16, '200 g'),
(12, 29, '10 g'),
(12, 9, '3 tbsp'),

(13, 27, '200 g'),
(13, 38, '150 g'),
(13, 25, '1 pcs'),

(14, 19, '80 g'),
(14, 4, '80 g'),
(14, 3, '150 g'),
(14, 1, '2 pcs'),

(15, 10, '2 pcs'),
(15, 25, '1 pcs'),
(15, 17, '50 g'),

(16, 34, '200 g'),
(16, 9, '2 tbsp'),
(16, 22, '1 pcs'),

(17, 32, '1 pcs'),
(17, 31, '100 g'),
(17, 33, '100 g'),

(18, 5, '200 ml'),
(18, 21, '1 tbsp'),
(18, 1, '1 pcs'),

(19, 31, '200 g'),
(19, 11, '1 pcs'),
(19, 5, '100 ml'),

(20, 39, '200 g'),
(20, 40, '100 g'),
(20, 3, '80 g'),
(20, 4, '100 g');

-- Done
SELECT 'INIT COMPLETE' AS status;
