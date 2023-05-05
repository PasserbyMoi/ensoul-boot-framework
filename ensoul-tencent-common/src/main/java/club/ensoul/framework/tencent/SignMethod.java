package club.ensoul.framework.tencent;

public enum SignMethod {

    SIGN_SHA1("HmacSHA1"),
    SIGN_SHA256("HmacSHA256"),
    SIGN_TC3_256("TC3-HMAC-SHA256");

    public final String signMethod;

    SignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

}