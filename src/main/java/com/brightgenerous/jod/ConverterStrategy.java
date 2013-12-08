package com.brightgenerous.jod;

@SuppressWarnings("deprecation")
class ConverterStrategy implements IConverterStrategy {

    private final String host;

    private final Integer port;

    private final String pipe;

    private final IInputResource inputResource;

    private final Format inputFormat;

    private final Format outputFormat;

    public ConverterStrategy(String host, Integer port, IInputResource inputResource,
            Format inputFormat, Format outputFormat) {
        this.host = host;
        this.port = port;
        pipe = null;
        this.inputResource = inputResource;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
    }

    public ConverterStrategy(String pipe, IInputResource inputResource, Format inputFormat,
            Format outputFormat) {
        host = null;
        port = null;
        this.pipe = pipe;
        this.inputResource = inputResource;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getPipe() {
        return pipe;
    }

    @Override
    public IInputResource getInputResource() {
        return inputResource;
    }

    @Override
    public Format getInputFormat() {
        return inputFormat;
    }

    @Override
    public Format getOutputFormat() {
        return outputFormat;
    }
}
