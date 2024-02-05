package io.jenkins.plugins.todeclarative.actions;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.model.Job;
import io.jenkins.plugins.prism.PrismConfiguration;
import io.jenkins.plugins.todeclarative.converter.api.ConverterRequest;
import io.jenkins.plugins.todeclarative.converter.api.ConverterResult;
import io.jenkins.plugins.todeclarative.converter.api.ToDeclarativeConverterListener;
import io.jenkins.plugins.todeclarative.converter.api.Warning;
import io.jenkins.plugins.todeclarative.converter.freestyle.FreestyleToDeclarativeConverter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.model.TransientActionFactory;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTPipelineDef;

public class ToDeclarativeAction implements Action, Describable<ToDeclarativeAction> {

    private FreeStyleProject job;

    private String jenkinsFile;

    private List<Warning> warnings;

    private Exception error;

    public ToDeclarativeAction(FreeStyleProject job) {
        this.job = job;
    }

    public String doConvert() throws Exception {
        job.checkPermission(Job.CONFIGURE);
        try {
            FreestyleToDeclarativeConverter converter = Jenkins.get()
                    .getExtensionList(FreestyleToDeclarativeConverter.class)
                    .get(0);
            ConverterRequest converterRequest = new ConverterRequest().job(job);
            ConverterResult converterResult = new ConverterResult().modelASTPipelineDef(new ModelASTPipelineDef(null));
            converter.convert(converterRequest, converterResult, job);
            this.jenkinsFile = converterResult.getModelASTPipelineDef().toPrettyGroovy();
            this.warnings = converterResult.getWarnings();
            ToDeclarativeConverterListener.fire(job, converterResult);
            return jenkinsFile;
        } catch (Exception e) {
            this.error = e;
        }
        return null;
    }

    public Exception getError() {
        return error;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    @CheckForNull
    @Override
    public String getIconFileName() {
        return "new-document.png";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "To Declarative";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "todeclarative";
    }

    public String getJenkinsFile() {
        return jenkinsFile;
    }

    public void setJenkinsFile(String jenkinsFile) {
        this.jenkinsFile = jenkinsFile;
    }

    @Override
    public Descriptor<ToDeclarativeAction> getDescriptor() {
        return Jenkins.get().getDescriptorOrDie(getClass());
    }

    @Extension
    public static class ActionInjector extends TransientActionFactory<FreeStyleProject> {
        @Override
        public Collection<ToDeclarativeAction> createFor(@NonNull FreeStyleProject p) {
            return Collections.singletonList(new ToDeclarativeAction(p));
        }

        @Override
        public Class type() {
            return FreeStyleProject.class;
        }
    }

    @Extension
    public static final class ToDeclarativeActionDescriptor extends Descriptor<ToDeclarativeAction> {
        // no op
        public PrismConfiguration getPrismConfiguration() {
            return PrismConfiguration.getInstance();
        }
    }
}
