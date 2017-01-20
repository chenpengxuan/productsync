package com.ymatou.productsync.infrastructure.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyifan on 2016/12/20.
 */
@Component
@DisconfFile(fileName = "custom.properties")
public class CustomProps {

    private String selectFieldSql;

    private String selectIdsSql;

    @DisconfFileItem(name = "selectFieldSql")
    public String getSelectFieldSql() {
        return selectFieldSql;
    }

    public void setSelectFieldSql(String selectFieldSql) {
        this.selectFieldSql = selectFieldSql;
    }

    @DisconfFileItem(name = "selectIdsSql")
    public String getSelectIdsSql() {
        return selectIdsSql;
    }

    public void setSelectIdsSql(String selectIdsSql) {
        this.selectIdsSql = selectIdsSql;
    }
}
