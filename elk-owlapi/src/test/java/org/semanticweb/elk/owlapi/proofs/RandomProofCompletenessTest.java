/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.owlapi.proofs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.elk.RandomSeedProvider;
import org.semanticweb.elk.owlapi.OWLAPITestUtils;
import org.semanticweb.elk.reasoner.tracing.TracingTestManifest;
import org.semanticweb.elk.testing.ConfigurationUtils;
import org.semanticweb.elk.testing.ConfigurationUtils.TestManifestCreator;
import org.semanticweb.elk.testing.PolySuite;
import org.semanticweb.elk.testing.PolySuite.Config;
import org.semanticweb.elk.testing.PolySuite.Configuration;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.VoidTestOutput;
import org.semanticweb.elk.testing.io.URLTestIO;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapitools.proofs.ExplainingOWLReasoner;
import org.semanticweb.owlapitools.proofs.exception.ProofGenerationException;
import org.semanticweb.owlapitools.proofs.expressions.OWLExpression;

/**
 * For some conclusions tries to randomly break all collected proofs by removing
 * required axioms; if the conclusion is still derived by the reasoner,
 * we missed some proof.
 * 
 * @author Peter Skocovsky
 * 
 */
@RunWith(PolySuite.class)
public class RandomProofCompletenessTest extends BaseProofTest {

	final static String INPUT_DATA_LOCATION = "classification_test_input";

	public RandomProofCompletenessTest(TracingTestManifest testManifest) {
		super(testManifest);
	}

	@Test
	public void proofCompletenessTest() throws Exception {
		final long seed = RandomSeedProvider.VALUE;
		
		final OWLDataFactory factory = manager_.getOWLDataFactory();
		
		// loading and classifying via the OWL API
		final OWLOntology ontology =
				loadOntology(manifest_.getInput().getInputStream());
		final ExplainingOWLReasoner reasoner =
				OWLAPITestUtils.createReasoner(ontology);
		try {
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		} catch (final InconsistentOntologyException e) {
			// we will explain it, too
		}

		try {
			// now do testing
	        
	        ProofTestUtils.visitAllSubsumptionsForProofTests(reasoner, factory,
	        		new ProofTestVisitor<ProofGenerationException>() {
				
				@Override
				public void visit(final OWLClassExpression subsumee,
						final OWLClassExpression subsumer)
								throws ProofGenerationException {
					randomProofCompletenessTest(reasoner,
							factory.getOWLSubClassOfAxiom(subsumee, subsumer),
							ontology, seed);
				}

				@Override
				public void inconsistencyTest()
						throws ProofGenerationException {
					randomInconsistencyProofCompletenessTest(reasoner, ontology,
							seed);
				}
				
			});
			
		} catch (final ProofGenerationException e) {
			fail(e.getClass().getName() + " " + e.getMessage());
		} finally {
			reasoner.dispose();
		}
		
	}
	
	private void randomProofCompletenessTest(
			final ExplainingOWLReasoner reasoner,
			final OWLSubClassOfAxiom conclusion, final OWLOntology ontology,
			final long seed) throws ProofGenerationException {
		final Random random = new Random(seed);
		
		final OWLExpression expr = reasoner.getDerivedExpression(conclusion);
		
		final Set<OWLAxiom> proofBreaker =
				ProofTestUtils.collectProofBreaker(expr, ontology, random);
		
		final Set<OWLAxiom> axs = new HashSet<OWLAxiom>(ontology.getAxioms());
		axs.removeAll(proofBreaker);
		
		ExplainingOWLReasoner checkReasoner = null;
		try {
			
			checkReasoner = OWLAPITestUtils.createReasoner(
					manager_.createOntology(axs));
			
			if (!checkReasoner.isConsistent()) {
				fail("Not all proofs were found!\n"
						+ "Seed: " + seed + "\n"
						+ "Conclusion: " + conclusion + "\n"
						+ "Proof Breaker: " + proofBreaker);
			}
			
			final boolean conclusionDerived =
					checkReasoner.getSuperClasses(conclusion.getSubClass(), false)
					.containsEntity((OWLClass) conclusion.getSuperClass());
			
			assertFalse("Not all proofs were found!\n"
							+ "Seed: " + seed + "\n"
							+ "Conclusion: " + conclusion + "\n"
							+ "Proof Breaker: " + proofBreaker,
					conclusionDerived
			);
			
		} catch (final OWLOntologyCreationException e) {
			throw new RuntimeException(e);
		} finally {
			if (checkReasoner != null) {
				checkReasoner.dispose();
			}
		}
		
	}
	
	private void randomInconsistencyProofCompletenessTest(
			final ExplainingOWLReasoner reasoner, final OWLOntology ontology,
			final long seed) throws ProofGenerationException {
		final Random random = new Random(seed);
		
		final OWLExpression expr =
				reasoner.getDerivedExpressionForInconsistency();
		
		final Set<OWLAxiom> proofBreaker =
				ProofTestUtils.collectProofBreaker(expr, ontology, random);
		
		final Set<OWLAxiom> axs = new HashSet<OWLAxiom>(ontology.getAxioms());
		axs.removeAll(proofBreaker);
		
		ExplainingOWLReasoner checkReasoner = null;
		try {
			checkReasoner = OWLAPITestUtils.createReasoner(
					manager_.createOntology(axs));
			
			final boolean conclusionDerived = !reasoner.isConsistent();
			
			assertFalse("Not all proofs were found!\n"
							+ "Seed: " + seed + "\n"
							+ "Conclusion: Ontology is inconsistent\n"
							+ "Proof Breaker: " + proofBreaker,
					conclusionDerived
			);
			
		} catch (final OWLOntologyCreationException e) {
			throw new RuntimeException(e);
		} finally {
			if (checkReasoner != null) {
				checkReasoner.dispose();
			}
		}
		
	}

	@Config
	public static Configuration getConfig() throws URISyntaxException,
			IOException {
		return ConfigurationUtils
				.loadFileBasedTestConfiguration(
						INPUT_DATA_LOCATION,
						RandomProofCompletenessTest.class,
						"owl",
						new TestManifestCreator<URLTestIO, VoidTestOutput, VoidTestOutput>() {
							@Override
							public TestManifest<URLTestIO, VoidTestOutput, VoidTestOutput> create(
									final URL input, final URL output) throws IOException {
								// don't need an expected output for these tests
								return new TracingTestManifest(input);
							}
						});
	}
}
