/*-
 * #%L
 * All-Pair Shortest Path
 *  
 * Copyright (C) 2019 László-Róbert, Albert (robert@albertlr.ro)
 *  
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
package ro.albertlr.graph.utils;

import com.google.common.collect.ForwardingMap;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class RProperties extends ForwardingMap<String, Object> {
    public static RProperties load() {
        return load("/config.properties");
    }

    public static RProperties load(String location) {
        Properties properties = new Properties();
        try {
            properties.load(RProperties.class.getResourceAsStream(location));
            log.info("properties loaded: {}", properties);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new RProperties(properties);
    }

    private final Properties delegate;
    private final StringValueResolver resolver;

    private RProperties(Properties properties) {
        this.delegate = properties;
        this.resolver = PropertyPlaceholderConfigurer.valueResolver(delegate);
    }

    @Override
    public String get(@Nullable Object key) {
        return getProperty((String) key);
    }

    public String getProperty(String key) {
        return resolver.resolveStringValue(delegate.getProperty(key));
    }

    public String getProperty(String key, String defaultValue) {
        return resolver.resolveStringValue(delegate.getProperty(key, defaultValue));
    }

    @Override
    protected Map<String, Object> delegate() {
        return (Map<String, Object>) (Map) delegate;
    }
}
