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

import junit.framework.TestCase;

import org.azkfw.log.Logger;
import org.azkfw.log.LoggerFactory;
import org.azkfw.log.LoggerSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * このクラスは、テストケースの実装を行うための基底クラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/05/30
 * @author Kawakicchi
 */
@RunWith(JUnit4.class)
public abstract class AbstractTestCase extends TestCase implements LoggerSupport {

	/** Logger */
	private Logger logger;

	/**
	 * コンストラクタ
	 */
	public AbstractTestCase() {
		logger = LoggerFactory.create(getClass());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractTestCase(final String name) {
		logger = LoggerFactory.create(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractTestCase(final Class<?> clazz) {
		logger = LoggerFactory.create(clazz);
	}

	@Before
	@Override
	public void setUp() {

	}

	@After
	@Override
	public void tearDown() {

	}

	/**
	 * ロガーを設定します。
	 * 
	 * @param aLogger ロガー
	 */
	public void setLogger(final Logger aLogger) {
		logger = aLogger;
	}

	/**
	 * ログ(DEBUG)を出力します。
	 * 
	 * @param message Message
	 */
	protected final void debug(final String message) {
		logger.debug(message);
	}

	/**
	 * ログ(DEBUG)を出力します。
	 * 
	 * @param throwable Throwable
	 */
	protected final void debug(final Throwable throwable) {
		logger.debug(throwable);
	}

	/**
	 * ログ(DEBUG)を出力します。
	 * 
	 * @param message Message
	 * @param throwable Throwable
	 */
	protected final void debug(final String message, final Throwable throwable) {
		logger.debug(message, throwable);
	}

	/**
	 * ログ(INFO)を出力します。
	 * 
	 * @param message Message
	 */
	protected final void info(final String message) {
		logger.info(message);
	}

	/**
	 * ログ(INFO)を出力します。
	 * 
	 * @param throwable Throwable
	 */
	protected final void info(final Throwable throwable) {
		logger.info(throwable);
	}

	/**
	 * ログ(INFO)を出力します。
	 * 
	 * @param message Message
	 * @param throwable Throwable
	 */
	protected final void info(final String message, final Throwable throwable) {
		logger.info(message, throwable);
	}

	/**
	 * ログ(WARN)を出力します。
	 * 
	 * @param message Message
	 */
	protected final void warn(final String message) {
		logger.warn(message);
	}

	/**
	 * ログ(WARN)を出力します。
	 * 
	 * @param throwable Throwable
	 */
	protected final void warn(final Throwable throwable) {
		logger.warn(throwable);
	}

	/**
	 * ログ(WARN)を出力します。
	 * 
	 * @param message Message
	 * @param throwable Throwable
	 */
	protected final void warn(final String message, final Throwable throwable) {
		logger.warn(message, throwable);
	}

	/**
	 * ログ(ERROR)を出力します。
	 * 
	 * @param message Message
	 */
	protected final void error(final String message) {
		logger.error(message);
	}

	/**
	 * ログ(ERROR)を出力します。
	 * 
	 * @param throwable Throwable
	 */
	protected final void error(final Throwable throwable) {
		logger.error(throwable);
	}

	/**
	 * ログ(ERROR)を出力します。
	 * 
	 * @param message Message
	 * @param throwable Throwable
	 */
	protected final void error(final String message, final Throwable throwable) {
		logger.error(message, throwable);
	}

	/**
	 * ログ(FATAL)を出力します。
	 * 
	 * @param message Message
	 */
	protected final void fatal(final String message) {
		logger.fatal(message);
	}

	/**
	 * ログ(FATAL)を出力します。
	 * 
	 * @param throwable Throwable
	 */
	protected final void fatal(final Throwable throwable) {
		logger.fatal(throwable);
	}

	/**
	 * ログ(FATAL)を出力します。
	 * 
	 * @param message Message
	 * @param throwable Throwable
	 */
	protected final void fatal(final String message, final Throwable throwable) {
		logger.fatal(message, throwable);
	}
}
