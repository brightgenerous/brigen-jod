package com.brightgenerous.jod;

public enum Format {

    PDF("pdf", "application/pdf"),

    SWF("swf", "application/x-shockwave-flash"),

    XHTML("xhtml", "application/xhtml+xml"),

    HTML("html", "text/html"),

    ODT("odt", "application/vnd.oasis.opendocument.text"),

    SXW("sxw", "application/vnd.sun.xml.writer"),

    RTF("rtf", "text/rtf"),

    WPD("wpd", "application/wordperfect"),

    WIKI("wiki", "text/x-wiki"),

    DOC("doc", "application/msword"),

    ODS("ods", "application/vnd.oasis.opendocument.spreadsheet"),

    SXC("sxc", "application/vnd.sun.xml.calc"),

    XLS("xls", "application/vnd.ms-excel"),

    CSV("csv", "text/csv"),

    TSV("tsv", "text/tab-separated-values"),

    ODP("odp", "application/vnd.oasis.opendocument.presentation"),

    SXI("sxi", "application/vnd.sun.xml.impress"),

    PPT("ppt", "application/vnd.ms-powerpoint"),

    ODG("odg", "application/vnd.oasis.opendocument.graphics"),

    SVG("svg", "image/svg+xml");

    private String extension;

    private String mimeType;

    private Format(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Format getByExtension(String fileName) {
        if ((fileName != null) && !fileName.isEmpty() && !fileName.endsWith(".")) {
            String ext = fileName;
            {
                int idx = ext.indexOf('.');
                if (idx != -1) {
                    ext = ext.substring(idx + 1);
                }
            }
            ext = ext.toLowerCase();
            for (Format format : Format.values()) {
                if (format.getExtension().equals(ext)) {
                    return format;
                }
            }
        }
        return null;
    }
}
