package com.aiba.haimaelc.http;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ReturnCallBack<T> {

    private Type mType;

    public ReturnCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        } else {
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }
    }

    public abstract void onSuccess(int from, T object);

    public abstract void onError(int code, int from, String error);

    public Type getmType() {
        return mType;
    }
}