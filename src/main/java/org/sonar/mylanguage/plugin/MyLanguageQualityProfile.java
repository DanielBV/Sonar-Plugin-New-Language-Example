package org.sonar.mylanguage.plugin;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.mylanguage.plugin.MyLanguageRuleRepository;
import org.sonar.mylanguage.plugin.MyLanguage;
import org.sonar.mylanguage.rules.EmptyFileCheck;

public final class MyLanguageQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("My Language Quality Profile", MyLanguage.KEY);
        profile.setDefault(true);

        profile.activateRule(MyLanguageRuleRepository.REPOSITORY_KEY, EmptyFileCheck.CHECK_KEY);

        profile.done();
    }

}
