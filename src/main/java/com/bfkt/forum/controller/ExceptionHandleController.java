package com.bfkt.forum.controller;

import com.bfkt.forum.common.Response;
import com.bfkt.forum.enums.ExceptionEnum;
import com.bfkt.forum.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandleController {
    private static final List<String> NOT_DISPLAY_LOG_CODE_LIST = List.of(
            ExceptionEnum.NOT_AUTH.getCode()
    );
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandleController.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> exceptionHandle(BizException e) {
        if (NOT_DISPLAY_LOG_CODE_LIST.contains(e.getCode())) {
            LOG.error("业务异常 : {}", e.getMessage(), e);
        }
        return Response.fail(e.getCode(), e.getMsg());
    }

    /**
     * json请求体读取异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> parameterExceptionHandler(HttpMessageNotReadableException e) {
        Throwable rootCause = e.getRootCause();
        String errorMsg;
        if (rootCause != null) {
            errorMsg = rootCause.getClass().getSimpleName() + "：" + e.getRootCause().getMessage();
        } else {
            errorMsg = e.getClass().getSimpleName() + "：" + e.getLocalizedMessage();
        }
        return Response.fail(ExceptionEnum.ERROR_CODE_02, errorMsg);
    }

    /**
     * 参数校验失败？
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.fail(ExceptionEnum.ERROR_CODE_01,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数数据绑定失败：1、字段属性值类型转换失败；2、字段属性值校验失败。
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> handleBindException(BindException e) {
        String msg;
        FieldError fieldError = e.getFieldError();
        if (fieldError != null) {
            if (fieldError.isBindingFailure()) {
                msg = String.format("参数【%s】类型不匹配，无法将【%s】转换成【%s】",
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        e.getFieldType(fieldError.getField()));
            } else {
                msg = fieldError.getDefaultMessage();
            }
        } else {
            List<ObjectError> allErrors = e.getAllErrors();
            ObjectError error = allErrors.get(0);
            msg = error.getDefaultMessage();
        }
        return Response.fail(ExceptionEnum.ERROR_CODE_03, msg);
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String msg = String.format("参数%s类型不匹配，无法将【%s】转换成%s",
                e.getParameter().getParameterName(),
                e.getValue(),
                Objects.requireNonNull(e.getRequiredType()).getName());
        return Response.fail(ExceptionEnum.ERROR_CODE_04, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        List<String> msgList = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            msgList.add(fieldName + "：" + errorMessage);
        });
        return Response.fail(ExceptionEnum.ERROR_CODE_06, String.join("；", msgList));
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> exceptionHandler(Exception e) {
        LOG.error(ExceptionEnum.ERROR_CODE_05.getDesc(e.getMessage()), e);
        return Response.fail(ExceptionEnum.ERROR_CODE_05, e.getClass().getSimpleName() + "：" + e.getMessage());
    }
}
