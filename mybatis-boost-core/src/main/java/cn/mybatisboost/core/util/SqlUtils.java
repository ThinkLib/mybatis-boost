package cn.mybatisboost.core.util;

import org.apache.ibatis.session.RowBounds;

import java.util.stream.Stream;

public abstract class SqlUtils {

    public static StringBuilder appendWhere(StringBuilder sqlBuilder, Stream<String> stream) {
        sqlBuilder.append(" WHERE ");
        stream.forEach(c -> sqlBuilder.append(c).append(" = ? AND "));
        sqlBuilder.setLength(sqlBuilder.length() - 5);
        return sqlBuilder;
    }

    public static String appendLimit(String sql, RowBounds rowBounds) {
        if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET || rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
            sql += " LIMIT " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
        }
        return sql;
    }

    public static String appendLimitOffset(String sql, RowBounds rowBounds) {
        if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET || rowBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
            sql += " LIMIT " + rowBounds.getLimit() + " OFFSET " + rowBounds.getOffset();
        }
        return sql;
    }
}