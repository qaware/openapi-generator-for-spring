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

package de.qaware.openapigeneratorforspring.ui.webjar;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a minimal version of org.webjars.WebJarAssetLocator
 * supporting the "shaded" away webjars resources.
 */
public class ShadedWebJarAssetLocator {

    // this path must correspond to the shading done in pom.xml
    private static final String SHADED_WEBJARS_PATH = "META-INF/resources/de/qaware/openapigeneratorforspring/shaded/webjars";

    private final Map<String, List<String>> pathsByWebJarName = new HashMap<>();

    public ShadedWebJarAssetLocator() {
        try (ScanResult scanResult = new ClassGraph().whitelistPaths(SHADED_WEBJARS_PATH).scan()) {
            for (Resource resource : scanResult.getAllResources()) {
                String pathWithPrefix = resource.getPath().substring(SHADED_WEBJARS_PATH.length() + 1);
                String webJarName = pathWithPrefix.substring(0, pathWithPrefix.indexOf("/"));
                pathsByWebJarName.computeIfAbsent(webJarName, ignored -> new ArrayList<>()).add(resource.getPath());
            }
        }
    }

    public String getFullPath(String webJarName, String pathSuffix) {
        List<String> paths = pathsByWebJarName.get(webJarName);
        if (paths == null) {
            throw new IllegalStateException("Cannot find any resource paths for " + webJarName);
        }
        return paths.stream()
                .filter(path -> path.endsWith(pathSuffix))
                .reduce((a, b) -> {
                    if (!a.equals(b)) {
                        throw new IllegalStateException("Two conflicting paths found for suffix " + pathSuffix + ": " + a + " vs. " + b);
                    }
                    return a;
                })
                .orElseThrow(() -> new IllegalStateException("No path found for suffix " + pathSuffix));
    }
}
