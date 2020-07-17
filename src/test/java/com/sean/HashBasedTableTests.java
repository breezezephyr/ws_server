package com.sean;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.Map;

public class HashBasedTableTests {
    @Test
    public void test() {
        System.out.println("Hello World!");
    }

    @Test
    public void testHashBasedTable() {
        // key:识别出来的道具个数，columnkey:识别出n个道具个数出现次数，value:frame_raw_data json
        Table<String,String,String> tables= HashBasedTable.create();
        tables.put("user-domain","user","id");
        tables.put("user-domain","user_profile","user_id");
        tables.put("user-domain","user_account_info","user_id");
        tables.put("user-domain","car_relation","user_id");
        tables.put("order-domain","car_relation","order_id");
        tables.put("order-domain","car_order","id");

        Map<String, String> car_relation = tables.column("car_relation");

        System.out.println("Hello World!");
    }
}