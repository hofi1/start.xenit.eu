package eu.xenit.alfred.initializr.integration.extensions;

import eu.xenit.alfred.initializr.asserts.build.gradle.GradleMultiProjectAssert;
import eu.xenit.alfred.initializr.asserts.docker.DockerComposeAssert;
import eu.xenit.alfred.initializr.asserts.docker.DockerComposeProjectAssert;

import eu.xenit.alfred.initializr.generator.extensions.alfred.telemetry.AlfredTelemetryProjectGenerationConfiguration;
import eu.xenit.alfred.initializr.integration.BaseGeneratorTests;
import io.spring.initializr.web.project.ProjectRequest;
import org.junit.Test;

public class AlfredTelemetryTests extends BaseGeneratorTests {

    @Test
    public void testAlfredTelemetryGradleDependencies() {
        ProjectRequest request = createProjectRequest("alfred-telemetry");

        GradleMultiProjectAssert result = generateGradleBuild(request);

        String telemetryVersion = AlfredTelemetryProjectGenerationConfiguration.ALFRED_TELEMETRY_VERSION;
        String micrometerVersion = AlfredTelemetryProjectGenerationConfiguration.MICROMETER_VERSION;

        result.rootGradleBuild()
                .hasDependency("alfrescoAmp",
                        quote("eu.xenit.alfred.telemetry:alfred-telemetry-platform:${alfredTelemetryVersion}@amp"))
                .contains("alfredTelemetryVersion = '" + telemetryVersion + "'")
                .contains("micrometerVersion = '" + micrometerVersion + "'");

        result.platformGradleBuild()
                .hasDependency("alfrescoProvided", quote("io.micrometer:micrometer-core:${micrometerVersion}"));
    }

    @Test
    public void dockerComposeGrafanaTests() {
        ProjectRequest request = createProjectRequest("micrometer-graphite");

        DockerComposeProjectAssert composeProject = generateCompose(request);

        // Expecting 2 compose files
        composeProject.assertSize(2);

        // Check the -grafana compose file
        DockerComposeAssert composeGrafana = composeProject.assertDockerCompose("grafana");

        composeGrafana
                .isNotBlank()
                .startsWith("version:")
                .hasService("grafana");

    }
}
