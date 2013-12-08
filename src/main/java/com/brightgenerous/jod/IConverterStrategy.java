package com.brightgenerous.jod;

@Deprecated
public interface IConverterStrategy {

    String getHost();

    Integer getPort();

    String getPipe();

    IInputResource getInputResource();

    Format getInputFormat();

    Format getOutputFormat();
}
