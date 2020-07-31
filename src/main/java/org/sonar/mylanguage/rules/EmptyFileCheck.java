package org.sonar.mylanguage.rules;


import org.sonar.check.Rule;
import org.sonar.mylanguage.plugin.Issue;
import org.sonar.mylanguage.plugin.MyLanguageRuleContext;

/**
 * A rule that checks that the file isn't empty
 */
@Rule(key = EmptyFileCheck.CHECK_KEY, name=EmptyFileCheck.NAME, description = EmptyFileCheck.HTML_DESCRIPTION)
public class EmptyFileCheck implements MyLanguageCheck {

    public static final String CHECK_KEY = "empty-file";
    public static final String NAME = "The file shouldn't be empty";
    public static final String HTML_DESCRIPTION = "This rule chekcs that the file isn't empty.";

    @Override
    public void scan(MyLanguageRuleContext context) {
        if(context.getFileText().trim().isEmpty()){
            context.addIssue(new Issue(this,1, "The file is empty"));
        }
    }
}
