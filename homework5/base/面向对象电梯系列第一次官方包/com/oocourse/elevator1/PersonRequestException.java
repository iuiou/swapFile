package base.面向对象电梯系列第一次官方包.com.oocourse.elevator1;

/**
 * 人员请求解析异常
 */
@SuppressWarnings("unused")
abstract class PersonRequestException extends Exception {
    private final String original;

    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    PersonRequestException(String original) {
        super(String.format("Person request parse failed! - \"%s\"", original));
        this.original = original;
    }

    /**
     * 获取原字符串
     *
     * @return 原字符串
     */
    public String getOriginal() {
        return original;
    }
}
