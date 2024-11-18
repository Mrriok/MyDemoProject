package com.zony.common.filenet.vo;

import com.zony.common.utils.PinyinCompareUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Comparator;

/**
 * 文件名和文件流处理对象
 *
 * @Author gubin
 * @Date 2020-04-20
 **/
public class FnAttachIsVo implements java.io.Serializable, Comparator<FnAttachIsVo> {

    private static final long serialVersionUID = 1L;

    private String id;//数据库唯一标识
    private String attachName;//文件名
    private String attachPath;//文件路径
    private InputStream is;//文件流

    public FnAttachIsVo() {

    }

    public FnAttachIsVo(String attachName, String attachPath) throws FileNotFoundException {
        this.attachName = attachName;
        this.attachPath = attachPath;
        this.is = new FileInputStream(attachPath);
    }

    @Override
    public int compare(FnAttachIsVo s1, FnAttachIsVo s2) {
        String o1 = s1.getAttachName();
        String o2 = s2.getAttachName();
        return PinyinCompareUtil.compare(o1, o2);
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }
}
