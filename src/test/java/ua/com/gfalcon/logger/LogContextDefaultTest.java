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

package ua.com.gfalcon.logger;

import static java.text.MessageFormat.format;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogContextDefaultTest {
    private static final Logger LOG = LoggerFactory.getLogger(LogContextDefaultTest.class);
    private static final String MESSAGE = "message";
    private static final Long QUOTE_ID = 47777L;
    private static final String QUOTE_ID_STRING = "47777";
    private static final String QUOTE_NAME_KEY = "quoteName";
    private static final String QUOTE_NAME_VALUE = "UberQuote";
    private static final String QUOTE_STATUS_KEY = "quoteStatus";
    private static final String QUOTE_STATUS_VALUE = "Jumping";

    private final LogContext<Long, String> logContextDefault = new LogContextDefault();

    private static final String CONTEXT_PREFIX = "ctx:{";
    private static final String CONTEXT_SUFFIX = "}";

    @ParameterizedTest(name = "Should {0}.")
    @MethodSource("argumentsForShouldAddInfoAndGetMessage")
    void shouldAddInfoAndGetMessage(String testCaseName, String message, Map<String, Object> quoteInfo, Long key,
            String expectedResult) {
        //given

        //when
        String actual = logContextDefault.get(message, key, quoteInfo);

        //then
        Assertions.assertEquals(expectedResult, actual);
    }

    private Stream<Arguments> argumentsForShouldAddInfoAndGetMessage() {
        return Stream.of(
                Arguments.of("return formatted message when quoteInfo map is empty", MESSAGE, new HashMap<>(), QUOTE_ID,
                        createMessageWithContextPattern(MESSAGE, format("key={0}", QUOTE_ID_STRING))),
                Arguments.of("return formatted message when quoteInfo map is null", MESSAGE, null, QUOTE_ID,
                        createMessageWithContextPattern(MESSAGE, format("key={0}", QUOTE_ID_STRING))),
                Arguments.of("return formatted message when quoteInfo map is immutable and prefilled with some values",
                        MESSAGE, createMap(QUOTE_NAME_KEY, QUOTE_NAME_VALUE, QUOTE_STATUS_KEY, QUOTE_STATUS_VALUE),
                        QUOTE_ID, createMessageWithContextPattern(MESSAGE,
                                format("key={0}, {1}={2}, {3}={4}", QUOTE_ID_STRING, QUOTE_NAME_KEY, QUOTE_NAME_VALUE,
                                        QUOTE_STATUS_KEY, QUOTE_STATUS_VALUE))), Arguments.of(
                        "return formatted message with proper order when quoteInfo prefilled with values. Info by key first then "
                                + "additional values", MESSAGE,
                        createMap(QUOTE_NAME_KEY, QUOTE_NAME_VALUE, QUOTE_STATUS_KEY, QUOTE_STATUS_VALUE), QUOTE_ID,
                        createMessageWithContextPattern(MESSAGE,
                                format("key={0}, {1}={2}, {3}={4}", QUOTE_ID_STRING, QUOTE_NAME_KEY, QUOTE_NAME_VALUE,
                                        QUOTE_STATUS_KEY, QUOTE_STATUS_VALUE))),
                Arguments.of("return formatted message when key is null", MESSAGE,
                        Collections.singletonMap(QUOTE_NAME_KEY, QUOTE_NAME_VALUE), null,
                        createMessageWithContextPattern(MESSAGE, format("{0}={1}", QUOTE_NAME_KEY, QUOTE_NAME_VALUE))),
                Arguments.of("return formatted message is null", null, new HashMap<>(), QUOTE_ID,
                        createMessageWithContextPattern(EMPTY, format("key={0}", QUOTE_ID_STRING))),
                Arguments.of("return formatted message is blank", " ", new HashMap<>(), QUOTE_ID,
                        createMessageWithContextPattern(EMPTY, format("key={0}", QUOTE_ID_STRING))));
    }

    private Map<String, String> createMap(String... args) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }

    private String createMessageWithContextPattern(String message, String params) {
        String contextPrefix = StringUtils.isNotBlank(message) ? ". " + CONTEXT_PREFIX : CONTEXT_PREFIX;

        return StringUtils.join(message, contextPrefix, params, CONTEXT_SUFFIX);
    }

}