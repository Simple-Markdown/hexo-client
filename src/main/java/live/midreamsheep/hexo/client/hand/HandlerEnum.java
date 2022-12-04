package live.midreamsheep.hexo.client.hand;

public enum HandlerEnum {
    DPUSH(0x01),
    PULL(0x02),
    DELETE_FILE(0x03),
    ADD_FILE(0x04),
    UPDATE_FILE(0x05),
    ADD_DIR(0x06),
    DELETE_DIR(0x07),
    PUSH(0x08),
    SERVER(0x09),
    SERVER_STOP(0x10);
    private final int id;

    HandlerEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
