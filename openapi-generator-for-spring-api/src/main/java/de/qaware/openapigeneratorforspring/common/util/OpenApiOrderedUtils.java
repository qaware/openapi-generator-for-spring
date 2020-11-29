/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;

/**
 * Helper util to semantically order beans by priority.
 * Earlier means higher priority in list of autowired beans.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiOrderedUtils {

    public interface DefaultOrdered extends Ordered {
        int DEFAULT_ORDER = 0;

        @Override
        default int getOrder() {
            return DEFAULT_ORDER;
        }
    }

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
