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
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.azkfw.configuration.ConfigurationFormatException;
import org.azkfw.context.Context;
import org.azkfw.log.LoggerFactory;
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

	/**
	 * プラグイン読み込みフラグ
	 * <p>
	 * 全てのテストで一度のみプラグインを読み込む
	 * </p>
	 */
	private static boolean PLUGIN_LOAD_FLAG = false;

	@Override
	public void setUp() {
		super.setUp();

		if (!PLUGIN_LOAD_FLAG) {
			PLUGIN_LOAD_FLAG = true;

			LoggerFactory.load("org.azkfw.log.Log4jLoggerFactory", "conf/log4j.xml", getContext());
			setLogger(LoggerFactory.create(this.getClass())); // ロガーを再設定

			if (StringUtility.isNotEmpty(getPluginFile())) {
				try {
					Context context = getContext();
					InputStream is = context.getResourceAsStream(getPluginFile());
					if (null != is) {
						PluginManager.load(is, context);
						PluginManager.initialize();
					} else {
						String msg = String.format("Plugin file not found.[%s]", getPluginFile());
						fatal(msg);
						fail(msg);
					}
				} catch (PluginServiceException ex) {
					String msg = "Plugin file load error.";
					fatal(msg, ex);
					fail(msg);
				} catch (ConfigurationFormatException ex) {
					String msg = "Plugin file load error.";
					fatal(msg, ex);
					fail(msg);
				} catch (IOException ex) {
					String msg = "Plugin file load error.";
					fatal(msg, ex);
					fail(msg);
				}
			}
		}
	}

	@Override
	public void tearDown() {

		super.tearDown();
	}

	/**
	 * コンテキスト情報を取得する。
	 * 
	 * @return コンテキスト情報
	 */
	protected Context getContext() {
		return new TestContext("./src/test/resources/");
	}

	/**
	 * テスト用のコンテキスト情報を取得する。
	 * 
	 * @return コンテキスト情報
	 */
	protected Context getTestContext() {
		return new TestContext("./src/test/data/");
	}

	/**
	 * プラグインファイルを取得する。
	 * 
	 * @return プラグインファイル
	 */
	protected String getPluginFile() {
		return "conf/azuki-plugin.xml";
	}

	/**
	 * テストファイルを文字列として取得する。
	 * 
	 * @param name 名前
	 * @return 文字列
	 */
	protected final String getTestFileToString(final String name) {
		return getTestFileToString(name, Charset.defaultCharset());
	}

	/**
	 * テストファイルを文字列として取得する。
	 * 
	 * @param name 名前
	 * @param charset 文字コード
	 * @return 文字列
	 */
	protected final String getTestFileToString(final String name, final Charset charset) {
		String string = null;
		try {
			InputStream stream = getTestContext().getResourceAsStream(name);
			if (null != stream) {
				string = getStreamToString(stream, charset);
			} else {
				String msg = String.format("File not found.[%s]", name);
				fatal(msg);
				fail(msg);
			}
		} catch (IOException ex) {
			String msg = String.format("File read error.[%s]", name);
			fatal(msg, ex);
			fail(msg);
		}
		return string;
	}

	/**
	 * テキストファイルの内容を比較する。
	 * 
	 * @param expected 期待値
	 * @param actual 現行値
	 */
	protected final void assertEquals(final File expected, final File actual) {
		assertEquals(null, expected, actual);
	}

	/**
	 * テキストファイルの内容を比較する。
	 * 
	 * @param expected 期待値
	 * @param actual 現行値
	 * @param charset 文字コード
	 */
	protected final void assertEquals(final File expected, final File actual, final Charset charset) {
		assertEquals(null, expected, actual, charset);
	}

	/**
	 * テキストファイルの内容を比較する。
	 * 
	 * @param message メッセージ
	 * @param expected 期待値
	 * @param actual 現行値
	 */
	protected final void assertEquals(final String message, final File expected, final File actual) {
		assertEquals(message, expected, actual, Charset.defaultCharset());
	}

	/**
	 * テキストファイルの内容を比較する。
	 * 
	 * @param message メッセージ
	 * @param expected 期待値
	 * @param actual 現行値
	 * @param charset 文字コード
	 */
	protected final void assertEquals(final String message, final File expected, final File actual, final Charset charset) {
		String exp = null;
		try {
			exp = getStreamToString(new FileInputStream(expected), charset);
		} catch (IOException ex) {
			String msg = String.format("File not found.[%s]", expected);
			fatal(msg, ex);
			fail(msg);
		}
		String act = null;
		try {
			act = getStreamToString(new FileInputStream(actual), charset);
		} catch (IOException ex) {
			String msg = String.format("File not found.[%s]", expected);
			fatal(msg, ex);
			fail(msg);
		}

		if (null == message) {
			assertEquals(exp, act);
		} else {
			assertEquals(message, exp, act);
		}
	}

	/**
	 * ライターを解放する。
	 * 
	 * @param writers ライター
	 */
	protected final void release(final Writer... writers) {
		for (Writer writer : writers) {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException ex) {
					warn(ex);
				}
			}
		}
	}

	/**
	 * リーダーを解放する。
	 * 
	 * @param readers リーダー
	 */
	protected final void release(final Reader... readers) {
		for (Reader reader : readers) {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					warn(ex);
				}
			}
		}
	}

	private String getStreamToString(final InputStream stream, final Charset charset) throws IOException {
		String lineSeparater = "\n";
		try {
			lineSeparater = System.getProperty("line.separator");
		} catch (SecurityException e) {
		}

		StringBuilder string = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, charset));
			String line = null;
			int index = 0;
			while (null != (line = reader.readLine())) {
				if (0 != index) {
					string.append(lineSeparater);
				}
				string.append(line);
				index++;
			}
		} finally {
			release(reader);
		}
		return string.toString();
	}
}
