package com.zwc.gateway.commoms;

import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 自定义异常处理
 * @author 赵育冬
 */
//@Component
public class ExtensionErrorAttributes implements ErrorAttributes {
    private static final String ERROR_ATTRIBUTE = ExtensionErrorAttributes.class.getName() + ".ERROR";
    private final boolean includeException;

    public ExtensionErrorAttributes() {
        this(false);
    }

    public ExtensionErrorAttributes(boolean includeException) {
        this.includeException = includeException;
    }

    /**
     * 在当前方法中实现异常信息
     * @param request
     * @param includeStackTrace
     * @return
     */
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.path());
        Throwable error = this.getError(request);
        HttpStatus errorStatus = this.determineHttpStatus(error);
        //永远返回status 为200
        errorAttributes.put("status", 200);
        //抛出的异常code
        errorAttributes.put("code", errorStatus.value());
        //自定义的 异常内容
        errorAttributes.put("message", determineMessage(error));
        handleException(errorAttributes, determineException(error), includeStackTrace);
        return errorAttributes;
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        return error instanceof ResponseStatusException ? ((ResponseStatusException)error).getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable error) {
        if (error instanceof WebExchangeBindException) {
            return error.getMessage();
        } else {
            return error instanceof ResponseStatusException ? ((ResponseStatusException)error).getReason() : error.getMessage();
        }
    }

    private Throwable determineException(Throwable error) {
        if (error instanceof ResponseStatusException) {
            return error.getCause() != null ? error.getCause() : error;
        } else {
            return error;
        }
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void handleException(Map<String, Object> errorAttributes, Throwable error, boolean includeStackTrace) {
        if (this.includeException) {
            errorAttributes.put("exception", error.getClass().getName());
        }

        if (includeStackTrace) {
            this.addStackTrace(errorAttributes, error);
        }

        if (error instanceof BindingResult) {
            BindingResult result = (BindingResult)error;
            if (result.getErrorCount() > 0) {
                errorAttributes.put("errors", result.getAllErrors());
            }
        }

    }

    @Override
    public Throwable getError(ServerRequest request) {
        return (Throwable)request.attribute(ERROR_ATTRIBUTE).orElseThrow(() -> {
            return new IllegalStateException("Missing exception attribute in ServerWebExchange");
        });
    }

    @Override
    public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
        exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE, error);
    }
}
