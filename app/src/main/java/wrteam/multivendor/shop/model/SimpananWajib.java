package wrteam.multivendor.shop.model;

public class SimpananWajib {
    String user_id;
    String period;
    String years;
    String billing_date;
    String is_status_payment;
    String payment_date;
    String amount;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getBilling_date() {
        return billing_date;
    }

    public void setBilling_date(String billing_date) {
        this.billing_date = billing_date;
    }

    public String getIs_status_payment() {
        return is_status_payment;
    }

    public void setIs_status_payment(String is_status_payment) {
        this.is_status_payment = is_status_payment;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
