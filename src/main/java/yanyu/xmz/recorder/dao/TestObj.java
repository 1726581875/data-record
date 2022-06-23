package yanyu.xmz.recorder.dao;

import yanyu.xmz.recorder.dao.annotation.DateAuto;
import yanyu.xmz.recorder.dao.annotation.Id;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/13
 */
public class TestObj {

    @Id
    private Long id;

    private String name;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

}
