package io.github.anjoismysign.blobcontainer.configuration;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.entity.Container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerConfiguration {
    public static ContainerConfiguration getInstance(){
        return BlobContainer.getInstance().getManagerDirector().getConfigurationManager().getConfiguration();
    }

    private boolean tinyDebug;
    private List<Container> containers;

    ContainerConfiguration(){}

    public boolean isTinyDebug() {
        return tinyDebug;
    }

    public void setTinyDebug(boolean tinyDebug) {
        this.tinyDebug = tinyDebug;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    /**
     * Converts the list of containers into a Map indexed by their identifier.
     *
     * @return A map where the key is the container identifier and the value is the Container object.
     */
    public Map<String, Container> getContainersByIdentifier() {
        Map<String, Container> map = new HashMap<>();
        if (containers != null) {
            for (Container container : containers) {
                if (container.getIdentifier() != null) {
                    map.put(container.getIdentifier(), container);
                }
            }
        }
        return map;
    }
}
