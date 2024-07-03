package com.hycan.idn.tsp.message.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description:  自定义JsonTypeHandler处理PostgreSQL的JSON数据类型
 */
@Slf4j
@MappedTypes(Object.class)
public class JSONTypeHandlerPg extends BaseTypeHandler<Object> {
    //引入PGSQL提供的工具类PGobject
    private static final PGobject JSON_OBJECT = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        JSON_OBJECT.setType("json");
        try {
            //java对象转化成json字符串
            JSON_OBJECT.setValue(new ObjectMapper().writeValueAsString(parameter));
        } catch (IOException e) {
            log.error("JSONTypeHandlerPg.setNonNullParameter出错！", e);
        }
        ps.setObject(i, JSON_OBJECT);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
