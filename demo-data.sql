--
-- Entry Point Table
--
DROP TABLE IF EXISTS `entry_point`;
CREATE TABLE `xyz_parking`.`entry_point`
(
    `id`          INT                NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45) UNIQUE NOT NULL,
    `start_point` INT,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = UTF8MB4;

--
-- Parking Slot Table
--
DROP TABLE IF EXISTS `parking_slot`;
CREATE TABLE `xyz_parking`.`parking_slot`
(
    `id`              INT NOT NULL AUTO_INCREMENT,
    `occupancy`       TINYINT DEFAULT 0,
    `distance_number` INT     DEFAULT 1,
    `slot_size`       TINYINT default 1,
    `vehicle_id`      INT UNIQUE,
    PRIMARY KEY (`id`),
#  FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle`(`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = UTF8MB4;

--
-- Vehicle Table
--
DROP TABLE IF EXISTS `vehicle`;
CREATE TABLE `xyz_parking`.`vehicle`
(
    `id`                INT NOT NULL AUTO_INCREMENT,
    `vehicle_code`      VARCHAR(45) default NULL,
    `vehicle_size`      TINYINT     default 1,
    `park_in`           TIMESTAMP,
    `park_out`          TIMESTAMP,
    `total_paid_amount` DECIMAL(10, 2),
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = UTF8MB4;

--
-- Entry Points default data
--
INSERT INTO `xyz_parking`.`entry_point`
    (`name`, `start_point`)
VALUES ('a', 2),
       ('b', 10),
       ('c', 17);

--
-- Parking Slots default data
--
INSERT INTO `xyz_parking`.`parking_slot`
    (`occupancy`, `distance_number`, `slot_size`)
VALUES (0, 1, 1),
       (0, 2, 2),
       (0, 3, 1),
       (0, 3, 3),
       (0, 4, 3),
       (0, 5, 2),
       (0, 7, 3),
       (0, 8, 2),
       (0, 9, 1),
       (0, 10, 2),
       (0, 11, 2),
       (0, 12, 3),
       (0, 14, 3),
       (0, 15, 1),
       (0, 16, 3),
       (0, 17, 3),
       (0, 19, 2),
       (0, 20, 1);
