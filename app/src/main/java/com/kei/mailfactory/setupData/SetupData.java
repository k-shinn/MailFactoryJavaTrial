package com.kei.mailfactory.setupData;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.kei.mailfactory.BR;

/**
 * メールの雛形となるデータ
 * Created by kei on 2017/11/19.
 */

public class SetupData extends BaseObservable {

    private String categoryTitle;
    private String mailTitle;
    private String[] toAddress;
    private String[] ccAddress;
    private String[] bccAddress;
    private String messageBase;
    private String reason;
    private String delay;

    @Bindable
    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Bindable
    public String getMailTitle() {
        return mailTitle;
    }

    @Bindable
    public String[] getToAddress() {
        return toAddress;
    }

    @Bindable
    public String[] getCcAddress() {
        return ccAddress;
    }

    @Bindable
    public String[] getBccAddress() {
        return bccAddress;
    }

    @Bindable
    public String getMessageBase() {
        return messageBase;
    }

    @Bindable
    public String getReason() {
        return reason;
    }

    @Bindable
    public String getDelay() {
        return delay;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
        notifyPropertyChanged(BR.categoryTitle);
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
        notifyPropertyChanged(BR.mailTitle);
    }

    public void setToAddress(String[] toAddress) {
        this.toAddress = toAddress;
        notifyPropertyChanged(BR.toAddress);
    }

    public void setCcAddress(String[] ccAddress) {
        this.ccAddress = ccAddress;
        notifyPropertyChanged(BR.ccAddress);
    }

    public void setBccAddress(String[] bccAddress) {
        this.bccAddress = bccAddress;
        notifyPropertyChanged(BR.bccAddress);
    }

    public void setMessageBase(String messageBase) {
        this.messageBase = messageBase;
        notifyPropertyChanged(BR.messageBase);
    }

    public void setReason(String reason) {
        this.reason = reason;
        notifyPropertyChanged(BR.reason);
    }

    public void setDelay(String delay) {
        this.delay = delay;
        notifyPropertyChanged(BR.delay);
    }
}
