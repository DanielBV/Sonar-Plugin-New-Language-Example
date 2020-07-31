package org.sonar.mylanguage.rules;

import org.sonar.mylanguage.plugin.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the information that the rules use to check a file.
 * This is just an example plugin so it doesn't have a parser, it just contains the text
 * In a regular plugin it would hold the parse tree of a file and more info (symbol table, issues, etc)
 */
public class MyLanguageRuleContext {

    private List<Issue> issues;
    private String fileText;

    public List<Issue> getIssues() {
        return issues;
    }

    public String getFileText() {
        return fileText;
    }

    public MyLanguageRuleContext(String fileText){
        issues = new ArrayList<>();
        this.fileText = fileText;
    }

    public void addIssue(Issue issue){
        issues.add(issue);
    }

}
