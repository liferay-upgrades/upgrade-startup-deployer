package com.liferay.upgrades.startup.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.liferay.upgrades.startup.project.dependency.deployer.LocalShell;
import com.liferay.upgrades.startup.project.dependency.deployer.ModuleDeployer;

import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        try {
            DeployerOptions deployerOptions = _resolveOptions(args);

            if (!deployerOptions.directory.isEmpty()) {
                ModuleDeployer moduleDeployer = new ModuleDeployer();

                String script = moduleDeployer.scriptFactory(
                    Paths.get(deployerOptions.directory + "/build.gradle"));

                LocalShell.executeCommand(script);
            }

        } catch (Exception exception) {
            if (exception instanceof ParameterException) {
                log.info(_generateMDOptionsHelp());
            }
            else throw new RuntimeException(exception);
        }
    }

    private static String _generateMDOptionsHelp() {
        return "The available option is:\n" +
                "\t--folder or -f to specify the path for the liferay workspace (Required)\n" + "ie. -f /path/to/workspace/modules/lorem-ipsum-module";
    }

    private static DeployerOptions _resolveOptions(String[] args) {
        DeployerOptions deployerOptions = new DeployerOptions();

        JCommander jCommander = JCommander.newBuilder()
                .addObject(deployerOptions)
                .build();

        jCommander.parse(args);

        return deployerOptions;
    }

    private static class DeployerOptions {
        @Parameter(
                names = {"-f", "--folder"},
                description = "Specify the path for the liferay workspace",
                required = true
        )
        String directory;
    }

    private static final Logger log = Logger.getLogger(Main.class.getName());
}
