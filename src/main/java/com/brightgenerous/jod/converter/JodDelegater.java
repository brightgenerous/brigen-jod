package com.brightgenerous.jod.converter;

import com.brightgenerous.jod.IConverter;
import com.brightgenerous.jod.IConverterStrategy;

@SuppressWarnings("deprecation")
interface JodDelegater {

    String getDefaultHost();

    Integer getDefaultPort();

    String getDefaultPipe();

    IConverter getConverter(IConverterStrategy strategy);
}
