package io.github.anjoismysign.blobcontainer.director.manager;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.configuration.ContainerConfiguration;
import io.github.anjoismysign.blobcontainer.director.ContainerManager;
import io.github.anjoismysign.blobcontainer.director.ContainerManagerDirector;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ContainerConfigurationManager extends ContainerManager {
    private ContainerConfiguration configuration;

    public ContainerConfigurationManager(ContainerManagerDirector managerDirector) {
        super(managerDirector);
        reload();
    }

    @Override
    public void reload() {
        BlobContainer plugin = getPlugin();
        plugin.saveResource("config.yml", false);
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        Constructor constructor = new Constructor(ContainerConfiguration.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            configuration = yaml.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public ContainerConfiguration getConfiguration() {
        return configuration;
    }
}