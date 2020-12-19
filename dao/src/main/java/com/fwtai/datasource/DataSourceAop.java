package com.fwtai.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class DataSourceAop{

    @Before("execution(* com.fwtai..*.*Dao.find*(..))" +
      "|| execution(* com.fwtai..*.*Dao.list*(..))" +
      "|| execution(* com.fwtai..*.*Dao.query*(..))" +
      "|| execution(* com.fwtai..*.*Dao.get*(..))" +
      "|| execution(* com.fwtai..*.*Dao.select*(..))")
    public void setReadDataSourceType(){
        System.err.println("拦截[read]方法");
        DataSourceContextHolder.read();
    }

    @Before("execution(* com.fwtai..*.*Dao.execute*(..))" +
      "|| execution(* com.fwtai..*.*Dao.save*(..))" +
      "|| execution(* com.fwtai..*.*Dao.add*(..))" +
      "|| execution(* com.fwtai..*.*Dao.edit*(..))" +
      "|| execution(* com.fwtai..*.*Dao.update*(..))" +
      "|| execution(* com.fwtai..*.*Dao.del*(..))")
    public void setWriteDataSourceType() {
        System.err.println("拦截[write]操作");
        DataSourceContextHolder.write();
    }
}