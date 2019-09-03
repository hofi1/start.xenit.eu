package eu.xenit.alfred.initializr.integration;

import eu.xenit.alfred.initializr.asserts.build.gradle.GradleMultiProjectAssert;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.Test;

public class AlfredGradleSdkTests extends BaseGeneratorTests {

    @Test
    public void testDefaultBuild() {
        ProjectRequest request = createProjectRequest();

        GradleMultiProjectAssert result = generateGradleBuild(request);

        result.rootGradleBuild()
                .contains("alfrescoVersion = '5.2.5'\n");

        result.platformBuildGradle()
                .hasPlugin("eu.xenit.alfresco")
                .hasPlugin("eu.xenit.amp")

                .hasDependency("alfrescoProvided", quote("org.alfresco:alfresco-repository:${alfrescoVersion}"));

    }
}