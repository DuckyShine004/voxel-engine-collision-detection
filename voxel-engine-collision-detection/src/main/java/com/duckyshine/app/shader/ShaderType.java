package com.duckyshine.app.shader;

public enum ShaderType {
    WORLD("world");

    private final String type;

    private ShaderType(String type) {
        this.type = type;
    }

    public String get() {
        return this.type;
    }
}
