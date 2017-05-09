package com.lol.fwk.db
        ;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditStateType implements UserType {

    /** 从二级缓存中取这个对象数据时要调用的方法 */
    public Object assemble(Serializable arg0, Object arg1)
            throws HibernateException {
        return arg0;
    }

    /** 这是用于Hibernate缓存生成的快照，由于AuditStateEnum是不可变的，直接返回就好了。 */
    public Object deepCopy(Object arg0) throws HibernateException {
        return arg0;
    }

    /** hibernate把这个数据放入二级缓存时要调用的方法 */
    public Serializable disassemble(Object arg0) throws HibernateException {
        return (Serializable) arg0;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }
    /** 从JDBC的ResultSet读取属性值。这个方法是在从数据库查询数据时用到。 */
    @Override
    public Object nullSafeGet(ResultSet resultset, String[] names, SharedSessionContractImplementor sharedsessioncontractimplementor, Object obj) throws HibernateException, SQLException {
        String value = resultset.getString(names[0]);
        return AuditStateEnum.getAuditState(value);
    }
    /** 将属性的值设置到PreparedStatement。 */
    @Override
    public void nullSafeSet(PreparedStatement preparedstatement, Object value, int index, SharedSessionContractImplementor sharedsessioncontractimplementor) throws HibernateException, SQLException {
        if (value == null) {
            preparedstatement.setString(index, AuditStateEnum.Default.getValue());
        } else {
            preparedstatement.setString(index, ((AuditStateEnum) value).getValue());
        }
    }

    /** 告诉hibernate这个类型是不可变的。有微小的性能优化 */
    public boolean isMutable() {
        return false;
    }

    /** 处理脱管对象状态的合并。 */
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    /** 告诉Hibernate这个UserType用来映射的数据类型。这里是AuditStateEnum类 */
    public Class returnedClass() {
        return AuditStateEnum.class;
    }

    /** 告诉Hibernate要使用什么SQL列类型生成DDL */
    public int[] sqlTypes() {
        return new int[] {StringType.INSTANCE.sqlType() };
    }

}
