package io.jenkins.plugins.todeclarative.converter.api;

import hudson.model.Job;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTPipelineDef;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTTreeStep;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Result of a conversion of a {@link Job} to a declarative pipeline model {@link ModelASTPipelineDef}
 * A successful conversion can have some {@link #warnings}
 */
public class ConverterResult
{
    private ModelASTPipelineDef modelASTPipelineDef = new ModelASTPipelineDef( this );

    private List<Supplier<ModelASTTreeStep>> wrappingTreeSteps = new ArrayList<>();

    private List<String> convertedTypes = new ArrayList<>();

    private List<Warning> warnings = new ArrayList<>();

    public ConverterResult()
    {
        // no op
    }

    public ModelASTPipelineDef getModelASTPipelineDef()
    {
        return modelASTPipelineDef;
    }

    public void setModelASTPipelineDef( ModelASTPipelineDef modelASTPipelineDef )
    {
        this.modelASTPipelineDef = modelASTPipelineDef;
    }

    public ConverterResult modelASTPipelineDef( ModelASTPipelineDef modelASTPipelineDef )
    {
        this.modelASTPipelineDef = modelASTPipelineDef;
        return this;
    }

    public void addWrappingTreeStep( Supplier<ModelASTTreeStep> treeStep )
    {
        this.wrappingTreeSteps.add( treeStep );
    }

    /**
     * @return List of {@link ModelASTTreeStep} can be a Tree of withCredential, configFileProvider etc..
     * everything which need to wrap around builders
     */
    public List<Supplier<ModelASTTreeStep>> getWrappingTreeSteps()
    {
        return wrappingTreeSteps;
    }

    public List<String> getConvertedTypes() {
        return convertedTypes;
    }

    public void addConvertedType(String typeClassName) {
        convertedTypes.add(typeClassName);
    }

    public List<Warning> getWarnings()
    {
        return warnings;
    }

    public void addWarning( Warning warning )
    {
        this.warnings.add( warning );
    }

}
