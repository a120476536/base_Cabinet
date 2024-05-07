package com.linji.mylibrary.net.oss;

/**
 * Created by ding on 16-12-27.
 */

public class ResourceInfo implements Comparable<ResourceInfo> {

    /**
     * sourcePath : "/abc.png"
     * position : 0
     */
    private int position;
    private String resourcePath;

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int compareTo(ResourceInfo resourceInfo) {
        return this.position - resourceInfo.getPosition();
    }

}
