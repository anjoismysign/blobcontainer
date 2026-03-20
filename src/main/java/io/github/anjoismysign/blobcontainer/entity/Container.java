package io.github.anjoismysign.blobcontainer.entity;

public class Container {
    private String identifier;
    private String blobInventory;
    private String button;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBlobInventory() {
        return blobInventory;
    }

    public void setBlobInventory(String blobInventory) {
        this.blobInventory = blobInventory;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}
