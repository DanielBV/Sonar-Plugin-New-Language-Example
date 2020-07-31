package org.sonar.mylanguage.rules;

import org.sonar.mylanguage.plugin.MyLanguageRuleContext;

public interface MyLanguageCheck{
    void scan(MyLanguageRuleContext context);
}
