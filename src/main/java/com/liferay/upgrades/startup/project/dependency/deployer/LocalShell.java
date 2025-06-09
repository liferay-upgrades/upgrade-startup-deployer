package com.liferay.upgrades.startup.project.dependency.deployer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class LocalShell {

    public static void executeCommand(String command) throws IOException {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(command);

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();

        if (Objects.nonNull(inputStream)) {
            System.out.println("Executing modules deploy");

            String content = new String(inputStream.readAllBytes());

            System.out.printf(content);
        }
    }

}