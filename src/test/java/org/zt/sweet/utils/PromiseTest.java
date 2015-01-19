/**
 * 
 */
package org.zt.sweet.utils;

import java.util.concurrent.Future;

import org.junit.Test;
import org.zt.sweet.log.Logs;
import org.zt.sweet.testsuit.Bean;
import org.zt.sweet.utils.Promise;
import org.zt.sweet.utils.PromiseCallback;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class PromiseTest {

	/**
	 * Test method for
	 * {@link org.zt.sweet.common.Promise#watch(org.zt.sweet.common.PromiseCallback)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testWatch() {
		Promise<Bean> promise = new Promise<Bean>();
		promise.watch(new PromiseCallback<Bean>() {

			@Override
			public void onComplete(Bean value, Throwable error) {
				Logs.get().info("Complate " + value.getName());
			}
		});

		Future f = promise.future();
		Bean bean = new Bean("A big thing");
		promise.onComplete(bean, null);
	}
}
