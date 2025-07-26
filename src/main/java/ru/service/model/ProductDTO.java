package ru.service.model;

import java.util.UUID;

public class ProductDTO {
    private String name;
    private UUID id;
    private String text;

    public ProductDTO() {
    }

    public ProductDTO(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
