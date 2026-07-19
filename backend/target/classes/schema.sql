-- XX平台数据库初始化脚本
-- 数据库类型：SQLite

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 应用分类表
CREATE TABLE IF NOT EXISTS app_category (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(50) NOT NULL,
  sort_order INTEGER DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Web应用表
CREATE TABLE IF NOT EXISTS web_app (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  category_id INTEGER,
  cover_image VARCHAR(500),
  version VARCHAR(20),
  detail TEXT,
  url VARCHAR(500) NOT NULL,
  click_count INTEGER DEFAULT 0,
  sort_order INTEGER DEFAULT 0,
  status INTEGER DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 宣贯数据表
CREATE TABLE IF NOT EXISTS showcase_item (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  category VARCHAR(50) NOT NULL,
  title VARCHAR(200) NOT NULL,
  summary VARCHAR(500),
  content TEXT,
  sort_order INTEGER DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 平台配置表
CREATE TABLE IF NOT EXISTS platform_config (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  config_key VARCHAR(100) NOT NULL UNIQUE,
  config_value TEXT,
  description VARCHAR(200)
);

-- 初始管理员账户 (密码: admin123)
INSERT OR IGNORE INTO sys_user (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');

-- 初始平台配置
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('platform_name', 'XX平台', '平台名称');
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('company_name', 'XX公司', '公司名称');
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('logo_path', '', 'Logo路径');
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('bg_image', '', '底图路径');
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('platform_version', 'V1.0.0', '平台版本号');
INSERT OR IGNORE INTO platform_config (config_key, config_value, description) VALUES ('platform_intro', '基于模型驱动架构的标准赋能平台', '平台简介');

-- 示例应用分类
INSERT OR IGNORE INTO app_category (name, sort_order) VALUES ('业务管理', 1);
INSERT OR IGNORE INTO app_category (name, sort_order) VALUES ('数据分析', 2);
INSERT OR IGNORE INTO app_category (name, sort_order) VALUES ('协同办公', 3);
INSERT OR IGNORE INTO app_category (name, sort_order) VALUES ('智能决策', 4);

-- ==================== 新版实体表（交付应用体系） ====================

-- 应用类型表（枚举量）
CREATE TABLE IF NOT EXISTS app_type (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200),
  sort_order INTEGER DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 流程模板表（枚举量）
CREATE TABLE IF NOT EXISTS process_template (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(200),
  sort_order INTEGER DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 交付应用表（实体量）
CREATE TABLE IF NOT EXISTS delivery_app (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  app_type_id INTEGER,
  url VARCHAR(500),
  description VARCHAR(500),
  cover_image VARCHAR(500),
  detail TEXT,
  version VARCHAR(20),
  click_count INTEGER DEFAULT 0,
  sort_order INTEGER DEFAULT 0,
  status INTEGER DEFAULT 1,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 业务流程表（实体量）
CREATE TABLE IF NOT EXISTS biz_process (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  author VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 数据实体模型表（实体量）
CREATE TABLE IF NOT EXISTS data_model (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  author VARCHAR(50),
  version VARCHAR(20),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 算法能力模型表（实体量）
CREATE TABLE IF NOT EXISTS algo_model (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  author VARCHAR(50),
  version VARCHAR(20),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 应用-流程关联表（中间表）
CREATE TABLE IF NOT EXISTS app_process_rel (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  app_id INTEGER NOT NULL,
  process_id INTEGER NOT NULL
);

-- 流程-模板关联表（中间表）
CREATE TABLE IF NOT EXISTS process_template_rel (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  process_id INTEGER NOT NULL,
  template_id INTEGER NOT NULL
);

-- 示例应用类型
INSERT OR IGNORE INTO app_type (name, description, sort_order) VALUES ('业务管理类', 'ERP、CRM等企业管理软件', 1);
INSERT OR IGNORE INTO app_type (name, description, sort_order) VALUES ('数据分析类', '数据可视化、BI分析工具', 2);
INSERT OR IGNORE INTO app_type (name, description, sort_order) VALUES ('协同办公类', 'OA、项目管理等协作工具', 3);
INSERT OR IGNORE INTO app_type (name, description, sort_order) VALUES ('智能决策类', 'AI辅助决策、预测分析系统', 4);

-- 示例流程模板
INSERT OR IGNORE INTO process_template (name, description, sort_order) VALUES ('标准审批流程', '通用的多级审批流程模板', 1);
INSERT OR IGNORE INTO process_template (name, description, sort_order) VALUES ('数据采集流程', '标准化的数据采集与校验流程', 2);
INSERT OR IGNORE INTO process_template (name, description, sort_order) VALUES ('发布上线流程', '应用从开发到上线的标准流程', 3);

-- 示例宣贯数据
INSERT OR IGNORE INTO showcase_item (category, title, summary, content, sort_order) VALUES ('USER_ECOLOGY', '用户生态概览', '覆盖10+行业领域，服务500+企业客户', '详细用户生态数据：覆盖10+行业领域，服务500+企业客户，用户遍布全国各省市。', 1);
INSERT OR IGNORE INTO showcase_item (category, title, summary, content, sort_order) VALUES ('PRODUCT_SYSTEM', '产品体系', '形成3大产品线，20+核心产品', '产品体系详情：涵盖业务管理、数据分析、协同办公三大产品线，共计20+核心产品。', 1);
INSERT OR IGNORE INTO showcase_item (category, title, summary, content, sort_order) VALUES ('MODEL_SYSTEM', '模型体系', '自研算法模型50+项', '模型体系详情：自研核心算法模型50+项，涵盖预测分析、风险评估、智能推荐等领域。', 1);
INSERT OR IGNORE INTO showcase_item (category, title, summary, content, sort_order) VALUES ('DATA_SYSTEM', '数据体系', '数据总量达TB级别', '数据体系详情：累计处理数据量达TB级别，数据源覆盖20+业务系统，日处理数据千万级。', 1);
INSERT OR IGNORE INTO showcase_item (category, title, summary, content, sort_order) VALUES ('IP', '知识产权', '获得软件著作权30+项，专利10+项', '知识产权详情：已获得软件著作权30+项，发明专利10+项，持续技术创新。', 1);
