package com.hengxuan.stock.http;

/**
 * Created by Administrator on 2015/8/18.
 */
public class HttpAPI {
    public static final String URL_BASE = "http://115.159.6.104/ehtrest/api/stock/";
//    public static final String URL_BASE = "http://192.168.1.2/ehtrest/api/stock/";
    //api
    public static final String GET_ACCOUT_DETAIL =URL_BASE + "get_accout_rate";//��ȡ�˻����ʲ����������棬�ۼ�����3������
    public static final String GET_EXCHANGE_DETAIL =URL_BASE  +  "getexchangedetail";//��ȡ��Ʊ�ؽ��׼�¼
    public static final String PURCHASE =URL_BASE + "purchaseproduct";//�깺
    public static final String REDEEM =URL_BASE + "redemption";//���
    public static final String REGISTER =URL_BASE + "register";//ע��
    public static final String SIGN_IN =URL_BASE + "sign_in";//��¼
    public static final String VERIFY =URL_BASE + "";//ͨ��������֤���
    public static final String GET_ARTICLE =URL_BASE + "get_article";//��ȡ�����б�
    public static final String GET_ARTICLE_CONTEXT =URL_BASE + "get_context";//��ȡ��������
    public static final String GET_TODAY_STOCK =URL_BASE + "get_today_stock_code";//��ȡÿ���Ƽ���Ʊ����
    public static final String GET_MONTH_STOCK =URL_BASE + "get_month_stock_code";//��ȡÿ���Ƽ���Ʊ����
    public static final String GET_JG_STOCK =URL_BASE + "get_institution_code";//��ȡ������Ʊ����
    public static final String GET_USER_ID = URL_BASE + "getIdentity_id";//��ȡ�û�id
    public static final String GET_HISTORICAL_MSG = URL_BASE + "get_new_tactics";
    //url to get bs sign
    public static final String GET_BS_SIGN = URL_BASE + "get_stock_trading_time";
    //
    public static final String GET_DPGD_CONTENT = URL_BASE + "get_grail_article";

    public static final String ZFB_ZF_URL = "http://115.159.6.104/ehtrest/api/mall/index?identityId=";

    public static final String CHECK_UPDATE = "http://115.159.6.104/ehtrest/api/soft/version_update/";
}
