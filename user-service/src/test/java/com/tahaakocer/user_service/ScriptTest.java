package com.tahaakocer.user_service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptTest {
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        if (engine == null) {
            System.err.println("JavaScript motoru bulunamadı!");
            manager.getEngineFactories().forEach(factory ->
                    System.out.println("Factory: " + factory.getEngineName() + " - " + factory.getClass()));
        } else {
            System.out.println("JavaScript motoru bulundu: " + engine.getFactory().getEngineName());
            try {
                engine.eval("print('Hello from GraalVM JS');");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}