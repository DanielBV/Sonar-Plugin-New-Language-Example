package org.sonar.mylanguage.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.mylanguage.rules.EmptyFileCheck;


public class MyLanguageRuleRepository implements RulesDefinition {

    private static final String REPOSITORY_NAME = "MyLanguageRules";
    public static final String REPOSITORY_KEY = "my-language-repository";

    @Override
    public void define(Context context) {

        RulesDefinition.NewRepository repository = context
                .createRepository(REPOSITORY_KEY, MyLanguage.KEY)
                .setName(REPOSITORY_NAME);

        RulesDefinitionAnnotationLoader loader = new RulesDefinitionAnnotationLoader();
        List<Class> checks = new ArrayList<>();
        getChecks().forEach(checks::add);

        loader.load(repository, checks.toArray(new Class[0]));
        repository.done();
    }

    public static Iterable<Class> getChecks(){
        HashSet<Class> set = new HashSet<>();
        set.add(EmptyFileCheck.class);
        return set;
    }


}
