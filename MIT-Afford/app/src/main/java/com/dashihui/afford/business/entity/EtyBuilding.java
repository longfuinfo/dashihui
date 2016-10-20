package com.dashihui.afford.business.entity;

import com.dashihui.afford.util.number.UtilNumber;

import java.util.List;

/**
 * 栋楼
 * Created by NiuFC on 2015/11/24.
 */
public class EtyBuilding {


    /**
     * CODE : 0100000
     * PARENTID : 0
     * ID : 6
     * TYPE : 1
     * NAME : 1#
     * CHILDREN : [{"CODE":"0110000","PARENTID":6,"ID":9,"TYPE":2,"NAME":"1单元","CHILDREN":[{"CODE":"0110101","PARENTID":9,"ID":11,"TYPE":3,"NAME":"101"},{"CODE":"0110102","PARENTID":9,"ID":12,"TYPE":3,"NAME":"102"}]},{"CODE":"0120000","PARENTID":6,"ID":10,"TYPE":2,"NAME":"2单元","CHILDREN":[{"CODE":"0120101","PARENTID":10,"ID":13,"TYPE":3,"NAME":"101"},{"CODE":"0120102","PARENTID":10,"ID":14,"TYPE":3,"NAME":"102"}]}]
     */

    private String CODE;
    private int PARENTID;
    private int ID;
    private int TYPE;
    private String NAME;
    /**
     * CODE : 0110000
     * PARENTID : 6
     * ID : 9
     * TYPE : 2
     * NAME : 1单元
     * CHILDREN : [{"CODE":"0110101","PARENTID":9,"ID":11,"TYPE":3,"NAME":"101"},{"CODE":"0110102","PARENTID":9,"ID":12,"TYPE":3,"NAME":"102"}]
     */

    private List<ChildrenEntity> CHILDREN;

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public void setPARENTID(int PARENTID) {
        this.PARENTID = PARENTID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setCHILDREN(List<ChildrenEntity> CHILDREN) {
        this.CHILDREN = CHILDREN;
    }

    public String getCODE() {
        return CODE;
    }

    public int getPARENTID() {
        return PARENTID;
    }

    public int getID() {
        return ID;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public List<ChildrenEntity> getCHILDREN() {
        return CHILDREN;
    }

    /**
     * 单元
     */
    public static class ChildrenEntity {
        private String CODE;
        private int PARENTID;
        private int ID;
        private int TYPE;
        private String NAME;
        /**
         * CODE : 0110101
         * PARENTID : 9
         * ID : 11
         * TYPE : 3
         * NAME : 101
         */

        private List<CHILDRENEnty> CHILDREN;

        public void setCODE(String CODE) {
            this.CODE = CODE;
        }

        public void setPARENTID(int PARENTID) {
            this.PARENTID = PARENTID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setTYPE(int TYPE) {
            this.TYPE = TYPE;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public void setCHILDREN(List<CHILDRENEnty> CHILDREN) {
            this.CHILDREN = CHILDREN;
        }

        public String getCODE() {
            return CODE;
        }

        public int getPARENTID() {
            return PARENTID;
        }

        public int getID() {
            return ID;
        }

        public int getTYPE() {
            return TYPE;
        }

        public String getNAME() {
            return NAME;
        }

        public List<CHILDRENEnty> getCHILDREN() {
            return CHILDREN;
        }

        /**
         * 门牌号
         */
        public static class CHILDRENEnty {
            private String CODE;
            private int PARENTID;
            private int ID;
            private int TYPE;
            private String NAME;
            public CHILDRENEnty() {
                super();
            }
            public CHILDRENEnty(String name, String ID) {
                super();
                this.NAME = name;
                this.ID = UtilNumber.IntegerValueOf(ID);
            }

            public void setCODE(String CODE) {
                this.CODE = CODE;
            }

            public void setPARENTID(int PARENTID) {
                this.PARENTID = PARENTID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public void setTYPE(int TYPE) {
                this.TYPE = TYPE;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public String getCODE() {
                return CODE;
            }

            public int getPARENTID() {
                return PARENTID;
            }

            public int getID() {
                return ID;
            }

            public int getTYPE() {
                return TYPE;
            }

            public String getNAME() {
                return NAME;
            }
        }
    }
}
