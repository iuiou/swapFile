package base.面向对象电梯系列第一次官方包.com.oocourse.elevator1;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户请求类型
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "WeakerAccess"})
public class PersonRequest {
    private static final int MAX_FLOOR = 19;
    private static final int MIN_FLOOR = 1;

    /**
     * 判断楼层是否合法
     *
     * @param floor 楼层
     * @return 是否合法
     */
    private static boolean isValidFloor(Integer floor) {
        return (floor != null) && (floor >= MIN_FLOOR) && (floor <= MAX_FLOOR);
    }

    private final int fromFloor;
    private final int toFloor;
    private final int personId;

    /**
     * 构造函数
     *
     * @param fromFloor 起始楼层
     * @param toFloor   目标楼层
     * @param personId  终止楼层
     */
    public PersonRequest(int fromFloor, int toFloor, int personId) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.personId = personId;
    }

    /**
     * 获取出发楼层
     *
     * @return 出发楼层
     */
    public int getFromFloor() {
        return fromFloor;
    }

    /**
     * 获取目标楼层
     *
     * @return 目标楼层
     */
    public int getToFloor() {
        return toFloor;
    }

    /**
     * 获取人员id
     *
     * @return 人员id
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * 转为字符串形式
     *
     * @return 字符串形式
     */
    @Override
    public String toString() {
        return String.format("%d-FROM-%d-TO-%d", personId, fromFloor, toFloor);
    }

    /**
     * 获取哈希值
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{
                this.personId, this.fromFloor, this.toFloor});
    }

    /**
     * 判断对象是否相等
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof PersonRequest) {
            return (((PersonRequest) obj).fromFloor == this.fromFloor)
                    && (((PersonRequest) obj).toFloor == this.toFloor)
                    && (((PersonRequest) obj).personId == this.personId);
        } else {
            return false;
        }
    }

    private static final String PARSE_PATTERN_STRING =
        "^(?<personId>\\d+)-FROM-" +
            "(?<fromFloor>(-|\\+|)\\d+)-TO-" +
            "(?<toFloor>(-|\\+|)\\d+)\\s*$";
    private static final Pattern PARSE_PATTERN
        = Pattern.compile(PARSE_PATTERN_STRING);
    private static final BigInteger INT_MAX = BigInteger.valueOf(+2147483647);
    private static final BigInteger INT_MIN = BigInteger.valueOf(-2147483648);

    /**
     * 判断字符串是否为合法int类型
     *
     * @param string 字符串
     * @return 是否合法
     */
    private static boolean isValidInteger(String string) {
        try {
            BigInteger integer = new BigInteger(string);
            return (integer.compareTo(INT_MAX) <= 0
                    && integer.compareTo(INT_MIN) >= 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将字符串转为Integer类型（非法时返回null）
     *
     * @param string 字符串
     * @return 转换值
     */
    private static Integer toValidInteger(String string) {
        if (isValidInteger(string)) {
            return new BigInteger(string).intValue();
        } else {
            return null;
        }
    }

    /**
     * 解析字符串至PersonRequest
     *
     * @param string 字符串
     * @return 解析结果
     * @throws PersonRequestException 解析失败
     */
    static PersonRequest parse(String string)
            throws PersonRequestException {
        Matcher matcher = PARSE_PATTERN.matcher(string);
        if (matcher.matches()) {
            String personIdString = matcher.group("personId");
            Integer personId = toValidInteger(personIdString);
            if (personId == null) {
                throw new InvalidPersonIdException(string);
            }

            String fromFloorString = matcher.group("fromFloor");
            Integer fromFloor = toValidInteger(fromFloorString);
            if (!isValidFloor(fromFloor)) {
                throw new InvalidFromFloorException(string);
            }

            String toFloorString = matcher.group("toFloor");
            Integer toFloor = toValidInteger(toFloorString);
            if (!isValidFloor(toFloor)) {
                throw new InvalidToFloorException(string);
            }

            if (fromFloor.intValue() == toFloor.intValue()) {
                throw new DuplicatedFromToFloorException(string);
            }

            return new PersonRequest(fromFloor, toFloor, personId);
        } else {
            throw new InvalidPatternException(string);
        }
    }
}
