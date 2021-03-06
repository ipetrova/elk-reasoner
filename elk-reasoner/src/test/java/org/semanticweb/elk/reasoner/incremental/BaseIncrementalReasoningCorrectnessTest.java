/**
 * 
 */
package org.semanticweb.elk.reasoner.incremental;

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

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.elk.RandomSeedProvider;
import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.io.IOUtils;
import org.semanticweb.elk.owl.parsing.Owl2ParseException;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.ReasoningTestManifest;
import org.semanticweb.elk.testing.PolySuite;
import org.semanticweb.elk.testing.TestInput;
import org.semanticweb.elk.testing.TestOutput;
import org.semanticweb.elk.util.collections.Operations;
import org.semanticweb.elk.util.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
@RunWith(PolySuite.class)
public abstract class BaseIncrementalReasoningCorrectnessTest<T, EO extends TestOutput, AO extends TestOutput> {

	// logger for this class
	protected static final Logger LOGGER_ = LoggerFactory
			.getLogger(BaseIncrementalReasoningCorrectnessTest.class);

	final static int REPEAT_NUMBER = 5;
	final static double DELETE_RATIO = 0.2;

	protected final ReasoningTestManifest<EO, AO> manifest;
	protected List<T> staticAxioms = null;
	protected OnOffVector<T> changingAxioms = null;

	public BaseIncrementalReasoningCorrectnessTest(
			ReasoningTestManifest<EO, AO> testManifest) {
		manifest = testManifest;
	}

	@Before
	public void before() throws IOException, Owl2ParseException {
		assumeTrue(!ignore(manifest.getInput()));

		InputStream stream = null;

		try {
			stream = manifest.getInput().getInputStream();
			staticAxioms = new ArrayList<T>(15);
			changingAxioms = new OnOffVector<T>(15);
			loadAxioms(stream, staticAxioms, changingAxioms);
		} finally {
			IOUtils.closeQuietly(stream);
		}

	}

	/**
	 * @param input
	 *            dummy parameter
	 */
	@SuppressWarnings("static-method")
	protected boolean ignore(TestInput input) {
		return false;
	}

	/**
	 * The main test method
	 * 
	 * @throws ElkException
	 */
	@Test
	public void incrementalReasoning() throws ElkException {
		changingAxioms.setAllOn();

		@SuppressWarnings("unchecked")
		Reasoner standardReasoner = getReasoner(Operations.concat(staticAxioms,
				changingAxioms.getOnElements()));
		@SuppressWarnings("unchecked")
		Reasoner incrementalReasoner = getReasoner(Operations.concat(
				staticAxioms, changingAxioms.getOnElements()));

		standardReasoner.setAllowIncrementalMode(false);
		incrementalReasoner.setAllowIncrementalMode(true);
		// initial correctness check
		correctnessCheck(standardReasoner, incrementalReasoner, -1);

		long seed = RandomSeedProvider.VALUE;
		Random rnd = new Random(seed);

		for (int i = 0; i < REPEAT_NUMBER; i++) {
			changingAxioms.setAllOff();
			// delete some axioms

			randomFlip(changingAxioms, rnd, DELETE_RATIO);

			if (LOGGER_.isDebugEnabled()) {
				for (T del : changingAxioms.getOnElements()) {
					dumpChangeToLog(del, LogLevel.DEBUG);
				}
			}

			// incremental changes
			applyChanges(standardReasoner, changingAxioms.getOnElements(),
					IncrementalChangeType.DELETE);
			applyChanges(incrementalReasoner, changingAxioms.getOnElements(),
					IncrementalChangeType.DELETE);

			LOGGER_.info("===DELETIONS===");

			correctnessCheck(standardReasoner, incrementalReasoner, seed);

			// add the axioms back
			applyChanges(standardReasoner, changingAxioms.getOnElements(),
					IncrementalChangeType.ADD);
			applyChanges(incrementalReasoner, changingAxioms.getOnElements(),
					IncrementalChangeType.ADD);

			LOGGER_.info("===ADDITIONS===");

			correctnessCheck(standardReasoner, incrementalReasoner, seed);
		}
	}

	protected void randomFlip(OnOffVector<T> axioms, Random rnd, double fraction) {
		Collections.shuffle(axioms, rnd);

		int flipped = 0;

		for (int i = 0; i < axioms.size()
				&& flipped <= fraction * axioms.size(); i++) {
			axioms.flipOnOff(i);
			flipped++;
		}
	}

	protected abstract void applyChanges(Reasoner reasoner,
			Iterable<T> changes, IncrementalChangeType type);

	protected abstract void dumpChangeToLog(T change, LogLevel level);

	protected abstract void loadAxioms(InputStream stream,
			List<T> staticAxiomsInput, OnOffVector<T> changingAxiomsInput)
			throws IOException, Owl2ParseException;

	protected abstract Reasoner getReasoner(Iterable<T> axioms);

	protected abstract void correctnessCheck(Reasoner standardReasoner,
			Reasoner incrementalReasoner, long seed) throws ElkException;
}
