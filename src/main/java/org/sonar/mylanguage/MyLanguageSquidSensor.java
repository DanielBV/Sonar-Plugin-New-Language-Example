package org.sonar.mylanguage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.mylanguage.plugin.MyLanguageRuleRepository;
import org.sonar.mylanguage.plugin.MyLanguage;
import org.sonar.mylanguage.plugin.MyLanguageScanner;
import org.sonar.mylanguage.rules.MyLanguageCheck;


public final class MyLanguageSquidSensor implements Sensor {

    private final Checks<MyLanguageCheck> checks;

    public MyLanguageSquidSensor(CheckFactory checkFactory) {
        this.checks = checkFactory
                .<MyLanguageCheck>create(MyLanguageRuleRepository.REPOSITORY_KEY)
                .addAnnotatedChecks(MyLanguageRuleRepository.getChecks());

    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(MyLanguage.KEY)
                .name("My Language Squid Sensor")
                .onlyOnFileType(Type.MAIN);
    }

    @Override
    public void execute(SensorContext context) {
        FilePredicates p = context.fileSystem().predicates();
        Iterable<InputFile> it = context.fileSystem().inputFiles(p.and(p.hasType(InputFile.Type.MAIN), p.hasLanguage(MyLanguage.KEY)));
        List<InputFile> list = new ArrayList<>();
        it.forEach(list::add);

        // You can extract parameter analysis here using context.config().get(PARAMETER) and inject them into the scanner
        List<InputFile> inputFiles = Collections.unmodifiableList(list);
        MyLanguageScanner scanner = new MyLanguageScanner(context, checks, inputFiles);
        scanner.scanFiles();
    }

}
