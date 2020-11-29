/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Shaded
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

package de.qaware.openapigeneratorforspring.ui.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStreamReader;

public class MustacheTemplate {

    private final Template template;

    public MustacheTemplate(String templateClassPath) {
        InputStreamReader reader = new InputStreamReader(MustacheTemplate.class.getResourceAsStream(templateClassPath));
        template = Mustache.compiler().nullValue("").escapeHTML(false).compile(reader);
    }

    public String execute(Object context) {
        return template.execute(context);
    }

}
