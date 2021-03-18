package com.fwtai.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private AtomicInteger count = new AtomicInteger(0);

    private int readsize;

    public RoutingDataSource(final int slaveTotal) {
        this.readsize = slaveTotal;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey == null) {
            logger.error("无法确定数据源");
        }
        if (DataSourceType.WRITE.getType().equals(typeKey)) {
            return DataSourceType.WRITE.getType();
        }
        //读库进行负载均衡
        final int a = count.getAndAdd(1);
        return a % readsize;
    }
}