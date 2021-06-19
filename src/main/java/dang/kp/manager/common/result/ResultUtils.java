package dang.kp.manager.common.result;

import dang.kp.manager.common.IStatusMessage;

public class ResultUtils {
    public static Result success(Object data) {
        return Result.builder()
                .code(IStatusMessage.SystemStatus.SUCCESS.getCode())
                .msg(IStatusMessage.SystemStatus.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static Result fail(String msg) {
        return Result.builder()
                .code(IStatusMessage.SystemStatus.SUCCESS.getCode())
                .msg(msg)
                .build();
    }
}
