package yanyu.xmz.recorder.business.dao;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;

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
