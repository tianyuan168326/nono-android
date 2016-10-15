package com.seki.noteasklite.DataUtil.Bean;

/**
 * Created by yuan-tian01 on 2016/3/9.
 */
public class UpdateInfo {

    /**
     * name : NONo
     * version : 1
     * changelog : First release
     * updated_at : 1457536481
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/56e03dd200fc7409cf000046?download_token=b680777240985b0f7de78478128b4b3d
     * install_url : http://download.fir.im/v2/app/install/56e03dd200fc7409cf000046?download_token=b680777240985b0f7de78478128b4b3d
     * direct_install_url : http://download.fir.im/v2/app/install/56e03dd200fc7409cf000046?download_token=b680777240985b0f7de78478128b4b3d
     * update_url : http://fir.im/vqlj
     * binary : {"fsize":18262486}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    /**
     * fsize : 18262486
     */

    private BinaryEntity binary;

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public void setBinary(BinaryEntity binary) {
        this.binary = binary;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getChangelog() {
        return changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public String getBuild() {
        return build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public BinaryEntity getBinary() {
        return binary;
    }

    public static class BinaryEntity {
        private int fsize;

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }

        public int getFsize() {
            return fsize;
        }
    }
}
