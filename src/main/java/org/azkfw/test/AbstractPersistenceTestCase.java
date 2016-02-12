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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * このクラスは、永続化機能をサポートしたタスククラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/06/06
 * @author Kawakicchi
 */
public abstract class AbstractPersistenceTestCase extends AbstractPluginTestCase {

	/**
	 * コンストラクタ
	 */
	public AbstractPersistenceTestCase() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractPersistenceTestCase(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractPersistenceTestCase(final Class<?> clazz) {
		super(clazz);
	}

	@Override
	public void setUp() {
		super.setUp();

	}

	@Override
	public void tearDown() {
		super.tearDown();
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
		StringBuilder string = new StringBuilder();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(stream, charset);
			char buffer[] = new char[1024];
			int size = -1;
			while (-1 != (size = reader.read(buffer, 0, 1024))) {
				string.append(buffer, 0, size);
			}
		} finally {
			release(reader);
		}
		return string.toString();
	}
}
