package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Helper util to semantically order beans by priority.
 * Earlier means higher priority in list of autowired beans.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiOrderedUtils {
    // determines how many beans can be squeezed in between
    // should be decently large
    private static final int ORDER_INCREMENT = 100;
    // determines how often earlierThan can be chained before we reach "earliest"
    // should not be too small
    private static final int MUCH_FACTOR = 10;

    /**
     * Run earlier than given order.
     *
     * @param otherOrder other order
     * @return earlier order
     */
    public static int earlierThan(int otherOrder) {
        return otherOrder - ORDER_INCREMENT;
    }

    /**
     * Run much earlier than given order.
     *
     * @param otherOrder other order
     * @return much earlier order
     */
    public static int muchEarlierThan(int otherOrder) {
        return otherOrder - MUCH_FACTOR * ORDER_INCREMENT;
    }

    /**
     * Run later than given order.
     *
     * @param otherOrder other order
     * @return later order
     */
    public static int laterThan(int otherOrder) {
        return otherOrder + ORDER_INCREMENT;
    }

    /**
     * Run much later than given order.
     *
     * @param otherOrder other order
     * @return much later order
     */
    public static int muchLaterThan(int otherOrder) {
        return otherOrder + MUCH_FACTOR * ORDER_INCREMENT;
    }
}
