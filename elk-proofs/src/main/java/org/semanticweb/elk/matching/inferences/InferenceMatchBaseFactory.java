package org.semanticweb.elk.matching.inferences;

/*
 * #%L
 * ELK Proofs Package
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.semanticweb.elk.matching.conclusions.BackwardLinkMatch1;
import org.semanticweb.elk.matching.conclusions.BackwardLinkMatch2;
import org.semanticweb.elk.matching.conclusions.ClassInconsistencyMatch1;
import org.semanticweb.elk.matching.conclusions.DisjointSubsumerMatch1;
import org.semanticweb.elk.matching.conclusions.DisjointSubsumerMatch2;
import org.semanticweb.elk.matching.conclusions.ForwardLinkMatch1;
import org.semanticweb.elk.matching.conclusions.ForwardLinkMatch2;
import org.semanticweb.elk.matching.conclusions.ForwardLinkMatch3;
import org.semanticweb.elk.matching.conclusions.IndexedDisjointClassesAxiomMatch1;
import org.semanticweb.elk.matching.conclusions.IndexedDisjointClassesAxiomMatch2;
import org.semanticweb.elk.matching.conclusions.IndexedEquivalentClassesAxiomMatch1;
import org.semanticweb.elk.matching.conclusions.IndexedEquivalentClassesAxiomMatch2;
import org.semanticweb.elk.matching.conclusions.IndexedObjectPropertyRangeAxiomMatch1;
import org.semanticweb.elk.matching.conclusions.IndexedObjectPropertyRangeAxiomMatch2;
import org.semanticweb.elk.matching.conclusions.IndexedSubClassOfAxiomMatch1;
import org.semanticweb.elk.matching.conclusions.IndexedSubClassOfAxiomMatch2;
import org.semanticweb.elk.matching.conclusions.IndexedSubObjectPropertyOfAxiomMatch1;
import org.semanticweb.elk.matching.conclusions.IndexedSubObjectPropertyOfAxiomMatch2;
import org.semanticweb.elk.matching.conclusions.PropagationMatch1;
import org.semanticweb.elk.matching.conclusions.PropertyRangeMatch1;
import org.semanticweb.elk.matching.conclusions.PropertyRangeMatch2;
import org.semanticweb.elk.matching.conclusions.SubClassInclusionComposedMatch1;
import org.semanticweb.elk.matching.conclusions.SubClassInclusionDecomposedMatch1;
import org.semanticweb.elk.matching.conclusions.SubClassInclusionDecomposedMatch2;
import org.semanticweb.elk.matching.conclusions.SubPropertyChainMatch1;
import org.semanticweb.elk.matching.conclusions.SubPropertyChainMatch2;
import org.semanticweb.elk.reasoner.indexing.model.ElkClassAssertionAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDifferentIndividualsAxiomBinaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDifferentIndividualsAxiomNaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointClassesAxiomBinaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointClassesAxiomNaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointUnionAxiomBinaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointUnionAxiomEquivalenceConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointUnionAxiomNaryConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointUnionAxiomOwlNothingConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkDisjointUnionAxiomSubClassConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkEquivalentClassesAxiomEquivalenceConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkEquivalentClassesAxiomSubClassConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkEquivalentObjectPropertiesAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkObjectPropertyAssertionAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkObjectPropertyDomainAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkObjectPropertyRangeAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkReflexiveObjectPropertyAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkSameIndividualAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkSubClassOfAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkSubObjectPropertyOfAxiomConversion;
import org.semanticweb.elk.reasoner.indexing.model.ElkTransitiveObjectPropertyAxiomConversion;
import org.semanticweb.elk.reasoner.saturation.inferences.BackwardLinkComposition;
import org.semanticweb.elk.reasoner.saturation.inferences.BackwardLinkOfObjectHasSelf;
import org.semanticweb.elk.reasoner.saturation.inferences.BackwardLinkOfObjectSomeValuesFrom;
import org.semanticweb.elk.reasoner.saturation.inferences.BackwardLinkReversed;
import org.semanticweb.elk.reasoner.saturation.inferences.BackwardLinkReversedExpanded;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyOfDisjointSubsumers;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyOfObjectComplementOf;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyOfOwlNothing;
import org.semanticweb.elk.reasoner.saturation.inferences.ClassInconsistencyPropagated;
import org.semanticweb.elk.reasoner.saturation.inferences.DisjointSubsumerFromSubsumer;
import org.semanticweb.elk.reasoner.saturation.inferences.ForwardLinkComposition;
import org.semanticweb.elk.reasoner.saturation.inferences.ForwardLinkOfObjectHasSelf;
import org.semanticweb.elk.reasoner.saturation.inferences.ForwardLinkOfObjectSomeValuesFrom;
import org.semanticweb.elk.reasoner.saturation.inferences.PropagationGenerated;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionComposedDefinedClass;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionComposedEntity;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionComposedObjectIntersectionOf;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionComposedObjectSomeValuesFrom;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionComposedObjectUnionOf;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionDecomposedFirstConjunct;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionDecomposedSecondConjunct;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionExpandedDefinition;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionExpandedFirstEquivalentClass;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionExpandedSecondEquivalentClass;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionExpandedSubClassOf;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionObjectHasSelfPropertyRange;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionOwlThing;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionRange;
import org.semanticweb.elk.reasoner.saturation.inferences.SubClassInclusionTautology;
import org.semanticweb.elk.reasoner.saturation.properties.inferences.PropertyRangeInherited;
import org.semanticweb.elk.reasoner.saturation.properties.inferences.SubPropertyChainExpandedSubObjectPropertyOf;
import org.semanticweb.elk.reasoner.saturation.properties.inferences.SubPropertyChainTautology;

public class InferenceMatchBaseFactory implements InferenceMatch.Factory {

	@Override
	public BackwardLinkCompositionMatch1 getBackwardLinkCompositionMatch1(
			BackwardLinkComposition parent,
			BackwardLinkMatch1 conclusionMatch) {
		return new BackwardLinkCompositionMatch1(parent, conclusionMatch);
	}

	@Override
	public BackwardLinkCompositionMatch2 getBackwardLinkCompositionMatch2(
			BackwardLinkCompositionMatch1 parent,
			IndexedSubObjectPropertyOfAxiomMatch2 fifthPremiseMatch) {
		return new BackwardLinkCompositionMatch2(parent, fifthPremiseMatch);
	}

	@Override
	public BackwardLinkCompositionMatch3 getBackwardLinkCompositionMatch3(
			BackwardLinkCompositionMatch2 parent,
			BackwardLinkMatch2 firstPremiseMatch) {
		return new BackwardLinkCompositionMatch3(parent, firstPremiseMatch);
	}

	@Override
	public BackwardLinkCompositionMatch4 getBackwardLinkCompositionMatch4(
			BackwardLinkCompositionMatch3 parent,
			SubPropertyChainMatch2 fourthPremiseMatch) {
		return new BackwardLinkCompositionMatch4(parent, fourthPremiseMatch);
	}

	@Override
	public BackwardLinkCompositionMatch5 getBackwardLinkCompositionMatch5(
			BackwardLinkCompositionMatch4 parent,
			ForwardLinkMatch3 thirdPremiseMatch) {
		return new BackwardLinkCompositionMatch5(parent, thirdPremiseMatch);
	}

	@Override
	public BackwardLinkOfObjectHasSelfMatch1 getBackwardLinkOfObjectHasSelfMatch1(
			BackwardLinkOfObjectHasSelf parent,
			BackwardLinkMatch1 conclusionMatch) {
		return new BackwardLinkOfObjectHasSelfMatch1(parent, conclusionMatch);
	}

	@Override
	public BackwardLinkOfObjectHasSelfMatch2 getBackwardLinkOfObjectHasSelfMatch2(
			BackwardLinkOfObjectHasSelfMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new BackwardLinkOfObjectHasSelfMatch2(parent, premiseMatch);
	}

	@Override
	public BackwardLinkOfObjectSomeValuesFromMatch1 getBackwardLinkOfObjectSomeValuesFromMatch1(
			BackwardLinkOfObjectSomeValuesFrom parent,
			BackwardLinkMatch1 conclusionMatch) {
		return new BackwardLinkOfObjectSomeValuesFromMatch1(parent,
				conclusionMatch);
	}

	@Override
	public BackwardLinkOfObjectSomeValuesFromMatch2 getBackwardLinkOfObjectSomeValuesFromMatch2(
			BackwardLinkOfObjectSomeValuesFromMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new BackwardLinkOfObjectSomeValuesFromMatch2(parent,
				premiseMatch);
	}

	@Override
	public BackwardLinkReversedExpandedMatch1 getBackwardLinkReversedExpandedMatch1(
			BackwardLinkReversedExpanded parent,
			BackwardLinkMatch1 conclusionMatch) {
		return new BackwardLinkReversedExpandedMatch1(parent, conclusionMatch);
	}

	@Override
	public BackwardLinkReversedExpandedMatch2 getBackwardLinkReversedExpandedMatch2(
			BackwardLinkReversedExpandedMatch1 parent,
			IndexedSubObjectPropertyOfAxiomMatch2 secondPremiseMatch) {
		return new BackwardLinkReversedExpandedMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public BackwardLinkReversedExpandedMatch3 getBackwardLinkReversedExpandedMatch3(
			BackwardLinkReversedExpandedMatch2 parent,
			ForwardLinkMatch3 firstPremiseMatch) {
		return new BackwardLinkReversedExpandedMatch3(parent,
				firstPremiseMatch);
	}

	@Override
	public BackwardLinkReversedMatch1 getBackwardLinkReversedMatch1(
			BackwardLinkReversed parent, BackwardLinkMatch1 conclusionMatch) {
		return new BackwardLinkReversedMatch1(parent, conclusionMatch);
	}

	@Override
	public BackwardLinkReversedMatch2 getBackwardLinkReversedMatch2(
			BackwardLinkReversedMatch1 parent, ForwardLinkMatch2 premiseMatch) {
		return new BackwardLinkReversedMatch2(parent, premiseMatch);
	}

	@Override
	public BackwardLinkReversedMatch3 getBackwardLinkReversedMatch3(
			BackwardLinkReversedMatch2 parent, ForwardLinkMatch3 premiseMatch) {
		return new BackwardLinkReversedMatch3(parent, premiseMatch);
	}

	@Override
	public ClassInconsistencyOfDisjointSubsumersMatch1 getClassInconsistencyOfDisjointSubsumersMatch1(
			ClassInconsistencyOfDisjointSubsumers parent,
			ClassInconsistencyMatch1 conclusionMatch) {
		return new ClassInconsistencyOfDisjointSubsumersMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ClassInconsistencyOfDisjointSubsumersMatch2 getClassInconsistencyOfDisjointSubsumersMatch2(
			ClassInconsistencyOfDisjointSubsumersMatch1 parent,
			DisjointSubsumerMatch2 firstPremiseMatch) {
		return new ClassInconsistencyOfDisjointSubsumersMatch2(parent,
				firstPremiseMatch);
	}

	@Override
	public ClassInconsistencyOfObjectComplementOfMatch1 getClassInconsistencyOfObjectComplementOfMatch1(
			ClassInconsistencyOfObjectComplementOf parent,
			ClassInconsistencyMatch1 conclusionMatch) {
		return new ClassInconsistencyOfObjectComplementOfMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ClassInconsistencyOfObjectComplementOfMatch2 getClassInconsistencyOfObjectComplementOfMatch2(
			ClassInconsistencyOfObjectComplementOfMatch1 parent,
			SubClassInclusionDecomposedMatch2 secondPremiseMatch) {
		return new ClassInconsistencyOfObjectComplementOfMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public ClassInconsistencyOfOwlNothingMatch1 getClassInconsistencyOfOwlNothingMatch1(
			ClassInconsistencyOfOwlNothing parent,
			ClassInconsistencyMatch1 conclusionMatch) {
		return new ClassInconsistencyOfOwlNothingMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ClassInconsistencyPropagatedMatch1 getClassInconsistencyPropagatedMatch1(
			ClassInconsistencyPropagated parent,
			ClassInconsistencyMatch1 conclusionMatch) {
		return new ClassInconsistencyPropagatedMatch1(parent, conclusionMatch);
	}

	@Override
	public ClassInconsistencyPropagatedMatch2 getClassInconsistencyPropagatedMatch2(
			ClassInconsistencyPropagatedMatch1 parent,
			BackwardLinkMatch2 firstPremiseMatch) {
		return new ClassInconsistencyPropagatedMatch2(parent,
				firstPremiseMatch);
	}

	@Override
	public DisjointSubsumerFromSubsumerMatch1 getDisjointSubsumerFromSubsumerMatch1(
			DisjointSubsumerFromSubsumer parent,
			DisjointSubsumerMatch1 conclusionMatch) {
		return new DisjointSubsumerFromSubsumerMatch1(parent, conclusionMatch);
	}

	@Override
	public DisjointSubsumerFromSubsumerMatch2 getDisjointSubsumerFromSubsumerMatch2(
			DisjointSubsumerFromSubsumerMatch1 parent,
			IndexedDisjointClassesAxiomMatch2 secondPremiseMatch) {
		return new DisjointSubsumerFromSubsumerMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public ElkClassAssertionAxiomConversionMatch1 getElkClassAssertionAxiomConversionMatch1(
			ElkClassAssertionAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkClassAssertionAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDifferentIndividualsAxiomBinaryConversionMatch1 getElkDifferentIndividualsAxiomBinaryConversionMatch1(
			ElkDifferentIndividualsAxiomBinaryConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkDifferentIndividualsAxiomBinaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDifferentIndividualsAxiomNaryConversionMatch1 getElkDifferentIndividualsAxiomNaryConversionMatch1(
			ElkDifferentIndividualsAxiomNaryConversion parent,
			IndexedDisjointClassesAxiomMatch1 conclusionMatch) {
		return new ElkDifferentIndividualsAxiomNaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointClassesAxiomBinaryConversionMatch1 getElkDisjointClassesAxiomBinaryConversionMatch1(
			ElkDisjointClassesAxiomBinaryConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkDisjointClassesAxiomBinaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointClassesAxiomNaryConversionMatch1 getElkDisjointClassesAxiomNaryConversionMatch1(
			ElkDisjointClassesAxiomNaryConversion parent,
			IndexedDisjointClassesAxiomMatch1 conclusionMatch) {
		return new ElkDisjointClassesAxiomNaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointUnionAxiomBinaryConversionMatch1 getElkDisjointUnionAxiomBinaryConversionMatch1(
			ElkDisjointUnionAxiomBinaryConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkDisjointUnionAxiomBinaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointUnionAxiomEquivalenceConversionMatch1 getElkDisjointUnionAxiomEquivalenceConversionMatch1(
			ElkDisjointUnionAxiomEquivalenceConversion parent,
			IndexedEquivalentClassesAxiomMatch1 conclusionMatch) {
		return new ElkDisjointUnionAxiomEquivalenceConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointUnionAxiomNaryConversionMatch1 getElkDisjointUnionAxiomNaryConversionMatch1(
			ElkDisjointUnionAxiomNaryConversion parent,
			IndexedDisjointClassesAxiomMatch1 conclusionMatch) {
		return new ElkDisjointUnionAxiomNaryConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointUnionAxiomOwlNothingConversionMatch1 getElkDisjointUnionAxiomOwlNothingConversionMatch1(
			ElkDisjointUnionAxiomOwlNothingConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkDisjointUnionAxiomOwlNothingConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkDisjointUnionAxiomSubClassConversionMatch1 getElkDisjointUnionAxiomSubClassConversionMatch1(
			ElkDisjointUnionAxiomSubClassConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkDisjointUnionAxiomSubClassConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkEquivalentClassesAxiomEquivalenceConversionMatch1 getElkEquivalentClassesAxiomEquivalenceConversionMatch1(
			ElkEquivalentClassesAxiomEquivalenceConversion parent,
			IndexedEquivalentClassesAxiomMatch1 conclusionMatch) {
		return new ElkEquivalentClassesAxiomEquivalenceConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkEquivalentClassesAxiomSubClassConversionMatch1 getElkEquivalentClassesAxiomSubClassConversionMatch1(
			ElkEquivalentClassesAxiomSubClassConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkEquivalentClassesAxiomSubClassConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkEquivalentObjectPropertiesAxiomConversionMatch1 getElkEquivalentObjectPropertiesAxiomConversionMatch1(
			ElkEquivalentObjectPropertiesAxiomConversion parent,
			IndexedSubObjectPropertyOfAxiomMatch1 conclusionMatch) {
		return new ElkEquivalentObjectPropertiesAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkObjectPropertyAssertionAxiomConversionMatch1 getElkObjectPropertyAssertionAxiomConversionMatch1(
			ElkObjectPropertyAssertionAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkObjectPropertyAssertionAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkObjectPropertyDomainAxiomConversionMatch1 getElkObjectPropertyDomainAxiomConversionMatch1(
			ElkObjectPropertyDomainAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkObjectPropertyDomainAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkObjectPropertyRangeAxiomConversionMatch1 getElkObjectPropertyRangeAxiomConversionMatch1(
			ElkObjectPropertyRangeAxiomConversion parent,
			IndexedObjectPropertyRangeAxiomMatch1 conclusionMatch) {
		return new ElkObjectPropertyRangeAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkReflexiveObjectPropertyAxiomConversionMatch1 getElkReflexiveObjectPropertyAxiomConversionMatch1(
			ElkReflexiveObjectPropertyAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkReflexiveObjectPropertyAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkSameIndividualAxiomConversionMatch1 getElkSameIndividualAxiomConversionMatch1(
			ElkSameIndividualAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkSameIndividualAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkSubClassOfAxiomConversionMatch1 getElkSubClassOfAxiomConversionMatch1(
			ElkSubClassOfAxiomConversion parent,
			IndexedSubClassOfAxiomMatch1 conclusionMatch) {
		return new ElkSubClassOfAxiomConversionMatch1(parent, conclusionMatch);
	}

	@Override
	public ElkSubObjectPropertyOfAxiomConversionMatch1 getElkSubObjectPropertyOfAxiomConversionMatch1(
			ElkSubObjectPropertyOfAxiomConversion parent,
			IndexedSubObjectPropertyOfAxiomMatch1 conclusionMatch) {
		return new ElkSubObjectPropertyOfAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ElkTransitiveObjectPropertyAxiomConversionMatch1 getElkTransitiveObjectPropertyAxiomConversionMatch1(
			ElkTransitiveObjectPropertyAxiomConversion parent,
			IndexedSubObjectPropertyOfAxiomMatch1 conclusionMatch) {
		return new ElkTransitiveObjectPropertyAxiomConversionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ForwardLinkCompositionMatch1 getForwardLinkCompositionMatch1(
			ForwardLinkComposition parent, ForwardLinkMatch1 conclusionMatch) {
		return new ForwardLinkCompositionMatch1(parent, conclusionMatch);
	}

	@Override
	public ForwardLinkCompositionMatch2 getForwardLinkCompositionMatch2(
			ForwardLinkCompositionMatch1 parent,
			ForwardLinkMatch2 conclusionMatch) {
		return new ForwardLinkCompositionMatch2(parent, conclusionMatch);
	}

	@Override
	public ForwardLinkCompositionMatch3 getForwardLinkCompositionMatch3(
			ForwardLinkCompositionMatch2 parent,
			BackwardLinkMatch2 firstPremiseMatch) {
		return new ForwardLinkCompositionMatch3(parent, firstPremiseMatch);
	}

	@Override
	public ForwardLinkCompositionMatch4 getForwardLinkCompositionMatch4(
			ForwardLinkCompositionMatch3 parent,
			SubPropertyChainMatch2 fourthPremiseMatch) {
		return new ForwardLinkCompositionMatch4(parent, fourthPremiseMatch);
	}

	@Override
	public ForwardLinkCompositionMatch5 getForwardLinkCompositionMatch5(
			ForwardLinkCompositionMatch4 parent,
			ForwardLinkMatch3 thirdPremiseMatch) {
		return new ForwardLinkCompositionMatch5(parent, thirdPremiseMatch);
	}

	@Override
	public ForwardLinkOfObjectHasSelfMatch1 getForwardLinkOfObjectHasSelfMatch1(
			ForwardLinkOfObjectHasSelf parent,
			ForwardLinkMatch1 conclusionMatch) {
		return new ForwardLinkOfObjectHasSelfMatch1(parent, conclusionMatch);
	}

	@Override
	public ForwardLinkOfObjectHasSelfMatch2 getForwardLinkOfObjectHasSelfMatch2(
			ForwardLinkOfObjectHasSelfMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new ForwardLinkOfObjectHasSelfMatch2(parent, premiseMatch);
	}

	@Override
	public ForwardLinkOfObjectSomeValuesFromMatch1 getForwardLinkOfObjectSomeValuesFromMatch1(
			ForwardLinkOfObjectSomeValuesFrom parent,
			ForwardLinkMatch1 conclusionMatch) {
		return new ForwardLinkOfObjectSomeValuesFromMatch1(parent,
				conclusionMatch);
	}

	@Override
	public ForwardLinkOfObjectSomeValuesFromMatch2 getForwardLinkOfObjectSomeValuesFromMatch2(
			ForwardLinkOfObjectSomeValuesFromMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new ForwardLinkOfObjectSomeValuesFromMatch2(parent,
				premiseMatch);
	}

	@Override
	public PropagationGeneratedMatch1 getPropagationGeneratedMatch1(
			PropagationGenerated parent, PropagationMatch1 conclusionMatch) {
		return new PropagationGeneratedMatch1(parent, conclusionMatch);
	}

	@Override
	public PropertyRangeInheritedMatch1 getPropertyRangeInheritedMatch1(
			PropertyRangeInherited parent,
			PropertyRangeMatch1 conclusionMatch) {
		return new PropertyRangeInheritedMatch1(parent, conclusionMatch);
	}

	@Override
	public PropertyRangeInheritedMatch2 getPropertyRangeInheritedMatch2(
			PropertyRangeInheritedMatch1 parent,
			IndexedObjectPropertyRangeAxiomMatch2 secondPremiseMatch) {
		return new PropertyRangeInheritedMatch2(parent, secondPremiseMatch);
	}

	@Override
	public PropertyRangeInheritedMatch3 getPropertyRangeInheritedMatch3(
			PropertyRangeInheritedMatch2 parent,
			SubPropertyChainMatch2 firstPremiseMatch) {
		return new PropertyRangeInheritedMatch3(parent, firstPremiseMatch);
	}

	@Override
	public SubClassInclusionComposedDefinedClassMatch1 getSubClassInclusionComposedDefinedClassMatch1(
			SubClassInclusionComposedDefinedClass parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionComposedDefinedClassMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionComposedDefinedClassMatch2 getSubClassInclusionComposedDefinedClassMatch2(
			SubClassInclusionComposedDefinedClassMatch1 parent,
			IndexedEquivalentClassesAxiomMatch2 secondPremiseMatch) {
		return new SubClassInclusionComposedDefinedClassMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionComposedEntityMatch1 getSubClassInclusionComposedEntityMatch1(
			SubClassInclusionComposedEntity parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionComposedEntityMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionComposedObjectIntersectionOfMatch1 getSubClassInclusionComposedObjectIntersectionOfMatch1(
			SubClassInclusionComposedObjectIntersectionOf parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionComposedObjectIntersectionOfMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionComposedObjectSomeValuesFromMatch1 getSubClassInclusionComposedObjectSomeValuesFromMatch1(
			SubClassInclusionComposedObjectSomeValuesFrom parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionComposedObjectSomeValuesFromMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionComposedObjectSomeValuesFromMatch2 getSubClassInclusionComposedObjectSomeValuesFromMatch2(
			SubClassInclusionComposedObjectSomeValuesFromMatch1 parent,
			BackwardLinkMatch2 secondPremiseMatch) {
		return new SubClassInclusionComposedObjectSomeValuesFromMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionComposedObjectUnionOfMatch1 getSubClassInclusionComposedObjectUnionOfMatch1(
			SubClassInclusionComposedObjectUnionOf parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionComposedObjectUnionOfMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionDecomposedFirstConjunctMatch1 getSubClassInclusionDecomposedFirstConjunctMatch1(
			SubClassInclusionDecomposedFirstConjunct parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionDecomposedFirstConjunctMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionDecomposedFirstConjunctMatch2 getSubClassInclusionDecomposedFirstConjunctMatch2(
			SubClassInclusionDecomposedFirstConjunctMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new SubClassInclusionDecomposedFirstConjunctMatch2(parent,
				premiseMatch);
	}

	@Override
	public SubClassInclusionDecomposedSecondConjunctMatch1 getSubClassInclusionDecomposedSecondConjunctMatch1(
			SubClassInclusionDecomposedSecondConjunct parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionDecomposedSecondConjunctMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionDecomposedSecondConjunctMatch2 getSubClassInclusionDecomposedSecondConjunctMatch2(
			SubClassInclusionDecomposedSecondConjunctMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		return new SubClassInclusionDecomposedSecondConjunctMatch2(parent,
				premiseMatch);
	}

	@Override
	public SubClassInclusionExpandedDefinitionMatch1 getSubClassInclusionExpandedDefinitionMatch1(
			SubClassInclusionExpandedDefinition parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionExpandedDefinitionMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionExpandedDefinitionMatch2 getSubClassInclusionExpandedDefinitionMatch2(
			SubClassInclusionExpandedDefinitionMatch1 parent,
			IndexedEquivalentClassesAxiomMatch2 secondPremiseMatch) {
		return new SubClassInclusionExpandedDefinitionMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionExpandedFirstEquivalentClassMatch1 getSubClassInclusionExpandedFirstEquivalentClassMatch1(
			SubClassInclusionExpandedFirstEquivalentClass parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionExpandedFirstEquivalentClassMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionExpandedFirstEquivalentClassMatch2 getSubClassInclusionExpandedFirstEquivalentClassMatch2(
			SubClassInclusionExpandedFirstEquivalentClassMatch1 parent,
			IndexedEquivalentClassesAxiomMatch2 secondPremiseMatch) {
		return new SubClassInclusionExpandedFirstEquivalentClassMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionExpandedSecondEquivalentClassMatch1 getSubClassInclusionExpandedSecondEquivalentClassMatch1(
			SubClassInclusionExpandedSecondEquivalentClass parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionExpandedSecondEquivalentClassMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionExpandedSecondEquivalentClassMatch2 getSubClassInclusionExpandedSecondEquivalentClassMatch2(
			SubClassInclusionExpandedSecondEquivalentClassMatch1 parent,
			IndexedEquivalentClassesAxiomMatch2 secondPremiseMatch) {
		return new SubClassInclusionExpandedSecondEquivalentClassMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionExpandedSubClassOfMatch1 getSubClassInclusionExpandedSubClassOfMatch1(
			SubClassInclusionExpandedSubClassOf parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionExpandedSubClassOfMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionExpandedSubClassOfMatch2 getSubClassInclusionExpandedSubClassOfMatch2(
			SubClassInclusionExpandedSubClassOfMatch1 parent,
			IndexedSubClassOfAxiomMatch2 secondPremiseMatch) {
		return new SubClassInclusionExpandedSubClassOfMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionObjectHasSelfPropertyRangeMatch1 getSubClassInclusionObjectHasSelfPropertyRangeMatch1(
			SubClassInclusionObjectHasSelfPropertyRange parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionObjectHasSelfPropertyRangeMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubClassInclusionObjectHasSelfPropertyRangeMatch2 getSubClassInclusionObjectHasSelfPropertyRangeMatch2(
			SubClassInclusionObjectHasSelfPropertyRangeMatch1 parent,
			SubClassInclusionDecomposedMatch2 firstPremiseMatch) {
		return new SubClassInclusionObjectHasSelfPropertyRangeMatch2(parent,
				firstPremiseMatch);
	}

	@Override
	public SubClassInclusionObjectHasSelfPropertyRangeMatch3 getSubClassInclusionObjectHasSelfPropertyRangeMatch3(
			SubClassInclusionObjectHasSelfPropertyRangeMatch2 parent,
			PropertyRangeMatch2 secondPremiseMatch) {
		return new SubClassInclusionObjectHasSelfPropertyRangeMatch3(parent,
				secondPremiseMatch);
	}

	@Override
	public SubClassInclusionOwlThingMatch1 getSubClassInclusionOwlThingMatch1(
			SubClassInclusionOwlThing parent,
			SubClassInclusionComposedMatch1 conclusionMatch) {
		return new SubClassInclusionOwlThingMatch1(parent, conclusionMatch);
	}

	@Override
	public SubClassInclusionRangeMatch1 getSubClassInclusionRangeMatch1(
			SubClassInclusionRange parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionRangeMatch1(parent, conclusionMatch);
	}

	@Override
	public SubClassInclusionRangeMatch2 getSubClassInclusionRangeMatch2(
			SubClassInclusionRangeMatch1 parent,
			PropertyRangeMatch2 premiseMatch) {
		return new SubClassInclusionRangeMatch2(parent, premiseMatch);
	}

	@Override
	public SubClassInclusionTautologyMatch1 getSubClassInclusionTautologyMatch1(
			SubClassInclusionTautology parent,
			SubClassInclusionDecomposedMatch1 conclusionMatch) {
		return new SubClassInclusionTautologyMatch1(parent, conclusionMatch);
	}

	@Override
	public SubPropertyChainExpandedSubObjectPropertyOfMatch1 getSubPropertyChainExpandedSubObjectPropertyOfMatch1(
			SubPropertyChainExpandedSubObjectPropertyOf parent,
			SubPropertyChainMatch1 conclusionMatch) {
		return new SubPropertyChainExpandedSubObjectPropertyOfMatch1(parent,
				conclusionMatch);
	}

	@Override
	public SubPropertyChainExpandedSubObjectPropertyOfMatch2 getSubPropertyChainExpandedSubObjectPropertyOfMatch2(
			SubPropertyChainExpandedSubObjectPropertyOfMatch1 parent,
			IndexedSubObjectPropertyOfAxiomMatch2 secondPremiseMatch) {
		return new SubPropertyChainExpandedSubObjectPropertyOfMatch2(parent,
				secondPremiseMatch);
	}

	@Override
	public SubPropertyChainTautologyMatch1 getSubPropertyChainTautologyMatch1(
			SubPropertyChainTautology parent,
			SubPropertyChainMatch1 conclusionMatch) {
		return new SubPropertyChainTautologyMatch1(parent, conclusionMatch);
	}

}
