/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.azkfw.configuration.ConfigurationFormatException;
import org.azkfw.context.Context;
import org.azkfw.plugin.PluginManager;
import org.azkfw.plugin.PluginServiceException;
import org.azkfw.test.context.TestContext;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、永続化機能をサポートしたタスククラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/06/06
 * @author Kawakicchi
 */
public class AbstractPersistenceTestCase extends AbstractTestCase {

	private static boolean PLUGIN_LOAD_FLAG = false;

	/**
	 * コンテキスト情報を取得する。
	 * 
	 * @return コンテキスト情報
	 */
	protected Context getContext() {
		return new TestContext("./src/test/resources/");
	}

	protected String getPluginFile() {
		return "conf/azuki-plugin.xml";
	}

	@Override
	public void setUp() {
		super.setUp();

		if (!PLUGIN_LOAD_FLAG) {
			PLUGIN_LOAD_FLAG = true;
			if (StringUtility.isNotEmpty(getPluginFile())) {
				try {
					Context context = getContext();
					InputStream is = context.getResourceAsStream(getPluginFile());
					if (null != is) {
						PluginManager.load(is, context);
					}
					PluginManager.initialize();
				} catch (PluginServiceException ex) {
					ex.printStackTrace();
					fail(ex.getLocalizedMessage());
				} catch (ConfigurationFormatException ex) {
					ex.printStackTrace();
					fail(ex.getLocalizedMessage());
				} catch (IOException ex) {
					ex.printStackTrace();
					fail(ex.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	public void tearDown() {
		if (StringUtility.isNotEmpty(getPluginFile())) {
			PluginManager.destroy();
		}

		super.tearDown();
	}

	protected File getTestContextFile(final String aName) {
		Context context = getContext();
		File file = new File(context.getAbstractPath(aName));
		return file;
	}

	public static void assertEquals(final File expected, final File actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(final File expected, final File actual, final Charset charset) {
		assertEquals(null, expected, actual, charset);
	}

	public static void assertEquals(final String message, final File expected, final File actual) {
		assertEquals(message, expected, actual, Charset.forName(System.getProperty("file.encoding")));
	}

	public static void assertEquals(final String message, final File expected, final File actual, final Charset charset) {
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		try {
			reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(expected), charset));
			reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(actual), charset));

			String line1, line2;
			int line = 0;
			while (true) {
				line1 = reader1.readLine();
				line2 = reader2.readLine();

				if (null == line1 && null == line2) {
					break;
				} else if (null == line1 || null == line2) {
					assertEquals(String.format("line %d", line), line1, line2);
					break;
				} else {
					assertEquals(String.format("line %d", line), line1, line2);
				}

				line++;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			if (null != reader1) {
				try {
					reader1.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (null != reader2) {
				try {
					reader2.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
