package geym.nn.test;

import java.lang.reflect.Field;

import org.junit.Test;
import org.neuroph.nnet.learning.KohonenLearning;

import junit.framework.Assert;

public class TestKohonenLearning {
	@Test
	public void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		KohonenLearning kl=new KohonenLearning();
		Field f=KohonenLearning.class.getDeclaredField("mapSize");
		f.setAccessible(true);
		f.set(kl, 10);
		f.setAccessible(false);
		Assert.assertEquals(kl.distance(0, 0), 0);
		Assert.assertEquals(kl.distance(0, 1), 1);
		Assert.assertEquals(kl.distance(1, 1), 0);
		Assert.assertEquals(kl.distance(0, 99), 9);
		Assert.assertEquals(kl.distance(4, 28), 4);
	}
}
