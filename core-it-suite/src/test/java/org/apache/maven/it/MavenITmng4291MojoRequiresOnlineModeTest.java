package org.apache.maven.it;

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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4291">MNG-4291</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4291MojoRequiresOnlineModeTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4291MojoRequiresOnlineModeTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test that the mojo annotation @requiresOnline is recognized. For a direct mojo invocation, this means to fail
     * when Maven is in offline mode but the mojo requires online model.
     */
    public void testitDirectInvocation()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4291" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.setLogFileName( "log-direct.txt" );
        verifier.getCliOptions().add( "--offline" );
        try
        {
            verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-online:2.1-SNAPSHOT:touch" );
            verifier.verifyErrorFreeLog();
            fail( "Invalid packaging of parent POM did not fail the build." );
        }
        catch ( VerificationException e )
        {
            // expected
        }
        finally
        {
            verifier.resetStreams();
        }
    }

    /**
     * Test that the mojo annotation @requiresOnline is recognized. For a mojo invocation bound to a lifecycle phase,
     * this means to skip the mojo when Maven is in offline mode but the mojo requires online model.
     */
    public void testitLifecycleInvocation()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4291" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.setLogFileName( "log-lifecycle.txt" );
        verifier.getCliOptions().add( "--offline" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertFileNotPresent( "target/touch.txt" );
    }

}