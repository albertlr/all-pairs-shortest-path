/*-
 * %%Ignore-License
 *
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.albertlr.graph.utils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.albertlr.graph.utils.PropertyPlaceholderHelper.PlaceholderResolver;

import javax.annotation.Nullable;
import java.util.Properties;

/**
 * {@link PlaceholderConfigurerSupport} subclass that resolves ${...} placeholders against
 * {@link #setLocation local} {@link #setProperties properties} and/or system properties
 * and environment variables.
 *
 * <p>{@link PropertyPlaceholderConfigurer} is still appropriate for use when:
 * <ul>
 * <li>the {@code spring-context} module is not available (i.e., one is using Spring's
 * {@code BeanFactory} API as opposed to {@code ApplicationContext}).
 * <li>existing configuration makes use of the {@link #setSystemPropertiesMode(int) "systemPropertiesMode"}
 * and/or {@link #setSystemPropertiesModeName(String) "systemPropertiesModeName"} properties.
 * Users are encouraged to move away from using these settings, and rather configure property
 * source search order through the container's {@code Environment}; however, exact preservation
 * of functionality may be maintained by continuing to use {@code PropertyPlaceholderConfigurer}.
 * </ul>
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @see #setSystemPropertiesModeName
 * @see PlaceholderConfigurerSupport
 * @see PropertyOverrideConfigurer
 * @since 02.10.2003
 */
@Slf4j
public class PropertyPlaceholderConfigurer  {


    /**
     * Default placeholder prefix: {@value}.
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    /**
     * Default placeholder suffix: {@value}.
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    /**
     * Default value separator: {@value}.
     */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";

    /**
     * Defaults to {@value #DEFAULT_PLACEHOLDER_PREFIX}.
     */
    protected String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    /**
     * Defaults to {@value #DEFAULT_PLACEHOLDER_SUFFIX}.
     */
    protected String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
    /**
     * Defaults to {@value #DEFAULT_VALUE_SEPARATOR}.
     */
    @Nullable
    protected String valueSeparator = DEFAULT_VALUE_SEPARATOR;
    protected boolean trimValues = false;
    @Nullable
    protected String nullValue;
    protected boolean ignoreUnresolvablePlaceholders = false;


    public static StringValueResolver valueResolver(Properties props) {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        StringValueResolver valueResolver = configurer.new PlaceholderResolvingStringValueResolver(props);
        return valueResolver;
    }

    /**
     * Never check system properties.
     */
    public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
    /**
     * Check system properties if not resolvable in the specified properties.
     * This is the default.
     */
    public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
    /**
     * Check system properties first, before trying the specified properties.
     * This allows system properties to override any other property source.
     */
    public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;

    private int systemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    /**
     * Set how to check system properties: as fallback, as override, or never.
     * For example, will resolve ${user.dir} to the "user.dir" system property.
     * <p>The default is "fallback": If not being able to resolve a placeholder
     * with the specified properties, a system property will be tried.
     * "override" will check for a system property first, before trying the
     * specified properties. "never" will not check system properties at all.
     *
     * @see #SYSTEM_PROPERTIES_MODE_NEVER
     * @see #SYSTEM_PROPERTIES_MODE_FALLBACK
     * @see #SYSTEM_PROPERTIES_MODE_OVERRIDE
     */
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        this.systemPropertiesMode = systemPropertiesMode;
    }

    /**
     * Resolve the given placeholder using the given properties, performing
     * a system properties check according to the given mode.
     * <p>The default implementation delegates to {@code resolvePlaceholder
     * (placeholder, props)} before/after the system properties check.
     * <p>Subclasses can override this for custom resolution strategies,
     * including customized points for the system properties check.
     *
     * @param placeholder          the placeholder to resolve
     * @param props                the merged properties of this configurer
     * @param systemPropertiesMode the system properties mode,
     *                             according to the constants in this class
     * @return the resolved value, of null if none
     * @see #setSystemPropertiesMode
     * @see System#getProperty
     * @see #resolvePlaceholder(String, java.util.Properties)
     */
    @Nullable
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        String propVal = null;
        if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
            propVal = resolveSystemProperty(placeholder);
        }
        if (propVal == null) {
            propVal = resolvePlaceholder(placeholder, props);
        }
        if (propVal == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
            propVal = resolveSystemProperty(placeholder);
        }
        return propVal;
    }

    /**
     * Resolve the given placeholder using the given properties.
     * The default implementation simply checks for a corresponding property key.
     * <p>Subclasses can override this for customized placeholder-to-key mappings
     * or custom resolution strategies, possibly just using the given properties
     * as fallback.
     * <p>Note that system properties will still be checked before respectively
     * after this method is invoked, according to the system properties mode.
     *
     * @param placeholder the placeholder to resolve
     * @param props       the merged properties of this configurer
     * @return the resolved value, of {@code null} if none
     * @see #setSystemPropertiesMode
     */
    @Nullable
    protected String resolvePlaceholder(String placeholder, Properties props) {
        return props.getProperty(placeholder);
    }

    /**
     * Resolve the given key as JVM system property, and optionally also as
     * system environment variable if no matching system property has been found.
     *
     * @param key the placeholder to resolve as system property key
     * @return the system property value, or {@code null} if not found
     * @see System#getProperty(String)
     * @see System#getenv(String)
     */
    @Nullable
    protected String resolveSystemProperty(String key) {
        try {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key);
            }
            return value;
        } catch (Throwable ex) {
            log.debug("Could not access system property '{}': {}", key, ex);
            return null;
        }
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {
        private final PropertyPlaceholderHelper helper;
        private final PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper(
                    placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
            this.resolver = new PropertyPlaceholderConfigurerResolver(props);
        }

        @Override
        @Nullable
        public String resolveStringValue(String strVal) {
            String resolved = this.helper.replacePlaceholders(strVal, this.resolver);
            if (trimValues) {
                resolved = resolved.trim();
            }
            return (resolved.equals(nullValue) ? null : resolved);
        }
    }

    @RequiredArgsConstructor
    private final class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver {
        private final Properties props;

        @Override
        @Nullable
        public String resolvePlaceholder(String placeholderName) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName,
                    this.props, systemPropertiesMode);
        }
    }

}
