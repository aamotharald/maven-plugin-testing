/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugin.testing;

import javax.inject.Inject;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.junit5.InjectMojo;
import org.apache.maven.plugin.testing.junit5.MojoParameter;
import org.apache.maven.plugin.testing.junit5.MojoParameters;
import org.apache.maven.plugin.testing.junit5.MojoTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@MojoTest
public class ParametersMojoTest {

    private static final Logger logger = LoggerFactory.getLogger(ParametersMojoTest.class);

    private static final String DEFAULT_POM = "src/test/projects/default/pom.xml";

    private static final String EXPLICIT_POM = "src/test/projects/explicit/pom.xml";

    private static final String PROPERTY_POM = "src/test/projects/property/pom.xml";

    @Inject
    private Log log;

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = DEFAULT_POM)
    void testDefaultPom(ParametersMojo mojo) {
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = EXPLICIT_POM)
    void testExplicitPom(ParametersMojo mojo) {
        assertEquals("explicitValue", mojo.plain);
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = PROPERTY_POM)
    void testPropertyPom(ParametersMojo mojo) {
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = DEFAULT_POM)
    void simpleMojo(ParametersMojo mojo) {
        assertEquals(log, mojo.getLog());
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = DEFAULT_POM)
    @MojoParameter(name = "plain", value = "plainValue")
    @MojoParameter(name = "withDefault", value = "withDefaultValue")
    void simpleMojoWithParameters(ParametersMojo mojo) {
        assertEquals("plainValue", mojo.plain);
        assertEquals("withDefaultValue", mojo.withDefault);
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = DEFAULT_POM)
    @MojoParameters({
        @MojoParameter(name = "plain", value = "plainValue"),
        @MojoParameter(name = "withDefault", value = "withDefaultValue")
    })
    void simpleMojoWithParametersGroupingAnnotation(ParametersMojo mojo) {
        assertEquals("plainValue", mojo.plain);
        assertEquals("withDefaultValue", mojo.withDefault);
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = DEFAULT_POM)
    @MojoParameter(name = "plain", value = "plainValue")
    void simpleMojoWithParameter(ParametersMojo mojo) {
        assertEquals("plainValue", mojo.plain);
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    @MojoParameter(name = "plain", value = "plainValue")
    @InjectMojo(goal = "test:test-plugin:0.0.1-SNAPSHOT:parameters", pom = EXPLICIT_POM)
    void simpleMojoWithParameterInjectionWinsOverConfig(ParametersMojo mojo) {
        assertEquals("plainValue", mojo.plain);
        assertDoesNotThrow(mojo::execute);
    }
}
