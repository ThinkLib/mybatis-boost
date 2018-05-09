# MybatisBoost

Mybatis SQL��������MybatisBoost������ͨ��Mapper��mybatis�﷨��ǿ���޸�֪ͨ�÷�ҳ��SQLָ�����ع��ܣ�ʹ��MybatisBoost��������Ŀ���Ч�ʰɣ�

## ���ٿ�ʼ

Tips������Spring Boot��Ŀ�Ŀ��ٿ�ʼ

```xml
<dependency>
    <groupId>tech.rfprojects.mybatisboost</groupId>
    <artifactId>mybatis-boost-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

���������ݿ�Table����POJO����һ�£���������������ʽҲһ�µĻ����������˲������ݡ�

MybatisBoost�����м������õı���ת���������ڼ�����ı���ΪT_AnTable�����POJO����ΪAnTable������ʹ����������������ӳ�䡣

```
mybatisboost.name-adaptor=cn.mybatisboost.core.adaptor.TPrefixedNameAdaptor
```

���������ݿ�����������ʽΪsnake_case������ʹ����������������ӳ�䡣

```
mybatis.configuration.map-underscore-to-camel-case=true
```

�����Զ�ӳ�䷽����MybatisBoostͬ���ṩ�ֶ�ӳ��ķ��������ڼ�����ı���ΪT_ThisTable�����POJO����ΪThatTable���������Ե�����Ҳ��һ�£������ʹ��JPA�ṩ�ı�׼ע������ֶ�ӳ�䡣

```java
@Table(name="T_ThisTable")
public class ThatTable {

    @Id
    private Long id; // ��Ĭ��������Ϊ��id�����ֶ���Ϊ����������Ժ���@Idע��
    @Column(name="thisField")
    private String thatField;
}
```

## Mapperʹ��ָ��

�̳���GenericMapper<T, ID>��Mybatis Mapper�ӿڼ��Զ�ӵ����GenericMapper���������ܡ�Ĭ������»�ʹ��entity���������Խ�����ɾ��ģ���ʹ��properties��conditionPropertiesָ�������ѯ�����ԡ�

```java
public interface GenericMapper<T, ID> {

    int count(T entity, String... conditionProperties);
    T selectOne(T entity, String... conditionProperties);
    List<T> select(T entity, String... conditionProperties);
    List<T> selectWithRowBounds(T entity, RowBounds rowBounds, String... conditionProperties);
    int countAll();
    List<T> selectAll();
    List<T> selectAllWithRowBounds(RowBounds rowBounds);
    T selectById(ID id);
    List<T> selectByIds(ID... ids);
    int insert(T entity, String... properties);
    int batchInsert(List<T> entities, String... properties);
    int insertSelectively(T entity, String... properties);
    int batchInsertSelectively(List<T> entities, String... properties);
    int update(T entity, String... conditionProperties);
    int updatePartially(T entity, String[] properties, String... conditionProperties);
    int updateSelectively(T entity, String... conditionProperties);
    int updatePartiallySelectively(T entity, String[] properties, String... conditionProperties);
    int delete(T entity, String... conditionProperties);
    int deleteByIds(ID... ids);
}
```

## Mybatis�﷨��ǿ

MybatisBoostĿǰֻ����һ����Χ������ǿ����ֱ�Ӽ�ʵ���ɡ�

�ر�Mybatis�﷨��ǿ
```xml
<select id="selectPostIn">
    SELECT * FROM POST WHERE ID in
    <foreach item="item" index="index" collection="list"
             open="(" separator="," close=")">
        #{item}
    </foreach>
</select>
```

����Mybatis�﷨��ǿ
```xml
<select id="selectPostIn">
    SELECT * FROM POST WHERE ID in (#{list})
</select>
```

## �޸�֪��ҳ

ֻ����Mapper�ķ��������ϼ���һ��RowBounds��������ʵ���޸�֪�������ҳ��

```java
List<T> selectAllWithRowBounds(RowBounds rowBounds); // RowBounds�ں�offset��limit�ֶ�
```

## SQLָ������

Ĭ����������й��ܶ��ǲ������ģ�������˵��ֱ�������ã����׶���

```
mybatisboost.showQuery=Boolean
mybatisboost.showQueryWithParameters=Boolean
mybatisboost.slowQueryThresholdInMillis=Long
mybatisboost.slowQueryHandler=Class<? extends BiConsumer<String, Long>>
```