package com.gobrs.async.example.inteceptor;

import com.gobrs.async.callback.AsyncTaskExceptionInterceptor;
import com.gobrs.async.callback.ErrorCallback;
import org.springframework.stereotype.Component;

/**
 * @program: gobrs-async-core
 * @ClassName GobrsInterceptor
 * @description: 异常拦截器
 * @author: sizegang
 * @create: 2022-03-20
 **/
@Component
public class GobrsInterceptor implements AsyncTaskExceptionInterceptor {

    @Override
    public void exception(ErrorCallback errorCallback) {
        System.out.println("Execute global interceptor");
    }
}
