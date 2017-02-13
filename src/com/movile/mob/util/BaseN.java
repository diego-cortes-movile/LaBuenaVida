package com.movile.mob.util;

/**
 * @author movile
 */
@SuppressWarnings("javadoc")
public enum BaseN {

    BASE2(2),
    BASE8(8),
    BASE16(16),
    BASE36(36),
    BASE62(62),
    BASE64(64);

    private static final String BASE_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+-";

    int base = 0;

    private BaseN(int val) {
        this.base = val;
    }

    private int getBase() {
        return this.base;
    }

    /**
     * @param decimalNumber decimal number to convert
     * @return convertedDecimal
     */
    public String convertDecimal(Long decimalNumber) {
        return fromDecimalToOtherBase(getBase(), decimalNumber);
    }

    /**
     * @param baseNumber number to revert
     * @return reverted number
     */
    public Long revertToDecimal(String baseNumber) {
        return fromOtherBaseToDecimal(getBase(), baseNumber);
    }

    private static String fromDecimalToOtherBase(int base, Long decimalNumber) {

        if (decimalNumber.equals(0L)) {
            return String.valueOf(BASE_CHARACTERS.charAt(0));
        }

        StringBuffer sb = new StringBuffer();

        int mod = 0;
        Long x = 0L;
        Long baseL = new Long(base);

        while (decimalNumber != 0) {
            x = decimalNumber % baseL;
            mod = x.intValue();
            sb.append(BASE_CHARACTERS.charAt(mod));
            decimalNumber = decimalNumber / baseL;
        }

        return sb.reverse().toString();
    }

    private static Long fromOtherBaseToDecimal(int base, String number) {

        int iterator = number.length();
        Long returnValue = 0L;
        Long multiplier = 1L;
        Long baseL = new Long(base);

        while (iterator > 0) {
            returnValue = returnValue + (BASE_CHARACTERS.indexOf(number.substring(iterator - 1, iterator)) * multiplier);
            multiplier = multiplier * baseL;
            --iterator;
        }

        return returnValue;
    }

}