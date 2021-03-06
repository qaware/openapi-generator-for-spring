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

package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Consumer for to-be-referenced {@link SecurityScheme security
 * schemes}. They are always referenced as there is no
 * other way than specifying them within the Open Api model.
 */
public interface ReferencedSecuritySchemesConsumer extends Consumer<Map<String, SecurityScheme>>, ReferencedItemConsumer {
}
