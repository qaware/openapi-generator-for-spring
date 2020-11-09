package de.qaware.openapigeneratorforspring.ui.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStreamReader;

public class MustacheTemplate {

    private final Template template;

    public MustacheTemplate(String templateClassPath) {
        InputStreamReader reader = new InputStreamReader(MustacheTemplate.class.getResourceAsStream(templateClassPath));
        template = Mustache.compiler().nullValue("").escapeHTML(false).compile(reader);
    }

    public String execute(Object context) {
        return template.execute(context);
    }

}
