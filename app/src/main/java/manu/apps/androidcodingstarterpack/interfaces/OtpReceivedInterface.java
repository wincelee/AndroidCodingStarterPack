package manu.apps.androidcodingstarterpack.interfaces;

public interface OtpReceivedInterface {

    void onOtpReceived(String otp);
    void onOtpTimeout();
}
