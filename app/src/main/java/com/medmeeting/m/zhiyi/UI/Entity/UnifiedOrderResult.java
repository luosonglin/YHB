package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 21/12/2017 10:44 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class UnifiedOrderResult {

    /**
     * tradeId : evet_000000378_20171221103850339
     * tradeTitle : 会议[会议-[会议2]-门票购买]的门票购买
     * amount : 0.01
     * requestPay : null
     * alipayOrderString : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2016120503867037&biz_content=%7B%22out_trade_no%22%3A%22evet_000000378_20171221103850339%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%BC%9A%E8%AE%AE%5B%E4%BC%9A%E8%AE%AE-%5B%E4%BC%9A%E8%AE%AE2%5D-%E9%97%A8%E7%A5%A8%E8%B4%AD%E4%B9%B0%5D%E7%9A%84%E9%97%A8%E7%A5%A8%E8%B4%AD%E4%B9%B0%22%2C%22timeout_express%22%3A%22120m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fapi.medmeeting.com%2Fv1%2Fopen%2Fcallback%2Falipay&sign=Dexr96VePW4S6fkDGAk4wVqaD4mbD2kMg746p4nXoQeiXUl1z6vphSU1U9TEwSSn4bjqblYpnEtKoxvaGXNq0%2Fgi2AE7l67UPQnm5VmwSSa1nQcLm3vP86QKr86wcdBx8hjgkzj7xYztgriXdYDkZTN%2BDGZBi9I1kDock%2F4Jza%2FkETKr8GfKFYGJ6hNgf3uOPDRUMzkdWOHodyom4Pi%2B%2BnS6E%2Fm9M8C082HZ1WqhsfBcIfGVE55wK%2BsjpcxNDHQZJcPjD%2F8Kiad%2B3cgoL01VvT%2FnK59sz4cvI2dQCZrWQ5Q%2Fp8DyV7PHMens8jlcvuUD2inRC9nEzoy6kuomO5M59g%3D%3D&sign_type=RSA2&timestamp=2017-12-21+10%3A38%3A50&version=1.0
     */

    private String tradeId;
    private String tradeTitle;
    private float amount;
    private LiveWechatPayDto requestPay;
    private String alipayOrderString; //微信

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
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

    public LiveWechatPayDto getRequestPay() {
        return requestPay;
    }

    public void setRequestPay(LiveWechatPayDto requestPay) {
        this.requestPay = requestPay;
    }

    public String getAlipayOrderString() {
        return alipayOrderString;
    }

    public void setAlipayOrderString(String alipayOrderString) {
        this.alipayOrderString = alipayOrderString;
    }
}
