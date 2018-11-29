package cn.tekin.jpa.dialect;

import org.hibernate.dialect.MySQLDialect;

/**
 * 通过重写 getTableTypeString 自定义Mysql数据库的表生成类型
 * JPA默认的数据库编码为 MyISAM引擎   latin1编码
 */
public class Mysql5InnodbUtf8 extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    }
}
