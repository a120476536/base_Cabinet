package com.linji.mylibrary.mqtt;

import java.util.ArrayList;
import java.util.List;

public class MQTTMsgBean {
    private String commandType;
    private MqttData data;

    public String getCommandType() {
        return commandType == null ? "" : commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public MqttData getData() {
        return data;
    }

    public void setData(MqttData data) {
        this.data = data;
    }

    public class MqttData {
        private String versionNo;
        private String fileAddress;
        private List<Box> lockAddressList;
        private String param;
        private String userName;

        public List<Box> getLockAddressList() {
            if (lockAddressList == null) {
                return new ArrayList<>();
            }
            return lockAddressList;
        }

        public void setLockAddressList(List<Box> lockAddressList) {
            this.lockAddressList = lockAddressList;
        }

        public String getUserName() {
            return userName == null ? "" : userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }


        public String getVersionNo() {
            return versionNo == null ? "" : versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public String getFileAddress() {
            return fileAddress == null ? "" : fileAddress;
        }

        public void setFileAddress(String fileAddress) {
            this.fileAddress = fileAddress;
        }

        public class Box {

            private Integer lockPlateAddress;
            private Integer lockNo;
            private Integer name;

            public Integer getName() {
                return name;
            }

            public void setName(Integer name) {
                this.name = name;
            }

            public Integer getLockPlateAddress() {
                return lockPlateAddress;
            }

            public void setLockPlateAddress(Integer lockPlateAddress) {
                this.lockPlateAddress = lockPlateAddress;
            }

            public Integer getLockNo() {
                return lockNo;
            }

            public void setLockNo(Integer lockNo) {
                this.lockNo = lockNo;
            }
        }
    }
}
