/*
 * MIT License
 *
 * Copyright (c) 2018 NIX Solutions Ltd.
 * Copyright (c) 2021 Oleksii V. KHALIKOV, PE.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ua.com.gfalcon.logger.configuration;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.com.gfalcon.logger.parameters.extractor.ContextParamExtractor;
import ua.com.gfalcon.logger.parameters.extractor.ContextParamExtractorFactory;

/**
 * Context extractor factory configuration.
 */
@Configuration
public class ContextExtractorFactoryConfiguration {
    private static final ContextParamExtractor<Object> DEFAULT_EXTRACTOR = new ContextParamExtractor<Object>() {
        @Override
        public List<Class<?>> getExtractableClasses() {
            return emptyList();
        }

        @Override
        public Map<String, Object> extractParams(String name, Object parameter) {
            return emptyMap();
        }
    };

    /**
     * Context param extractor factory.
     */
    @Bean
    public ContextParamExtractorFactory contextParamExtractorFactory(
            @Autowired List<? extends ContextParamExtractor<?>> contextParamExtractorsList,
            @Autowired(required = false) @Qualifier("defaultExtractor") ContextParamExtractor<Object> extractor) {
        ContextParamExtractorFactory contextParamExtractorFactory = new ContextParamExtractorFactory(
                contextParamExtractorsList);

        if (Objects.isNull(extractor)) {
            contextParamExtractorFactory.setDefaultContextParamExtractor(DEFAULT_EXTRACTOR);
        } else {
            contextParamExtractorFactory.setDefaultContextParamExtractor(extractor);
        }

        return contextParamExtractorFactory;
    }

    @Bean
    public ContextParamExtractor<Object> defaultExtractor() {
        return DEFAULT_EXTRACTOR;
    }
}
