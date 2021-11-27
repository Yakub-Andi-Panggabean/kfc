package com.ta.kfc.mercu.interfaces.web.master;

public class MasterModule {
    public static final String MASTER_PATH = "/master";
    public static final String MASTER_BRAND_PATH = MASTER_PATH + "/brand";
    public static final String MASTER_PRODUCT_PATH = MASTER_PATH + "/product";
    public static final String MASTER_MODEL_PATH = MASTER_PATH + "/model";
    public static final String MASTER_SUPPLIER_PATH = MASTER_PATH + "/supplier";
    public static final String MASTER_USER_PATH = MASTER_PATH + "/user";
    public static final String MASTER_UNIT_PATH = MASTER_PATH + "/unit";
    public static final String MASTER_DEPARTMENT_PATH = MASTER_PATH + "/department";

    public static final String MASTER_MODEL_STATUS_PATH = MASTER_MODEL_PATH + "/{id}";
    public static final String MASTER_BRAND_STATUS_PATH = MASTER_BRAND_PATH + "/{id}";
    public static final String MASTER_PRODUCT_STATUS_PATH = MASTER_PRODUCT_PATH + "/{id}";
    public static final String MASTER_UNIT_STATUS_PATH = MASTER_UNIT_PATH + "/{id}";
    public static final String MASTER_DEPARTMENT_STATUS_PATH = MASTER_DEPARTMENT_PATH + "/{id}";

}
