package org.sonar.mylanguage.plugin;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class MyLanguage extends AbstractLanguage {

    public static final String KEY = "ml"; // Usually this is the extension of the file of the language
    public static final String NAME = "MyLanguage";

    private static final String[] DEFAULT_FILE_SUFFIXES = { ".ml" };

    private Configuration configuration;

    public MyLanguage(Configuration configuration) {
        super(KEY, NAME);
        this.configuration = configuration;
    }

    @Override
    public String[] getFileSuffixes() {

        return MyLanguage.DEFAULT_FILE_SUFFIXES;
    }


}
