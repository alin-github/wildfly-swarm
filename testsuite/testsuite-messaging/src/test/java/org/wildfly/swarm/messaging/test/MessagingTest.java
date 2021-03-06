package org.wildfly.swarm.messaging.test;

import java.io.File;
import java.nio.file.Files;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.Test;
import org.wildfly.swarm.fractions.FractionUsageAnalyzer;
import org.wildfly.swarm.spi.api.JARArchive;

import static org.fest.assertions.Assertions.assertThat;

public class MessagingTest {

    @Test
    public void testFractionMatching() throws Exception {
        JARArchive archive = ShrinkWrap.create(JARArchive.class);
        archive.addClass(MyTopicMDB.class);
        FractionUsageAnalyzer analyzer = new FractionUsageAnalyzer();

        final File out = Files.createTempFile(archive.getName(), ".war").toFile();
        out.deleteOnExit();
        archive.as(ZipExporter.class).exportTo(out, true);

        analyzer.source(out);
        assertThat(analyzer.detectNeededFractions()
                           .stream()
                           .filter(fd -> fd.getArtifactId().equals("messaging"))
                           .count()).isEqualTo(1);
    }
}
