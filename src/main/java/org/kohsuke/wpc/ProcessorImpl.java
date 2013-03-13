package org.kohsuke.wpc;

import org.kohsuke.MetaInfServices;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_6;

/**
 * @author Kohsuke Kawaguchi
 */
@SuppressWarnings("Since15")
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(RELEASE_6)
@MetaInfServices(Processor.class)
public class ProcessorImpl extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Elements elements = processingEnv.getElementUtils();
        for (Element e : roundEnv.getRootElements()) {
            PackageElement p = elements.getPackageOf(e);
            String[] tokens = p.getQualifiedName().toString().split("\\.");
            for (String t : tokens) {
                if (RESERVED_WORDS.contains(t.toUpperCase(Locale.ENGLISH))) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, 
                            "Package name "+p.getQualifiedName()+" contains "+t+", which is a reserved word in Windows. See http://support.microsoft.com/kb/74496/en-us",e);
                }
            }
        }
        return false;
    }
    
    // taken from http://support.microsoft.com/kb/74496/en-us
    private static final Set<String> RESERVED_WORDS = new HashSet<String>(Arrays.asList(
            "CON","PRN","AUX","CLOCK$","NUL",
            "COM1","COM2","COM3","COM4",
            "LPT1","LPT2","LPT3"
    ));
}
