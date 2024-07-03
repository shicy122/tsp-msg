DROP TABLE IF EXISTS holiday_config;
DROP TABLE IF EXISTS resource_config;
DROP TABLE IF EXISTS base_task_info;
DROP TABLE IF EXISTS broadcast_task_info;
DROP TABLE IF EXISTS scene_card_task_info;
DROP TABLE IF EXISTS task_mt_relation;
DROP TABLE IF EXISTS task_resource_relation;
DROP TABLE IF EXISTS task_vin_relation;
DROP TABLE IF EXISTS vehicle_base_info;


CREATE TABLE holiday_config (
    id bigint NOT NULL PRIMARY KEY,
    holiday_name varchar(32) NOT NULL,
    holiday_type varchar(10) NOT NULL,
    year integer NOT NULL,
    status varchar(10) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    create_by varchar(32) NULL DEFAULT NULL,
    update_by varchar(32) NULL DEFAULT NULL,
    CONSTRAINT idx_holiday_name_year UNIQUE (year, holiday_name)
);

COMMENT ON COLUMN holiday_config.id IS '主键ID';
COMMENT ON COLUMN holiday_config.holiday_name IS '节假日名称';
COMMENT ON COLUMN holiday_config.holiday_type IS '节假日类型 (BIRTHDAY:生日, LEGAL:法定, CUSTOM:自定义)';
COMMENT ON COLUMN holiday_config.year IS '年份';
COMMENT ON COLUMN holiday_config.status IS '状态 (ENABLE:启用, DISABLE:无效, FORBIDDEN:禁用)';
COMMENT ON COLUMN holiday_config.start_date IS '开始日期';
COMMENT ON COLUMN holiday_config.end_date IS '结束日期';
COMMENT ON COLUMN holiday_config.create_time IS '创建时间';
COMMENT ON COLUMN holiday_config.update_time IS '修改时间';
COMMENT ON COLUMN holiday_config.create_by IS '创建人';
COMMENT ON COLUMN holiday_config.update_by IS '修改人';

CREATE TABLE resource_config (
     id bigint NOT NULL PRIMARY KEY,
     resource_name varchar(32) NULL DEFAULT NULL,
     resource_type varchar(32) NOT NULL,
     resource_size bigint NULL DEFAULT NULL,
     resource_url text NOT NULL,
     source varchar(32) NOT NULL DEFAULT 'SYS' CHECK (source IN ('SYS', 'CUSTOM')),
     status varchar(10) NOT NULL DEFAULT 'ENABLE' CHECK (status IN ('ENABLE', 'FORBIDDEN')),
     create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
     update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
     create_by varchar(32) NULL DEFAULT NULL,
     update_by varchar(32) NULL DEFAULT NULL,
     CONSTRAINT idx_resource_name UNIQUE (resource_name)
);

COMMENT ON COLUMN resource_config.id IS '主键ID';
COMMENT ON COLUMN resource_config.resource_name IS '资源名称';
COMMENT ON COLUMN resource_config.resource_type IS '资源类型 (VEDIO:视频, ARTICLE:图文, PICTURE:图片, WALLPAPER:壁纸)';
COMMENT ON COLUMN resource_config.resource_size IS '资源大小 (单位:KB)';
COMMENT ON COLUMN resource_config.resource_url IS '资源链接';
COMMENT ON COLUMN resource_config.source IS '数据来源 (默认SYS) (可选值: SYS:系统录入, CUSTOM:自定义)';
COMMENT ON COLUMN resource_config.status IS '状态 (ENABLE:启用, FORBIDDEN:禁用)';
COMMENT ON COLUMN resource_config.create_time IS '创建时间';
COMMENT ON COLUMN resource_config.update_time IS '修改时间';
COMMENT ON COLUMN resource_config.create_by IS '创建人';
COMMENT ON COLUMN resource_config.update_by IS '修改人';

CREATE TABLE base_task_info (
    id bigint NOT NULL PRIMARY KEY,
    task_name varchar(32) NOT NULL,
    task_type varchar(16) NOT NULL,
    msg_title varchar(32) NOT NULL,
    msg_content varchar(255) NOT NULL,
    exec_policy varchar(32) NOT NULL,
    status varchar(10) NOT NULL DEFAULT 'CREATED',
    scope varchar(32) NOT NULL,
    publish_time timestamp NULL DEFAULT NULL,
    send_time timestamp NULL DEFAULT NULL,
    cancel_time timestamp NULL DEFAULT NULL,
    finish_time timestamp NULL DEFAULT NULL,
    create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    create_by varchar(32) NULL DEFAULT NULL,
    update_by varchar(32) NULL DEFAULT NULL,
    CONSTRAINT idx_task_name_type UNIQUE (task_name, task_type)
);

COMMENT ON COLUMN base_task_info.id IS '主键ID';
COMMENT ON COLUMN base_task_info.task_name IS '任务名称';
COMMENT ON COLUMN base_task_info.task_type IS '任务类型';
COMMENT ON COLUMN base_task_info.msg_title IS '消息标题';
COMMENT ON COLUMN base_task_info.msg_content IS '消息内容';
COMMENT ON COLUMN base_task_info.exec_policy IS '执行策略 (AVNT_ONLINE:车辆上电, USER_LOGIN:账号登陆)';
COMMENT ON COLUMN base_task_info.status IS '状态 (CREATED:已创建, PUBLISHED:已发布, SENDING:推送中, CANCELED:已撤回, FINISHED:已结束, TERMINATE:终止)';
COMMENT ON COLUMN base_task_info.scope IS '发送范围 (SINGLE:单个VIN, MULTIPLE:批量VIN, COMBINATION:组合条件)';
COMMENT ON COLUMN base_task_info.publish_time IS '发布时间';
COMMENT ON COLUMN base_task_info.send_time IS '推送时间';
COMMENT ON COLUMN base_task_info.cancel_time IS '撤回时间';
COMMENT ON COLUMN base_task_info.finish_time IS '结束时间';
COMMENT ON COLUMN base_task_info.create_time IS '创建时间';
COMMENT ON COLUMN base_task_info.update_time IS '修改时间';
COMMENT ON COLUMN base_task_info.create_by IS '创建人';
COMMENT ON COLUMN base_task_info.update_by IS '修改人';

CREATE TABLE broadcast_task_info (
    id bigint NOT NULL PRIMARY KEY,
    base_task_id bigint NOT NULL,
    exec_rate varchar(10) NOT NULL,
    holiday_config_id bigint NOT NULL,
    create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    create_by varchar(32) NULL DEFAULT NULL,
    update_by varchar(32) NULL DEFAULT NULL
);

COMMENT ON COLUMN broadcast_task_info.id IS '主键ID';
COMMENT ON COLUMN broadcast_task_info.base_task_id IS '任务ID';
COMMENT ON COLUMN broadcast_task_info.exec_rate IS '执行频率 (ONE_TIME:推送一次, EVERY_DAY:每天一次)';
COMMENT ON COLUMN broadcast_task_info.holiday_config_id IS '节假日配置ID';
COMMENT ON COLUMN broadcast_task_info.create_time IS '创建时间';
COMMENT ON COLUMN broadcast_task_info.update_time IS '修改时间';
COMMENT ON COLUMN broadcast_task_info.create_by IS '创建人';
COMMENT ON COLUMN broadcast_task_info.update_by IS '修改人';

CREATE TABLE scene_card_task_info (
    id bigint NOT NULL PRIMARY KEY,
    base_task_id bigint NOT NULL,
    exec_plan varchar(32) NOT NULL,
    plan_time timestamp NULL DEFAULT NULL,
    duration bigint NOT NULL DEFAULT 86400,
    actions json NULL DEFAULT NULL,
    create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    create_by varchar(32) NULL DEFAULT NULL,
    update_by varchar(32) NULL DEFAULT NULL
);

COMMENT ON COLUMN scene_card_task_info.id IS '主键ID';
COMMENT ON COLUMN scene_card_task_info.base_task_id IS '任务ID';
COMMENT ON COLUMN scene_card_task_info.exec_plan IS '执行计划 (RIGHT_NOW:立即, AT_TIME:定时)';
COMMENT ON COLUMN scene_card_task_info.plan_time IS '计划执行时间';
COMMENT ON COLUMN scene_card_task_info.duration IS '保留时长 (默认一天，单位:秒)';
COMMENT ON COLUMN scene_card_task_info.actions IS '触发动作';
COMMENT ON COLUMN scene_card_task_info.create_time IS '创建时间';
COMMENT ON COLUMN scene_card_task_info.update_time IS '修改时间';
COMMENT ON COLUMN scene_card_task_info.create_by IS '创建人';
COMMENT ON COLUMN scene_card_task_info.update_by IS '修改人';

CREATE TABLE task_mt_relation (
    id bigint NOT NULL PRIMARY KEY,
    base_task_id bigint NOT NULL,
    mt_id bigint NOT NULL,
    CONSTRAINT unq_task_mt UNIQUE (base_task_id, mt_id)
);

COMMENT ON COLUMN task_mt_relation.id IS '主键ID';
COMMENT ON COLUMN task_mt_relation.base_task_id IS '任务ID';
COMMENT ON COLUMN task_mt_relation.mt_id IS '车型ID';

CREATE TABLE task_resource_relation (
    id bigint NOT NULL PRIMARY KEY,
    base_task_id bigint NOT NULL,
    resource_id bigint NOT NULL,
    is_illustration smallint NOT NULL DEFAULT 1
);

COMMENT ON COLUMN task_resource_relation.id IS '主键ID';
COMMENT ON COLUMN task_resource_relation.base_task_id IS '任务ID';
COMMENT ON COLUMN task_resource_relation.resource_id IS '资源配置ID';
COMMENT ON COLUMN task_resource_relation.is_illustration IS '是否为配图 (0否, 1是)';

CREATE TABLE task_vin_relation (
    id bigint NOT NULL PRIMARY KEY,
    base_task_id bigint NOT NULL,
    vin varchar(32) NOT NULL,
    CONSTRAINT unq_task_vin UNIQUE (base_task_id, vin)
);

COMMENT ON COLUMN task_vin_relation.id IS '主键id';
COMMENT ON COLUMN task_vin_relation.base_task_id IS '任务ID';
COMMENT ON COLUMN task_vin_relation.vin IS '车辆VIN码';

CREATE TABLE vehicle_base_info (
    id bigint NOT NULL PRIMARY KEY,
    vin varchar(32) NOT NULL,
    serial_id varchar(20) NULL,
    mt_id bigint NULL,
    serial varchar(32) NULL,
    mt varchar(32) NULL,
    car_model_market varchar(32) NULL,
    CONSTRAINT unq_vin UNIQUE (vin)
);

COMMENT ON COLUMN vehicle_base_info.id IS '主键ID';
COMMENT ON COLUMN vehicle_base_info.vin IS 'VIN码';
COMMENT ON COLUMN vehicle_base_info.serial_id IS '车系ID';
COMMENT ON COLUMN vehicle_base_info.mt_id IS '车型ID';
COMMENT ON COLUMN vehicle_base_info.serial IS '车系';
COMMENT ON COLUMN vehicle_base_info.mt IS '车型';
COMMENT ON COLUMN vehicle_base_info.car_model_market IS '产线';
