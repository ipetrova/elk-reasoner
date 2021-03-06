/*
 * #%L
 * ELK Utilities for Testing
 * 
 * $Id$
 * $HeadURL$
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
/**
 * 
 */
package org.semanticweb.elk.testing.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.semanticweb.elk.io.FileUtils;
import org.semanticweb.elk.testing.TestInput;
import org.semanticweb.elk.testing.TestOutput;

/**
 * @author Pavel Klinov
 *
 * pavel.klinov@uni-ulm.de
 *
 */
public class URLTestIO implements TestInput, TestOutput {

	private final URL url;
	
	
	public URLTestIO(final URL file) {
		this.url = file;
	}
	
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

	@Override
	public String toString() {
		return url.toString();
	}

	@Override
	public String getName() {
		return FileUtils.getFileName(url.getPath());
	}
}