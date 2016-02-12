package org.azkfw.test;

import java.io.IOException;
import java.io.InputStream;

import org.azkfw.configuration.ConfigurationFormatException;
import org.azkfw.context.Context;
import org.azkfw.log.LoggerFactory;
import org.azkfw.plugin.PluginManager;
import org.azkfw.plugin.PluginServiceException;
import org.azkfw.test.context.TestContext;
import org.azkfw.util.StringUtility;

/**
 * このクラスは、プラグイン機能をサポートしたタスククラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/06/06
 * @author Kawakicchi
 */
public abstract class AbstractPluginTestCase extends AbstractTestCase {

	/**
	 * プラグイン読み込みフラグ
	 * <p>
	 * 全てのテストで一度のみプラグインを読み込む
	 * </p>
	 */
	private static boolean PLUGIN_LOAD_FLAG = false;

	/**
	 * コンストラクタ
	 */
	public AbstractPluginTestCase() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param name 名前
	 */
	public AbstractPluginTestCase(final String name) {
		super(name);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public AbstractPluginTestCase(final Class<?> clazz) {
		super(clazz);
	}

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
						PluginManager.getInstance().load(is, context);
						PluginManager.getInstance().initialize();
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
	 * プラグインファイルを取得する。
	 * 
	 * @return プラグインファイル
	 */
	protected String getPluginFile() {
		return "conf/azuki-plugin.xml";
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
}
