/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.model;

import java.util.List;

/**
 * Name : JettyConfigModel
 * <br>
 * Description :
 * <br>
 *
 * Create by Mohammed ElAdly
 * <br>
 * Mail : mohammed.eladly@afaqy.com
 * @since 11/12/2019
 * <br>
 */
public class JettyConfigModel {
    private List<Integer> ports;
    private boolean enableSsl;
    private String scanPackage, microServiceName;
    private String certificatePath, keyStorePassword, keyManagerPassword;
    private String serverVersion, swaggerEnv;

    public boolean isEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getSwaggerEnv() {
        return swaggerEnv;
    }

    public void setSwaggerEnv(String swaggerEnv) {
        this.swaggerEnv = swaggerEnv;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public String getMicroServiceName() {
        return microServiceName;
    }

    public void setMicroServiceName(String microServiceName) {
        this.microServiceName = microServiceName;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyManagerPassword() {
        return keyManagerPassword;
    }

    public void setKeyManagerPassword(String keyManagerPassword) {
        this.keyManagerPassword = keyManagerPassword;
    }
}