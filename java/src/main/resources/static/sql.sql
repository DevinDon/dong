DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS prisoner;
DROP TABLE IF EXISTS prisoner_info;
DROP TABLE IF EXISTS jail;
DROP TABLE IF EXISTS jail_cell;

CREATE TABLE user(
  id INT PRIMARY KEY AUTO_INCREMENT
  COMMENT '用户编号',
  name VARCHAR(255) NOT NULL
  COMMENT '用户名称',
  username VARCHAR(255) NOT NULL
  COMMENT '用户登录账户',
  password VARCHAR(255) NOT NULL
  COMMENT '用户密码',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体更新时间'
)
  COMMENT '监狱管理员表';

CREATE TABLE prisoner(
  id INT PRIMARY KEY AUTO_INCREMENT
  COMMENT '犯人编号',
  info_id INT NOT NULL DEFAULT -1
  COMMENT '犯人信息编号 -1 代表暂无',
  violation VARCHAR(255) NOT NULL
  COMMENT '违法行为',
  jail_cell_id INT NOT NULL DEFAULT -1
  COMMENT '监狱牢房编号 -1 代表暂无',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体更新时间'
)
  COMMENT '犯人表';

CREATE TABLE prisoner_info(
  id INT PRIMARY KEY AUTO_INCREMENT
  COMMENT '犯人编号',
  name VARCHAR(255) NOT NULL
  COMMENT '犯人名称',
  id_card VARCHAR(255) NOT NULL
  COMMENT '犯人身份证',
  birth_time TIMESTAMP NOT NULL
  COMMENT '出生时间',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体更新时间'
)
  COMMENT '犯人信息表';

CREATE TABLE jail(
  id INT PRIMARY KEY AUTO_INCREMENT
  COMMENT '监狱',
  name VARCHAR(255) NOT NULL
  COMMENT '监狱名称',
  address VARCHAR(255) NOT NULL
  COMMENT '所在地址',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体更新时间'
)
  COMMENT '监狱表';

CREATE TABLE jail_cell(
  id INT PRIMARY KEY AUTO_INCREMENT
  COMMENT '牢房编号',
  jail_id INT NOT NULL
  COMMENT '监狱编号',
  size INT NOT NULL DEFAULT 8
  COMMENT '可容纳人数',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp()
  COMMENT '实体更新时间'
)
  COMMENT '监狱牢房表';

INSERT INTO user (name, username, password) VALUES ('监狱管理员1', 'abc123', '123456');
INSERT INTO user (name, username, password) VALUES ('监狱管理员2', 'aaaaaa', '123456');
INSERT INTO user (name, username, password) VALUES ('监狱管理员3', 'bbbbbb', '123456');

INSERT INTO jail (name, address) VALUES ('大连南关岭监狱', '大连市甘井子区姚西街192号');

INSERT INTO jail_cell (jail_id) VALUES (1);
INSERT INTO jail_cell (jail_id) VALUES (1);
INSERT INTO jail_cell (jail_id) VALUES (1);
INSERT INTO jail_cell (jail_id) VALUES (1);
INSERT INTO jail_cell (jail_id, size) VALUES (1, 4);
INSERT INTO jail_cell (jail_id, size) VALUES (1, 6);

INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('大明', '51052219810813859X', '1981-08-13');
INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('赵农仲', '511823198401105096', '1984-01-10');
INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('洪枫瑾', '511823199401106013', '1994-08-13');
INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('彭凌可', '511823198411106339', '1984-11-10');
INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('何政航', '511823198801108430', '1988-01-10');
INSERT INTO prisoner_info (name, id_card, birth_time) VALUES ('余爽顺', '511823198601107518', '1986-08-13');

INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (1, '杀人', 1);
INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (2, '强奸', 1);
INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (3, '入室盗窃', 6);
INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (4, '传销', 6);
INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (5, '偷税漏税', 6);
INSERT INTO prisoner (info_id, violation, jail_cell_id) VALUES (6, '故意伤害', 1);