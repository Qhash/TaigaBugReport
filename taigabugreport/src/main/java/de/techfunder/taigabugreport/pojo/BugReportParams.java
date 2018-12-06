package de.techfunder.taigabugreport.pojo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Locale;

import de.techfunder.taigabugreport.R;

public class BugReportParams implements Serializable {
    private String mProjectId;
    private String mAdditionalParams;
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }


    public String getAdditionalParams() {
        return mAdditionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.mAdditionalParams = additionalParams;
    }

    private RequestSettings mRequestSettings;

    public RequestSettings getRequestSettings() {
        return mRequestSettings;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String mProjectId) {
        this.mProjectId = mProjectId;
    }

    public void setRequestSettings(RequestSettings mRequestSettings) {
        this.mRequestSettings = mRequestSettings;
    }

    public String getDescription(String description, Context context){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.taigabugreport_text_info) + "\n");
        if(context != null)
            stringBuilder.append(context.getString(R.string.taigabugreport_text_version)).append(getVersion(context)).append("\n");
        stringBuilder.append(context.getString(R.string.taigabugreport_text_manufactured)).append(Build.MANUFACTURER).append("\n")
                .append(context.getString(R.string.taigabugreport_text_model)).append(Build.MODEL).append("\n")
                .append(context.getString(R.string.taigabugreport_text_api)).append(Build.VERSION.SDK_INT).append("\n")
                .append(context.getString(R.string.taigabugreport_text_additional_parameters) + "\n")
                .append(getAdditionalParams()).append("\n")
                .append(context.getString(R.string.taigabugreport_text_description) + "\n").append(description).append("\n");
        return stringBuilder.toString();
    }

    public String getTitle(String title, Context context){
        return context.getString(R.string.taigabugreport_text_report) + " " + (TextUtils.isEmpty(title) ? "" : title);
    }

    private String getVersion(Context context) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String format = "%s.%d";
            return String.format(Locale.getDefault(), format, versionName, versionCode);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            return "Version: Not defined";
        }
    }
}
