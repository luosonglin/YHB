package com.medmeeting.m.zhiyi.UI.Entity;

public class LivePayDto {

    /**
     * prepayId : null
     * tradeTitle : 直播节目[王医师的首次直播]的门票购买
     * amount : 0.01
     * requestPay : null
     * alipayOrderString : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2016120503867037&biz_content=%7B%22out_trade_no%22%3A%22prog_000100010_20170703153955881%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E7%9B%B4%E6%92%AD%E8%8A%82%E7%9B%AE%5B%E7%8E%8B%E5%8C%BB%E5%B8%88%E7%9A%84%E9%A6%96%E6%AC%A1%E7%9B%B4%E6%92%AD%5D%E7%9A%84%E9%97%A8%E7%A5%A8%E8%B4%AD%E4%B9%B0%22%2C%22timeout_express%22%3A%22120m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fapi.medmeeting.com%2Fv1%2Fopen%2Fcallback%2Falipay&sign=TSZ5ntRdD1ZI0B%2Ffxd2Clp3CV5b4YxRcssteONSYSP64QRbuHvqhZ1ZpNxW4hUGqj3SJAWA2Dx3F1xbsQ21PqSye02OtEjTEG7AQrr3TtjgkTDSkxPol6joxhXCSYEI%2BdCt8gxiANKgnCmX%2FGQMtacODZ60LG8CzU5JhJIrXS%2FhnGodG3GCMicEOV3r91A2T0hGblbx1d%2Bm4Zv43r%2F3hoRaauyR4xvkJa8BsWlLoltwztvz4ZsckrlyYtctLbdPAichIo%2FJCUaMEVTZ4PRglDEC%2FlJ7K5qV%2F81C3SZp9rlV0LsGdcJQSusgTln9meDMewVGsJHIJJ%2B%2BQ28mXrl1WYA%3D%3D&sign_type=RSA2&timestamp=2017-07-03+15%3A39%3A55&version=1.0
     */

    private String prepayId;
    private String tradeTitle;
    private float amount;
    private Object requestPay;
    private String alipayOrderString;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Object getRequestPay() {
        return requestPay;
    }

    public void setRequestPay(Object requestPay) {
        this.requestPay = requestPay;
    }

    public String getAlipayOrderString() {
        return alipayOrderString;
    }

    public void setAlipayOrderString(String alipayOrderString) {
        this.alipayOrderString = alipayOrderString;
    }
}
