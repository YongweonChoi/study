CREATE DATABASE IF NOT EXISTS ioc_di_study
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE ioc_di_study;

DROP TABLE IF EXISTS tb_category;

CREATE TABLE tb_category
(
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    use_yn CHAR(1) NOT NULL DEFAULT 'Y',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at DATETIME,
    PRIMARY KEY (category_id)
);

INSERT INTO tb_category
(
    category_name,
    description,
    use_yn
)
VALUES
('SPRING', 'Spring Framework 학습', 'Y'),
('REACT', 'React 학습', 'Y'),
('DATABASE', 'Database 학습', 'Y');

COMMIT;
select category_id
			  ,category_name
			  ,description
			  ,use_yn
			  ,created_at
		  from tb_category
		  order by category_id desc