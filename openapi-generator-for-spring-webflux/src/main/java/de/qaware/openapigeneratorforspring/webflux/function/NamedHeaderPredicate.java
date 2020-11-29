/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebFlux
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

package de.qaware.openapigeneratorforspring.webflux.function;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.ServerRequest;

@RequiredArgsConstructor(staticName = "of")
public class NamedHeaderPredicate implements RequestPredicate {

    private final String headerName;
    private final String headerValue;

    @Override
    public boolean test(ServerRequest request) {
        if (CorsUtils.isPreFlightRequest(request.exchange().getRequest())) {
            return true;
        } else {
            return request.headers().header(headerName).contains(headerValue);
        }
    }

    @Override
    public void accept(RequestPredicates.Visitor visitor) {
        visitor.header(headerName, headerValue);
    }

    @Override
    public String toString() {
        return headerName + ": " + headerValue;
    }
}
