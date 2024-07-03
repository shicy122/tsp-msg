ALTER TABLE task_resource_relation DROP COLUMN is_illustration;
ALTER TABLE task_resource_relation ADD COLUMN is_illustration BOOLEAN DEFAULT TRUE;
COMMENT ON COLUMN task_resource_relation.is_illustration IS '是否为配图';