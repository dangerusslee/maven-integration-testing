/*
 * Copyright 2014 The Apache Software Foundation.
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
 */
package org.apache.maven.it;

import java.io.File;
import org.apache.maven.it.util.ResourceExtractor;

@Deprecated
public class MavenITmng2199DeprecatedParentVersionRangeTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2199DeprecatedParentVersionRangeTest()
    {
        // This test originally declared [3.2.2,) as the range although the project is not supported by that version.
        // The first version supporting such projects is Maven 3.3.0 and that is not by design. Commit
        // be3fb200326208ca4b8c41ebf16d5ae6b8049792 removed all version validation logic from local parent resolution.
        // This is the only reason this test succeeds with Maven >= 3.3.0. You could have left out the parent version
        // element completely and the test still succeeds with 3.3.0 <= Maven < 3.5.0. It never should have. Local
        // parent version validation logic had been re-added in 3.3.4, then reviewed in 3.3.6 (still supporting such
        // projects) and then finally made consistent to remote parent resolution in 3.5.0 which stops such projects
        // from being supported again as it was the case in any Maven version prior 3.3.0.
        super( "[3.3.0,3.3.9]" );
    }

    public void testBrokenProjectSilentlyProcessedUpToVerify()
        throws Exception
    {
        // This test isn't actually testing anything version range related.
        Verifier verifier = null;
        File testDir =
            ResourceExtractor.simpleExtractResources( getClass(), "/mng-2199-parent-version-range/local-parent" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.addCliOption( "-U" );
            verifier.setAutoclean( false );

            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

}
