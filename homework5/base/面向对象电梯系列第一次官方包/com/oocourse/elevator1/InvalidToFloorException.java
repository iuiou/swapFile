package base.面向对象电梯系列第一次官方包.com.oocourse.elevator1;

/**
 * 非法目标楼层异常
 */
class InvalidToFloorException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    InvalidToFloorException(String original) {
        super(original);
    }
}
