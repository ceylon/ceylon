/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help.model;

import org.eclipse.ceylon.common.tool.ArgumentModel;
import org.eclipse.ceylon.common.tool.OptionModel;

public interface Visitor {

    public void start(Doc doc);

    public void end(Doc doc);

    public void visitAdditionalSection(DescribedSection describedSection);

    public void startOptions(OptionsSection optionsSection);

    public void visitOption(Option option);
    
    public void endOptions(OptionsSection optionsSection);
    
    public void visitSummary(SummarySection summarySection);

    public void startSynopses(SynopsesSection synopsesSection);
    
    public void startSynopsis(Synopsis synopsis);
    
    public void visitSynopsisOption(OptionModel<?> option);
    
    public void visitSynopsisArgument(ArgumentModel<?> option);
    
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option);
    
    public void endSynopsis(Synopsis synopsis);
    
    public void endSynopses(SynopsesSection synopsesSection);

    public void visitDescription(DescribedSection descriptionSection);

    



}
