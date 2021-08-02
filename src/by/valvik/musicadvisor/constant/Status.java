package by.valvik.musicadvisor.constant;

public enum Status {

    OK(200),
    BAD_REQUEST(400);

    private final int code;

    Status(int code) {

        this.code = code;

    }

    public int getCode() {

        return code;

    }

}
