package de.techfunder.taigabugreport.pojo;

import java.io.Serializable;

public class RequestSettings implements Serializable {
    private String mServerUrl;
    private String mSendMessageUrl;

    public static Builder newBuilder() {
        return new RequestSettings().new Builder();
    }

    public String getServerUrl() {
        return mServerUrl;
    }

    public String getSendReportUrl() {
        return mSendMessageUrl;
    }

    public class Builder {
        public RequestSettings.Builder setServerUrl(String serverUrl) {
            RequestSettings.this.mServerUrl = serverUrl;
            return this;
        }

        public RequestSettings.Builder setSendReportUrl(String urlSendMessage) {
            RequestSettings.this.mSendMessageUrl = urlSendMessage;
            return this;
        }

        public RequestSettings build() {
            return RequestSettings.this;
        }
    }
}

