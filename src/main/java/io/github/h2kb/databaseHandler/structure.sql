CREATE TABLE IF NOT EXISTS `parser`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8

CREATE TABLE IF NOT EXISTS `parser`.`team` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8

CREATE TABLE IF NOT EXISTS `parser`.`player` (
  `id` INT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `age` INT(3) NOT NULL,
  `role` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `role`
    FOREIGN KEY (`role`)
    REFERENCES `parser`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8

CREATE TABLE IF NOT EXISTS `parser`.`player2team` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `player` INT NOT NULL,
  `team` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `player`
    FOREIGN KEY (`player`)
    REFERENCES `parser`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `player2team`
    FOREIGN KEY (`team`)
    REFERENCES `parser`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8

CREATE TABLE IF NOT EXISTS `parser`.`result` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `team` INT NOT NULL,
  `place` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `result_team`
    FOREIGN KEY (`team`)
    REFERENCES `parser`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8


delete from player2team;
delete from player;
delete from result;
delete from team;
delete from role;