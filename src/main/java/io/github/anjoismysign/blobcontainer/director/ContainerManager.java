package io.github.anjoismysign.blobcontainer.director;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.bloblib.entities.GenericManager;

public class ContainerManager extends GenericManager<BlobContainer, ContainerManagerDirector> {
    public ContainerManager(ContainerManagerDirector managerDirector) {
        super(managerDirector);
    }
}