package by.valvik.musicadvisor.constant;

public enum Delimiter {

    SLASH("/"),
    QUESTION("?"),
    EMPTY(""),
    SPACE(" "),
    EQUALS("="),
    AMPERSAND("&");

    private final String sign;

    Delimiter(String sign) {

        this.sign = sign;
    }

    public String getSign() {

        return sign;

    }

}
