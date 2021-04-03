package base.面向对象电梯系列第一次官方包.com.oocourse.elevator1;

/**
 * 起始目标楼层相同
 */
class DuplicatedFromToFloorException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    DuplicatedFromToFloorException(String original) {
        super(original);
    }
}
