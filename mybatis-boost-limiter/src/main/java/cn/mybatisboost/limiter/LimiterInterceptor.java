package cn.mybatisboost.limiter;

import cn.mybatisboost.core.Configuration;
import cn.mybatisboost.core.SqlProvider;
import cn.mybatisboost.core.util.MyBatisUtils;
import cn.mybatisboost.limiter.provider.MySQL;
import cn.mybatisboost.limiter.provider.PostgreSQL;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class LimiterInterceptor implements Interceptor {

    private Configuration configuration;
    private Map<String, SqlProvider> providerMap = new HashMap<>();
    private volatile SqlProvider provider;

    public LimiterInterceptor() {
        this(new Configuration());
    }

    public LimiterInterceptor(Configuration configuration) {
        this.configuration = configuration;
        addProvider(new MySQL());
        addProvider(new PostgreSQL());
    }

    public synchronized void addProvider(SqlProvider provider) {
        providerMap.put(provider.toString(), provider);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        SqlProvider provider = this.provider;
        if (provider == null || configuration.isMultipleDatasource()) {
            Connection connection = (Connection) invocation.getArgs()[0];
            String databaseName = connection.getMetaData().getDatabaseProductName();
            this.provider = provider = providerMap.get(databaseName);
        }

        if (provider != null) {
            MetaObject metaObject = MyBatisUtils.getRealMetaObject(invocation.getTarget());
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            provider.replace(metaObject, mappedStatement, boundSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}