package org.sonar.mylanguage.plugin;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.mylanguage.rules.MyLanguageCheck;
import org.sonar.mylanguage.rules.MyLanguageRuleContext;

import java.io.IOException;
import java.util.List;

public class MyLanguageScanner {


    private final SensorContext context;
    private final List<InputFile> inputFiles;
    private final Checks<MyLanguageCheck> checks;


    protected static Logger LOG = Loggers.get(MyLanguageScanner.class);

    public MyLanguageScanner(SensorContext context, Checks<MyLanguageCheck> checks, List<InputFile> inputFiles) {
        this.context = context;
        this.checks = checks;
        this.inputFiles = inputFiles;
    }

    public void scanFiles() {
        for (InputFile mylanguageFile : inputFiles) {
            if (context.isCancelled()) {
                return;
            }
            try {
                scanFile(mylanguageFile);
            } catch (Exception e) {
                LOG.error("Error scanning the file", e );
            }
        }

    }

    private void scanFile(InputFile inputFile) {
        MyLanguageRuleContext ruleContext = null;

        // Here you should parse the file and pass the tree to the ruleContext
        // (Or you can create a symbol table and pass it to the ruleContext)
        String content;
        try {
            content = inputFile.contents();
            ruleContext = new MyLanguageRuleContext(content);
        } catch (IOException e) {
            LOG.error("Error reading from the file" + inputFile.filename() +".", e);
            return;
        }

        for (MyLanguageCheck check : checks.all())
            check.scan(ruleContext);

        saveIssues(inputFile, ruleContext.getIssues());

        // This is necessary so that the project panel doesn't show the message "The main branch has no lines of code"
        // It considers every line a line of code. If the language has comments it could be changed to show only lines
        // of code.
        int lines = content.split("[\r\n]+").length;
        context.<Integer>newMeasure().forMetric(CoreMetrics.NCLOC).on(inputFile).withValue(lines).save();

    }

    private void saveIssues(InputFile inputFile, List<Issue> issues) {
        for (Issue preciseIssue : issues) {
            RuleKey ruleKey = checks.ruleKey(preciseIssue.getCheck());
            NewIssue newIssue = context
                    .newIssue()
                    .forRule(ruleKey);


            TextRange range = inputFile.selectLine(preciseIssue.getLine());


            NewIssueLocation newLocation = newIssue.newLocation()
                    .on(inputFile);
            newLocation.message(preciseIssue.getMessage());
            newLocation.at(range);
            newIssue.at(newLocation);

            newIssue.save();
        }
    }


}
