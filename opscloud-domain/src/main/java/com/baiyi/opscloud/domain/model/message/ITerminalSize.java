package com.baiyi.opscloud.domain.model.message;

/**
 * @Author baiyi
 * @Date 2021/12/24 9:28 PM
 * @Version 1.0
 */
public interface ITerminalSize {

    double W = 7.0;
    double H = 14.4166;

    int getWidth();

    int getHeight();

    default int getCols() {
        return (int) Math.floor(getWidth() / W);
    }

    default int getRows() {
        return (int) Math.floor(getHeight() / H);
    }

}
